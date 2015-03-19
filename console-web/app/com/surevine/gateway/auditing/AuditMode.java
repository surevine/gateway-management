package com.surevine.gateway.auditing;

public enum AuditMode {

	LOG,
	XML,
	UNKNOWN;

	public static AuditMode getMode(final String mode) {

		if("xml".equalsIgnoreCase(mode)) {
			return XML;
		} else if("log".equalsIgnoreCase(mode)) {
			return LOG;
		} else {
			return UNKNOWN;
		}

	}

}
