package com.surevine.sanitisation;

import java.io.File;
import java.util.List;

import models.Destination;

public interface SanitisationService {

	/**
	 * Determine's whether an archive passes the sanitisation rules.
	 * @param archive File to be sanitised
	 * @param projectSlug slug of project the file contains changes for
	 * @param identifier Unique identifier to help clarify source of archive e.g. specific commit or export
	 * @param destinations list of destination the archive is being shared with
	 * @return SanitisationResult result of sanitisation execution
	 * @throws SanitisationServiceException
	 */
	SanitisationResult sanitise(File archive,
								String projectSlug,
								String identifier,
								List<Destination> destinations)
								throws SanitisationServiceException;

}
