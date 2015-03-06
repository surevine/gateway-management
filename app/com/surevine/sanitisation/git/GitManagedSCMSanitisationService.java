package com.surevine.sanitisation.git;

import java.io.File;
import java.util.List;
import java.util.Map;

import models.Repository;

import com.surevine.sanitisation.SanitisationConfiguration;
import com.surevine.sanitisation.SanitisationResult;
import com.surevine.sanitisation.SanitisationService;
import com.surevine.sanitisation.SanitisationServiceException;
import com.typesafe.config.ConfigFactory;

public class GitManagedSCMSanitisationService extends GitManagedSanitisationService implements SanitisationService {

	private static GitManagedSCMSanitisationService _instance = null;

	private GitManagedSCMSanitisationService(String workingDir,
											String sanitisationRepo,
											String sanitisationScriptName)
													throws SanitisationServiceException {
		super(workingDir, sanitisationRepo, sanitisationScriptName);
	}

	public static GitManagedSCMSanitisationService getInstance() throws SanitisationServiceException {
		if(_instance == null) {
			_instance = new GitManagedSCMSanitisationService(ConfigFactory.load().getString("sanitisation.git.scm.working.dir"),
					ConfigFactory.load().getString("sanitisation.git.scm.script.repo"),
					ConfigFactory.load().getString("sanitisation.git.scm.script.name"));
		}
		return _instance;
	}

	@Override
	public SanitisationConfiguration buildSanitisationConfiguration(
			File archive, Map<String, String[]> properties,
			Repository repository) {

		if(properties.containsKey("commitMessage")) {
			return new GitManagedCommitSanitisationConfiguration(archive,
						repository,
						properties.get("sanitisationIdentifier")[0],
						properties.get("commitMessage")[0]);
		}

		return new GitManagedSanitisationConfiguration(archive,
				repository,
				properties.get("sanitisationIdentifier")[0]);
	}

	@Override
	public SanitisationResult sanitise(SanitisationConfiguration config) throws SanitisationServiceException {
		return super.sanitise(config);
	}

	@Override
	public List<String> getValidationErrors(Map<String, String[]> postedProperties) {
		return super.getValidationErrors(postedProperties);
	}

}
