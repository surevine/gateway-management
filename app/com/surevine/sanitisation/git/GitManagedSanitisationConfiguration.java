package com.surevine.sanitisation.git;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import models.Partner;
import models.Repository;

public class GitManagedSanitisationConfiguration {

	private File archive;
	private Repository repository;
	private String sanitisationIdentifier;
	private List<Partner> partners;

	public GitManagedSanitisationConfiguration(File archive,
			Repository repository,
			String sanitisationIdentifier) {

		this.setArchive(archive);
		this.setRepository(repository);
		this.setSanitisationIdentifier(sanitisationIdentifier);
		this.setPartners(repository.getPartners());
	}

	public File getArchive() {
		return archive;
	}


	public void setArchive(File archive) {
		this.archive = archive;
	}

	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	public String getSanitisationIdentifier() {
		return sanitisationIdentifier;
	}

	public void setSanitisationIdentifier(String sanitisationIdentifier) {
		this.sanitisationIdentifier = sanitisationIdentifier;
	}

	public List<Partner> getPartners() {
		return partners;
	}

	public void setPartners(List<Partner> partners) {
		this.partners = partners;
	}

	/**
	 * Build argument string for use in sanitisation script execution.
	 * @return String argument string
	 */
	public String buildScriptArgsString() {

		StringBuilder args = new StringBuilder();

		args.append(archive.getAbsolutePath() + " ");
		args.append(getRepository().getIdentifier() + " ");
		args.append(getSanitisationIdentifier() + " ");

		StringBuilder partnerNames = new StringBuilder();
		StringBuilder partnerURLs = new StringBuilder();
		Iterator<Partner> it = partners.iterator();
		while(it.hasNext()) {
			Partner partner = it.next();
			partnerNames.append(partner.name);
			partnerURLs.append(partner.url);
			if(it.hasNext()) {
				partnerNames.append("|");
				partnerURLs.append("|");
			}
		}
		args.append(partnerURLs.toString() + " ");
		args.append(partnerNames.toString());

		return args.toString();
	}

}
