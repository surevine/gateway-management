package com.surevine.gateway.auth;

/**
 * Represents error when interacting with AuthServiceProxy implementation.
 *
 * @author jonnyheavey
 *
 */
public class AuthServiceProxyException extends Exception {

	public AuthServiceProxyException(final String message) {
        super(message);
    }

    public AuthServiceProxyException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AuthServiceProxyException(final Throwable cause) {
        super(cause);
    }

}
