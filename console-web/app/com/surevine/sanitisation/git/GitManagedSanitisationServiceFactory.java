package com.surevine.sanitisation.git;

import models.RepositoryType;

import com.surevine.community.sanitisation.SanitisationException;
import com.surevine.sanitisation.SanitisationService;
import com.surevine.sanitisation.SanitisationServiceFactory;

public class GitManagedSanitisationServiceFactory implements SanitisationServiceFactory {

	@Override
	public SanitisationService getSanitisationService(RepositoryType repoType) throws SanitisationException {
		switch(repoType) {
			case SCM:
				return GitManagedSCMSanitisationService.getInstance();
			case ISSUE:
				return GitManagedIssueSanitisationService.getInstance();
			default:
				throw new SanitisationException("Unsupported repository type.");
		}
	}

}
