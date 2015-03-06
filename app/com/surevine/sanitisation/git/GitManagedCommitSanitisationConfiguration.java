package com.surevine.sanitisation.git;

import java.io.File;
import java.util.Iterator;

import models.Partner;
import models.Repository;

/**
 * Special configuration for git 'commit' sanitisations
 * @author jonnyheavey
 */
public class GitManagedCommitSanitisationConfiguration extends GitManagedSanitisationConfiguration {

	private String commitMessage;

	public GitManagedCommitSanitisationConfiguration(File archive,
			Repository repository, String sanitisationIdentifier, String commitMessage) {
		super(archive, repository, sanitisationIdentifier);
		this.setCommitMessage(commitMessage);
	}

	public String getCommitMessage() {
		return commitMessage;
	}

	public void setCommitMessage(String commitMessage) {
		this.commitMessage = commitMessage;
	}

	@Override
	public String toSanitisationString() {

		StringBuilder args = new StringBuilder();

		args.append(getArchive().getAbsolutePath() + " ");
		args.append(getCommitMessage() + " ");
		args.append(getRepository().getIdentifier() + " ");
		args.append(getIdentifier() + " ");

		StringBuilder partnerNames = new StringBuilder();
		StringBuilder partnerURLs = new StringBuilder();
		Iterator<Partner> it = getRepository().getPartners().iterator();
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
