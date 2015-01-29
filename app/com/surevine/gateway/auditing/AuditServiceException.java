package com.surevine.gateway.auditing;

public class AuditServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AuditServiceException(final String message) {
        super(message);
    }

    public AuditServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AuditServiceException(final Throwable cause) {
        super(cause);
    }

}
