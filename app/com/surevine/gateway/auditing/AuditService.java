package com.surevine.gateway.auditing;

public interface AuditService {

	/**
	 * Audit an action
	 *
	 * @param action Action performed
	 * @param username User who performed action
	 * @param message
	 */
	public void audit(GatewayAction action, String username, String message);

}
