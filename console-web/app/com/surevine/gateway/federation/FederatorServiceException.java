package com.surevine.gateway.federation;

public class FederatorServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	public FederatorServiceException(final String message) {
        super(message);
    }

    public FederatorServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public FederatorServiceException(final Throwable cause) {
        super(cause);
    }

}
