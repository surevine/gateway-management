package com.surevine.sanitisation;

import java.io.File;
import java.util.List;

import models.Destination;

public interface SanitisationService {

	/**
	 * Determine's whether an archive passes the sanitisation rules.
	 * @param archive tar.gz archive to be sanitised
	 * @return SanitisationResult result of sanitisation execution
	 */
	SanitisationResult sanitise(File archive,
								String projectSlug,
								String commitId,
								List<Destination> destinations)
								throws SanitisationServiceException;

}
