package com.surevine.sanitisation;

import java.io.File;
import models.Repository;

public abstract class SanitisationConfiguration {

	/**
	 * Archive of files to be sanitised/validated
	 */
	private File archive;

	/**
	 * Identifier/description of sanitisation execution
	 */
	private String identifier;

	/**
	 * Repository the archive derives from
	 */
	private Repository repository;

	/**
	 * Build string containing configuration info
	 * for use in sanitisation execution.
	 * @return sanitisation info string
	 */
	public String toSanitisationString() {
		return identifier;
	}

	public File getArchive() {
		return archive;
	}

	public void setArchive(File archive) {
		this.archive = archive;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

}
