package com.surevine.sanitisation;

import java.io.File;

public interface SanitisationService {

	/**
	 * Determine's whether an archive passes the sanitisation rules.
	 * @param archive tar.gz archive to be sanitised
	 * @return SanitisationResult result of sanitisation execution
	 */
	SanitisationResult sanitise(File archive);

}
