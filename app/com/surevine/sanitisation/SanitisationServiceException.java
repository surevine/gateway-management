package com.surevine.sanitisation;

public class SanitisationServiceException extends Exception {

	public SanitisationServiceException(final String message) {
        super(message);
    }

    public SanitisationServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public SanitisationServiceException(final Throwable cause) {
        super(cause);
    }

}
