package com.surevine.sanitisation;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import models.Partner;

import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.h2.store.fs.FileUtils;

import com.typesafe.config.ConfigFactory;

import play.Logger;

/**
 * SanitisationService implementation which executes sanitisation
 * scripts maintained in a Git repository.
 *
 * @author jonnyheavey
 */
public class GitManagedSanitisationServiceImpl implements SanitisationService {

	private static GitManagedSanitisationServiceImpl _instance = null;

	private static final int SANITISATION_SUCCESS_CODE = 0;
	private static final String SANITISATION_SCRIPT_REPO = ConfigFactory.load().getString("sanitisation.script.repo");
	private static final String SANITISATION_SCRIPT_NAME = ConfigFactory.load().getString("sanitisation.script.name");
	private static final String SANITISATION_WORKING_DIR = ConfigFactory.load().getString("sanitisation.working.dir");

	private GitManagedSanitisationServiceImpl() {
	}

	public static GitManagedSanitisationServiceImpl getInstance() {
		if(_instance == null) {
			_instance = new GitManagedSanitisationServiceImpl();
		}
		return _instance;
	}

	@Override
	public SanitisationResult sanitise(File archive,
										String projectSlug,
										String identifier,
										List<Partner> destinations)
										throws SanitisationServiceException {

		try {
			initSanitisationScript();
			updateSanitisationScript();

			Logger.info(String.format("Sanitising archive of files with identifier '%s'.", identifier));

			SanitisationResult result = executeSanitisationScripts(archive,
												projectSlug,
												identifier,
												buildDestinationURLs(destinations),
												buildDestinationNames(destinations));

			if(result.isSane()) {
				Logger.info(String.format("Archive with identifier '%s' passed sanitisation.", identifier));
			} else {
				Logger.info(String.format("Archive with identifier '%s' failed sanitisation.", identifier));
			}

			return result;

		} catch (IOException | GitAPIException | InterruptedException e) {
			throw new SanitisationServiceException("Error with sanitisation service.", e);
		}

	}

	/**
	 * Creates a local clone of sanitisation repository
	 * @throws InvalidRemoteException
	 * @throws TransportException
	 * @throws IllegalStateException
	 * @throws GitAPIException
	 */
	private void initSanitisationScript() throws InvalidRemoteException, TransportException, IllegalStateException, GitAPIException {

		final File workingDir = new File(SANITISATION_WORKING_DIR);
		final File sanitisationScript = new File(SANITISATION_WORKING_DIR + "/" + SANITISATION_SCRIPT_NAME);

		if(!sanitisationScript.exists()) {

			// Ensure directory doesn't exist (will be created by clone)
			FileUtils.deleteRecursive(SANITISATION_WORKING_DIR, true);

			Git.cloneRepository()
			.setURI(SANITISATION_SCRIPT_REPO)
			.setDirectory(workingDir)
			.setBare(false)
			.setCloneAllBranches(false)
			.call();

		}
	}

	/**
	 * Updates existing local sanitisation repository (pulls from origin)
	 * @throws IOException
	 * @throws InvalidRemoteException
	 * @throws TransportException
	 * @throws GitAPIException
	 */
	private void updateSanitisationScript() throws IOException, InvalidRemoteException, TransportException, GitAPIException {
        Git.open(new File(SANITISATION_WORKING_DIR))
        .pull()
        .setRemote("origin")
        .call();
	}

	/**
	 * Executes sanitisation shell scripts in cloned repository.
	 * @param archive
	 * @param projectSlug
	 * @param identifier
	 * @param destinationURLs
	 * @param destinationNames
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private SanitisationResult executeSanitisationScripts(File archive,
														String projectSlug,
														String identifier,
														String destinationURLs,
														String destinationNames)
														throws IOException, InterruptedException {

		SanitisationResult result = new SanitisationResult(archive, true);
		List<File> sanitisationScripts = findSanitisationScripts(new File(SANITISATION_WORKING_DIR), new ArrayList<File>());

		for(File script : sanitisationScripts) {

			String sanitisationCommand = buildSanitisationCommand(script.getAbsolutePath(),
																	archive,
																	projectSlug,
																	identifier,
																	destinationURLs,
																	destinationNames);

			Process p = Runtime.getRuntime().exec(sanitisationCommand);
			String scriptOutput = IOUtils.toString(p.getInputStream(), Charset.defaultCharset());
			int exitValue = p.waitFor();

			if((scriptOutput != null) && (scriptOutput != "")) {
				Logger.info(String.format("%s (%s)", scriptOutput, script.getAbsolutePath()));
			}

			if(exitValue != SANITISATION_SUCCESS_CODE) {
				result.setSane(false);
				result.addError(scriptOutput);
				Logger.info(String.format("Sanitisation script '%s' failed, marking archive unsafe.",
											script.getAbsolutePath()));
			}

		}

		return result;
	}

	/**
	 * Assembles sanitisation script execution command (bash) including args
	 *
	 * @param scriptPath path of script to execute
	 * @param archive File to be sanitised
	 * @param projectSlug slug of project the file contains changes for
	 * @param identifier Unique identifier to help clarify source of archive e.g. specific commit or export
	 * @param destinationURLs bar (|) separated list of destination URLs the project is being shared with
	 * @param destinationNames bar (|) separated list of destination names the project is being shared with
	 * @return String command to be executed in shell
	 */
	private String buildSanitisationCommand(String scriptPath,
											File archive,
											String projectSlug,
											String identifier,
											String destinationURLs,
											String destinationNames) {

		StringBuilder command = new StringBuilder();
		command.append(scriptPath + " ");
		command.append(archive.toString() + " ");
		command.append(projectSlug + " ");
		command.append(identifier + " ");
		command.append("\"" + destinationURLs + "\" ");
		command.append("\"" + destinationNames + "\"");

		Logger.info("Executing sanitisation command: " + command.toString());

		return command.toString();
	}

	/**
	 * Creates a bar separated list of destination names
	 * @param destinations the destination to compose the list from
	 * @return
	 */
	private String buildDestinationNames(List<Partner> destinations) {

		StringBuilder destinationNames = new StringBuilder();

		Iterator<Partner> it = destinations.iterator();
		while(it.hasNext()) {
			Partner destination = it.next();
			destinationNames.append(destination.name);
			if(it.hasNext()) {
				destinationNames.append("|");
			}
		}

		return destinationNames.toString();
	}

	/**
	 * Creates a bar separated list of destination URLs
	 * @param destinations the destination to compose the list from
	 * @return
	 */
	private String buildDestinationURLs(List<Partner> destinations) {
		StringBuilder destinationURLs = new StringBuilder();

		Iterator<Partner> it = destinations.iterator();
		while(it.hasNext()) {
			Partner destination = it.next();
			destinationURLs.append(destination.url);
			if(it.hasNext()) {
				destinationURLs.append("|");
			}
		}

		return destinationURLs.toString();
	}

	/**
	 * Finds all files in directory with configured
	 * sanitisation script name.
	 * @param directory Directory to search for files
	 * @param sanitisationScripts collection of scripts to
	 * @return list of all files found
	 */
	private List<File> findSanitisationScripts(File directory, List<File> sanitisationScripts) {
		File[] files = directory.listFiles();
		for(File file: files) {
			if(file.isDirectory()) {
				findSanitisationScripts(file, sanitisationScripts);
			} else {
				if(file.getName().equals(SANITISATION_SCRIPT_NAME)) {
					sanitisationScripts.add(file);
				}
			}
		}
		return sanitisationScripts;
	}

}
