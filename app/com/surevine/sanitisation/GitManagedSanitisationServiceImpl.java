package com.surevine.sanitisation;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.h2.store.fs.FileUtils;

import com.typesafe.config.ConfigFactory;

import play.Logger;

/**
 * SanitisationService implementation which executes scripts
 * maintained in a Git repository.
 *
 * @author jonnyheavey
 */
public class GitManagedSanitisationServiceImpl implements SanitisationService {

	private static GitManagedSanitisationServiceImpl _instance = null;

	private static final int SANITISATION_SUCCESS_CODE = 0;
	private static final String SANITISATION_SCRIPT_REPO = ConfigFactory.load().getString("sanitisation.script.repo");
	private static final String SANITISATION_SCRIPT_NAME = ConfigFactory.load().getString("sanitisation.script.name");
	private static final String SANITISATION_WORKING_DIR = ConfigFactory.load().getString("sanitisation.working.dir");

	private GitManagedSanitisationServiceImpl() {}

	public static GitManagedSanitisationServiceImpl getInstance() {
		if(_instance == null) {
			_instance = new GitManagedSanitisationServiceImpl();
		}
		return _instance;
	}

	@Override
	public boolean isSane(File archive) {

		try {
			initSanitisationScript();
			updateSanitisationScript();
		} catch (IllegalStateException | GitAPIException | IOException e1) {
			Logger.error("Failed to initialise sanitisation script. Rejecting commit.", e1);
			return false;
		}

		int scriptResponse;
		try {
			scriptResponse = executeSanitisationScript(archive);
		} catch (IOException | InterruptedException e2) {
			Logger.error("Error during sanitisation script execution.", e2);
			return false;
		}

		if(scriptResponse != SANITISATION_SUCCESS_CODE) {
			// Sanitisation script returned error, so commit not sane.
			return false;
		}

		return true;
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
	 * Executes sanitisation shell script.
	 * @param archive archive file to be sanitised.
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private int executeSanitisationScript(File archive) throws IOException, InterruptedException {

		Process p = Runtime.getRuntime().exec(SANITISATION_WORKING_DIR + "/" + SANITISATION_SCRIPT_NAME);
		int exitValue = p.waitFor();

		// TODO read output from script (to relay back to client)
		return exitValue;
	}

}
