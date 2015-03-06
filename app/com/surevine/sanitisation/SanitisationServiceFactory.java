package com.surevine.sanitisation;

import models.RepositoryType;

public interface SanitisationServiceFactory {

	/**
	 * Load appropriate SanitisaitonService implementation
	 * depending on type of repository to be validated / sanitised
	 * @param repoType Type of repository to be sanitised
	 * @return
	 * @throws SanitisationServiceException
	 */
	public SanitisationService getSanitisationService(RepositoryType repoType) throws SanitisationServiceException;

}
