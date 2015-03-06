package com.surevine.sanitisation.git;

import java.io.File;
import java.util.Iterator;

import com.surevine.sanitisation.SanitisationConfiguration;

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
