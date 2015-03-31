package com.surevine.sanitisation;

import java.io.File;
import java.util.Collections;
import java.util.List;

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
	 *
	 * @return sanitisation info string
	 */
	public List<String> toSanitisationArguments() {
		return Collections.singletonList("\"" + identifier + "\"");
	}

	public File getArchive() {
		return archive;
	}

	public void setArchive(final File archive) {
		this.archive = archive;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(final String identifier) {
		this.identifier = identifier;
	}

	public Repository getRepository() {
		return repository;
	}

	public void setRepository(final Repository repository) {
		this.repository = repository;
	}

}
