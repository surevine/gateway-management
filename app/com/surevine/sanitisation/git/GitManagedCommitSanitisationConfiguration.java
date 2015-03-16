package com.surevine.sanitisation.git;

import java.io.File;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

import models.FederationConfiguration;
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
		args.append("\"" + getCommitMessage() + "\" ");
		args.append(getRepository().getIdentifier() + " ");
		args.append(getIdentifier() + " ");

		StringBuilder partnerNames = new StringBuilder();
		StringBuilder partnerURLs = new StringBuilder();

		Iterator<FederationConfiguration> it = getRepository().getFederationConfigurations().iterator();
		while(it.hasNext()) {
			FederationConfiguration fedConfig = it.next();
			if(fedConfig.outboundEnabled) {
				Partner partner = fedConfig.getPartner();
				partnerNames.append(partner.getName());
				partnerURLs.append(partner.getUrl());
				if(it.hasNext()) {
					partnerNames.append("|");
					partnerURLs.append("|");
				}
			}
		}

		args.append("\"" + StringUtils.strip(partnerURLs.toString(), "|") + "\" ");
		args.append("\"" + StringUtils.strip(partnerNames.toString(), "|") + "\"");

		return args.toString();
	}

}
