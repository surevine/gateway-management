package com.surevine.sanitisation.git;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

import com.surevine.community.sanitisation.SanitisationException;
import com.surevine.community.sanitisation.SanitisationResult;
import com.surevine.sanitisation.SanitisationConfiguration;

public abstract class GitManagedSanitisationService {

	protected static final int SANITISATION_SUCCESS_CODE = 0;

	protected final File workingDir;
	protected final String sanitisationRepo;
	protected final String sanitisationScriptName;

	protected GitManagedSanitisationService(final String workingDir, final String sanitisationRepo,
			final String sanitisationScriptName) throws SanitisationException {
		this.workingDir = new File(workingDir);
		this.sanitisationRepo = sanitisationRepo;
		this.sanitisationScriptName = sanitisationScriptName;

		try {
			initSanitisationScript();
		} catch (IllegalStateException | GitAPIException e) {
			throw new SanitisationException("Error initialising sanitisation service.", e);
		}
	}

	/**
	 * Retrieve any errors in posted data
	 *
	 * @param postedProperties
	 * @return
	 */
	protected List<String> getValidationErrors(final Map<String, String[]> postedProperties) {

		final List<String> errors = new ArrayList<String>();

		final String sanitisationIdentifier = postedProperties.get("sanitisationIdentifier")[0];
		if (sanitisationIdentifier == null) {
			errors.add("sanitisationIdentifier is missing from request");
		}

		return errors;
	}

	/**
	 * Updates existing local sanitisation repository (pulls from origin)
	 *
	 * @throws IOException
	 * @throws InvalidRemoteException
	 * @throws TransportException
	 * @throws GitAPIException
	 */
	protected void updateSanitisationScript() throws IOException, InvalidRemoteException, TransportException,
			GitAPIException {
		Git.open(workingDir).pull().setRemote("origin").call();
	}

	/**
	 * Executes sanitisation shell scripts in cloned repository.
	 *
	 * @param GitManagedSanitisationConfiguration
	 *            config sanitisation configuration
	 * @return
	 * @throws SanitisationException
	 */
	protected SanitisationResult executeSanitisationScripts(final SanitisationConfiguration config)
			throws SanitisationException {

		final SanitisationResult result = new SanitisationResult(config.getArchive(), config.getRepository()
				.getIdentifier(), config.getIdentifier(), true);

		final List<File> sanitisationScripts = findSanitisationScripts(workingDir, new ArrayList<File>());

		for (final File script : sanitisationScripts) {

			final String[] sanitisationCommand = buildSanitisationCommand(script.getAbsolutePath(), config);

			Logger.info("Executing: " + sanitisationCommand);

			InputStream processOutput = null;
			String scriptOutput = null;
			int exitValue = 0;

			try {
				final Process p = Runtime.getRuntime().exec(sanitisationCommand);
				processOutput = p.getInputStream();
				scriptOutput = IOUtils.toString(processOutput, Charset.defaultCharset());
				exitValue = p.waitFor();
			} catch (IOException | InterruptedException e) {
				throw new SanitisationException("Error during sanitisation script execution.", e);
			} finally {
				IOUtils.closeQuietly(processOutput);
			}

			if ((scriptOutput != null) && (scriptOutput.trim() != "")) {
				Logger.info(String.format("%s (%s)", scriptOutput, script.getAbsolutePath()));
			}

			if (exitValue != SANITISATION_SUCCESS_CODE) {
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
	 *
	 * @param archive
	 * @param properties
	 * @param repository
	 * @return
	 * @throws SanitisationException
	 */
	protected SanitisationResult sanitise(final SanitisationConfiguration config) throws SanitisationException {

		try {

			updateSanitisationScript();

			Logger.info(String.format("Sanitising archive of files with identifier '%s' for %s repository '%s'.",
					config.getIdentifier(), config.getRepository().getRepoType(), config.getRepository()
							.getIdentifier()));

			final SanitisationResult result = executeSanitisationScripts(config);

			if (result.isSane()) {
				Logger.info(String.format("Archive with identifier '%s' for %s repository '%s' passed sanitisation.",
						config.getIdentifier(), config.getRepository().getRepoType(), config.getRepository()
								.getIdentifier()));
			} else {
				// TODO audit script failure
				Logger.info(String.format("Archive with identifier '%s' for %s repository '%s' failed sanitisation.",
						config.getIdentifier(), config.getRepository().getRepoType(), config.getRepository()
								.getIdentifier()));
			}

			return result;

		} catch (IOException | GitAPIException e) {
			throw new SanitisationException("Error with sanitisation service.", e);
		}

	}

	/**
	 * Assemble sanitisation script command to execute
	 *
	 * @param sanitisationScriptPath
	 *            path to script
	 * @param scriptArgs
	 *            arguments to provide to script
	 * @return
	 */
	private String[] buildSanitisationCommand(final String sanitisationScriptPath,
			final SanitisationConfiguration config) {
		final List<String> arguments = config.toSanitisationArguments();
		final String[] command = new String[arguments.size() + 1];
		command[0] = sanitisationScriptPath;
		for (int i = 0; i < arguments.size(); i++) {
			command[i + 1] = arguments.get(i);
		}
		return command;
	}

	/**
	 * Finds all files in directory with configured
	 * sanitisation script name.
	 *
	 * @param directory
	 *            Directory to search for files
	 * @param sanitisationScripts
	 *            collection of scripts to
	 * @return list of all files found
	 */
	private List<File> findSanitisationScripts(final File directory, final List<File> sanitisationScripts) {
		final File[] files = directory.listFiles();
		for (final File file : files) {
			if (file.isDirectory()) {
				findSanitisationScripts(file, sanitisationScripts);
			} else {
				if (file.getName().equals(sanitisationScriptName)) {
					sanitisationScripts.add(file);
				}
			}
		}
		return sanitisationScripts;
	}

	/**
	 * Creates a local clone of sanitisation repository
	 *
	 * @throws InvalidRemoteException
	 * @throws TransportException
	 * @throws IllegalStateException
	 * @throws GitAPIException
	 */
	private void initSanitisationScript() throws InvalidRemoteException, TransportException, IllegalStateException,
			GitAPIException {
		final File sanitisationScript = new File(workingDir.getPath() + "/" + sanitisationScriptName);
		if (!sanitisationScript.exists()) {
			// Ensure directory doesn't exist (will be created by clone)
			FileUtils.deleteRecursive(workingDir.getPath(), true);
			Git.cloneRepository().setURI(sanitisationRepo).setDirectory(workingDir).setBare(false)
					.setCloneAllBranches(false).call();
		}
	}

}
