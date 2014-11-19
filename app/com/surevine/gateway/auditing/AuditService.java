package com.surevine.gateway.auditing;

public interface AuditService {

	/**
	 * Audit an action
	 *
	 * @param event event to be recorded
	 */
	public void audit(AuditEvent event);

}
