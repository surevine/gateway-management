package com.surevine.sanitisation;

import java.io.File;

public interface SanitisationService {

	/**
	 * Determine's whether an archive passes the sanitisation rules.
	 * @param archive tar.gz archive to be sanitised
	 * @return boolean True if archive is valid
	 */
	public boolean isSane(File archive);

}
