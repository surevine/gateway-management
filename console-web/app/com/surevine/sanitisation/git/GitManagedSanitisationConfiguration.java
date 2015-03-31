package com.surevine.sanitisation.git;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import models.FederationConfiguration;
import models.Partner;
import models.Repository;

import org.apache.commons.lang3.StringUtils;

import com.surevine.sanitisation.SanitisationConfiguration;

/**
 * Sanitisation configuration for git managed sanitisation service.
 *
 * @author jonnyheavey
 */
public class GitManagedSanitisationConfiguration extends SanitisationConfiguration {

	public GitManagedSanitisationConfiguration(final File archive, final Repository repository,
			final String sanitisationIdentifier) {

		this.setArchive(archive);
		this.setRepository(repository);
		this.setIdentifier(sanitisationIdentifier);
	}

	@Override
	public List<String> toSanitisationArguments() {
		final List<String> args = new ArrayList<String>();

		args.add("\"" + getArchive().getAbsolutePath() + "\"");
		args.add("\"" + getRepository().getIdentifier() + "\"");
		args.add("\"" + getIdentifier() + "\"");

		final StringBuilder partnerNames = new StringBuilder();
		final StringBuilder partnerURLs = new StringBuilder();

		final Iterator<FederationConfiguration> it = getRepository().getFederationConfigurations().iterator();
		while (it.hasNext()) {
			final FederationConfiguration fedConfig = it.next();
			if (fedConfig.outboundEnabled) {
				final Partner partner = fedConfig.getPartner();
				partnerNames.append(partner.getName());
				partnerURLs.append(partner.getUrl());
				if (it.hasNext()) {
					partnerNames.append("|");
					partnerURLs.append("|");
				}
			}
		}

		args.add("\"" + StringUtils.strip(partnerURLs.toString(), "|") + "\"");
		args.add("\"" + StringUtils.strip(partnerNames.toString(), "|") + "\"");

		return args;
	}

}
