package com.surevine.sanitisation;

import com.surevine.community.sanitisation.SanitisationException;
import com.surevine.sanitisation.git.GitManagedSanitisationServiceFactory;
import com.typesafe.config.ConfigFactory;

/**
 *
 * @author jonnyheavey
 *
 */
public abstract class Sanitisation {

	private Sanitisation() {
	}

	/**
	 * Retrieve sanitisation mode setting from property file.
	 * Used to conditionally initialise services.
	 *
	 * @return configured sanitisation mode
	 */
	private static SanitisationMode getSanitisationModeSetting() {
		return SanitisationMode.getMode(ConfigFactory.load().getString("sanitisation.mode"));
	}

	/**
	 * Load appropriate SanitisationServiceFactory according to configuration
	 * @return
	 * @throws SanitisationServiceException
	 */
	public static SanitisationServiceFactory getSanitisationServiceFactory() throws SanitisationException {
		switch(getSanitisationModeSetting()) {
			case GIT_MANAGED:
				return new GitManagedSanitisationServiceFactory();
			default:
				throw new SanitisationException("Unsupported sanitisation mode: " + getSanitisationModeSetting());
		}
	}

}
