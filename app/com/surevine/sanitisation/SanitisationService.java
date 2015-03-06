package com.surevine.sanitisation;

import java.io.File;
import java.util.List;
import java.util.Map;

import models.Repository;

public interface SanitisationService {

	/**
	 * Determine's whether an archive passes the sanitisation rules.
	 * @param archive File containing repository contents to sanitised
	 * @param properties Metadata posted with archive
	 * @param repository Repository the archive derives from
	 * @return SanitisationResult result of sanitisation execution
	 * @throws SanitisationServiceException
	 */
	SanitisationResult sanitise(File archive, Map<String, String[]> properties, Repository repository)
			throws SanitisationServiceException;

	/**
	 * Validate data posted to sanitisation service.
	 * @param postedProperties posted data
	 * @return list of error messages (empty if valid data)
	 */
	List<String> getValidationErrors(Map<String, String[]> postedProperties);

}
