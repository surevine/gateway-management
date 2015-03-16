package com.surevine.sanitisation;

/**
 * Supported sanitisation modes
 * @author jonnyheavey
 *
 */
public enum SanitisationMode {

	GIT_MANAGED;

	public static SanitisationMode getMode(final String mode) {
		if("git".equalsIgnoreCase(mode)) {
			return GIT_MANAGED;
		}
		return null;
	}

}
