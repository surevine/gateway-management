package com.surevine.sanitisation.git;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.h2.store.fs.FileUtils;

import play.Logger;

import com.surevine.sanitisation.SanitisationConfiguration;
import com.surevine.sanitisation.SanitisationResult;
import com.surevine.sanitisation.SanitisationServiceException;

public abstract class GitManagedSanitisationService {

	protected static final int SANITISATION_SUCCESS_CODE = 0;

	protected final File workingDir;
	protected final String sanitisationRepo;
	protected final String sanitisationScriptName;

	protected GitManagedSanitisationService(String workingDir, String sanitisationRepo, String sanitisationScriptName) throws SanitisationServiceException {
		this.workingDir = new File(workingDir);
		this.sanitisationRepo = sanitisationRepo;
		this.sanitisationScriptName = sanitisationScriptName;

		try {
			initSanitisationScript();
		} catch (IllegalStateException | GitAPIException e) {
			throw new SanitisationServiceException("Error initialising sanitisation service.", e);
		}
	}

	/**
	 * Retrieve any errors in posted data
	 * @param postedProperties
	 * @return
	 */
	protected List<String> getValidationErrors(Map<String, String[]> postedProperties) {

		List<String> errors = new ArrayList<String>();

		String sanitisationIdentifier = postedProperties.get("sanitisationIdentifier")[0];
		if(sanitisationIdentifier == null) {
			errors.add("sanitisationIdentifier is missing from request");
		}

		return errors;
	}

	/**
	 * Updates existing local sanitisation repository (pulls from origin)
	 * @throws IOException
	 * @throws InvalidRemoteException
	 * @throws TransportException
	 * @throws GitAPIException
	 */
	protected void updateSanitisationScript() throws IOException, InvalidRemoteException, TransportException, GitAPIException {
        Git.open(workingDir)
        .pull()
        .setRemote("origin")
        .call();
	}

	/**
	 * Executes sanitisation shell scripts in cloned repository.
	 * @param GitManagedSanitisationConfiguration config sanitisation configuration
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	protected SanitisationResult executeSanitisationScripts(SanitisationConfiguration config) throws IOException, InterruptedException {

		SanitisationResult result = new SanitisationResult(config.getArchive(), true);

		List<File> sanitisationScripts = findSanitisationScripts(workingDir, new ArrayList<File>());

		for(File script : sanitisationScripts) {

			String sanitisationCommand = buildSanitisationCommand(script.getAbsolutePath(), config.toSanitisationString());

			Logger.info("Executing: " + sanitisationCommand);

			Process p = Runtime.getRuntime().exec(sanitisationCommand);
			String scriptOutput = IOUtils.toString(p.getInputStream(), Charset.defaultCharset());
			int exitValue = p.waitFor();

			if((scriptOutput != null) && (scriptOutput.trim() != "")) {
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
	 * Perform sanitisation (validation) of archive
	 * @param archive
	 * @param properties
	 * @param repository
	 * @return
	 * @throws SanitisationServiceException
	 */
	protected SanitisationResult sanitise(SanitisationConfiguration config) throws SanitisationServiceException {

		try {

			updateSanitisationScript();

			Logger.info(String.format("Sanitising archive of files with identifier '%s' for %s repository '%s'.",
					config.getIdentifier(),
					config.getRepository().getRepoType(),
					config.getRepository().getIdentifier()));

			SanitisationResult result = executeSanitisationScripts(config);

			if(result.isSane()) {
				Logger.info(String.format("Archive with identifier '%s' for %s repository '%s' passed sanitisation.",
						config.getIdentifier(),
						config.getRepository().getRepoType(),
						config.getRepository().getIdentifier()));
			} else {
				// TODO audit script failure
				Logger.info(String.format("Archive with identifier '%s' for %s repository '%s' failed sanitisation.",
						config.getIdentifier(),
						config.getRepository().getRepoType(),
						config.getRepository().getIdentifier()));
			}

			return result;

		} catch (IOException | GitAPIException | InterruptedException e) {
			throw new SanitisationServiceException("Error with sanitisation service.", e);
		}

	}

	/**
	 * Assemble sanitisation script command to execute
	 * @param sanitisationScriptPath path to script
	 * @param scriptArgs arguments to provide to script
	 * @return
	 */
	private String buildSanitisationCommand(String sanitisationScriptPath, String scriptArgs) {
		return sanitisationScriptPath + " " + scriptArgs;
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
				if(file.getName().equals(sanitisationScriptName)) {
					sanitisationScripts.add(file);
				}
			}
		}
		return sanitisationScripts;
	}

	/**
	 * Creates a local clone of sanitisation repository
	 * @throws InvalidRemoteException
	 * @throws TransportException
	 * @throws IllegalStateException
	 * @throws GitAPIException
	 */
	private void initSanitisationScript() throws InvalidRemoteException, TransportException, IllegalStateException, GitAPIException {
		File sanitisationScript = new File(workingDir.getPath() + "/" + sanitisationScriptName);
		if(!sanitisationScript.exists()) {
			// Ensure directory doesn't exist (will be created by clone)
			FileUtils.deleteRecursive(workingDir.getPath(), true);
			Git.cloneRepository()
			.setURI(sanitisationRepo)
			.setDirectory(workingDir)
			.setBare(false)
			.setCloneAllBranches(false)
			.call();
		}
	}

}