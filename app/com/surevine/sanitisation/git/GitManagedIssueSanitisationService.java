package com.surevine.sanitisation.git;

import java.io.File;
import java.util.List;
import java.util.Map;

import models.Repository;

import com.surevine.community.sanitisation.SanitisationResult;
import com.surevine.sanitisation.SanitisationConfiguration;
import com.surevine.sanitisation.SanitisationService;
import com.surevine.sanitisation.SanitisationServiceException;
import com.typesafe.config.ConfigFactory;

public class GitManagedIssueSanitisationService extends GitManagedSanitisationService implements SanitisationService {

	private static GitManagedIssueSanitisationService _instance = null;

	private GitManagedIssueSanitisationService(String workingDir,
												String sanitisationRepo,
												String sanitisationScriptName)
														throws SanitisationServiceException {
		super(workingDir, sanitisationRepo, sanitisationScriptName);
	}

	public static GitManagedIssueSanitisationService getInstance() throws SanitisationServiceException {
		if(_instance == null) {
			_instance = new GitManagedIssueSanitisationService(ConfigFactory.load().getString("sanitisation.git.issue.working.dir"),
					ConfigFactory.load().getString("sanitisation.git.issue.script.repo"),
					ConfigFactory.load().getString("sanitisation.git.issue.script.name"));
		}
		return _instance;
	}

	@Override
	public SanitisationConfiguration buildSanitisationConfiguration(
			File archive, Map<String, String[]> properties,
			Repository repository) {

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
