package com.surevine.gateway.auditing.action;

public interface AuditAction {

	/**
	 * Human-readable description of action
	 *
	 * @return
	 */
	public String getDescription();

	/**
	 * Produces String representation of action
	 *
	 * @return
	 */
	public String serialize();

}
