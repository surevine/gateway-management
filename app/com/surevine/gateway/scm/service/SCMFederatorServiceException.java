package com.surevine.gateway.scm.service;

/**
 * Represents error when interacting with SCM federator component API
 * @author jonnyheavey
 */
public class SCMFederatorServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	public SCMFederatorServiceException(final String message) {
        super(message);
    }

    public SCMFederatorServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public SCMFederatorServiceException(final Throwable cause) {
        super(cause);
    }

}
