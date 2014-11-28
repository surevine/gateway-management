package com.surevine.gateway.auth;

/**
 * Represents error when interacting with AuthServiceProxy implementation.
 *
 * @author jonnyheavey
 *
 */
public class AuthServiceException extends Exception {

	public AuthServiceException(final String message) {
        super(message);
    }

    public AuthServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AuthServiceException(final Throwable cause) {
        super(cause);
    }

}
