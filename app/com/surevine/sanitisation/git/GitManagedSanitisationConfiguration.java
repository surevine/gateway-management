package com.surevine.sanitisation.git;

import java.io.File;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

import com.surevine.sanitisation.SanitisationConfiguration;

import models.FederationConfiguration;
import models.Partner;
import models.Repository;

/**
 * Sanitisation configuration for git managed sanitisation service.
 * @author jonnyheavey
 */
public class GitManagedSanitisationConfiguration extends SanitisationConfiguration {

	public GitManagedSanitisationConfiguration(File archive,
			Repository repository,
			String sanitisationIdentifier) {

		this.setArchive(archive);
		this.setRepository(repository);
		this.setIdentifier(sanitisationIdentifier);
	}

	@Override
	public String toSanitisationString() {

		StringBuilder args = new StringBuilder();

		args.append(getArchive().getAbsolutePath() + " ");
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
