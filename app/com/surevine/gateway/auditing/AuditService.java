package com.surevine.gateway.auditing;

public interface AuditService {

	/**
	 * Audit an action
	 *
	 * @param action Action performed
	 * @param datatime Date/Time action was performed
	 * @param username User who performed action
	 * @param message
	 */
	public void audit(AuditEvent event);

}
