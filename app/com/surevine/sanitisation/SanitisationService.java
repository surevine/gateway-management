package com.surevine.sanitisation;

import com.surevine.community.sanitisation.SanitisationException;
import com.surevine.community.sanitisation.SanitisationResult;

import java.io.File;
import java.util.List;
import java.util.Map;

import models.Repository;

public interface SanitisationService {

	/**
	 *
	 * @param archive
	 * @param properties
	 * @param repository
	 * @return
	 */
	SanitisationConfiguration buildSanitisationConfiguration(File archive,
			Map<String, String[]> properties, Repository repository);

	/**
	 * Determine's whether an archive passes the sanitisation rules.
	 * @param archive File containing repository contents to sanitised
	 * @param properties Metadata posted with archive
	 * @param repository Repository the archive derives from
	 * @return SanitisationResult result of sanitisation execution
	 * @throws SanitisationServiceException
	 */
	SanitisationResult sanitise(SanitisationConfiguration config)
			throws SanitisationException;

	/**
	 * Validate data posted to sanitisation service.
	 * @param postedProperties posted data
	 * @return list of error messages (empty if valid data)
	 */
	List<String> getValidationErrors(Map<String, String[]> postedProperties);

}
