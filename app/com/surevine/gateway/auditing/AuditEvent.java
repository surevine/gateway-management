package com.surevine.gateway.auditing;

import java.util.Date;

/**
 * Represents an auditable action in the system
 *
 * @author jonnyheavey
 *
 */
public class AuditEvent {

	/**
	 * Audit action that occurred
	 */
	private GatewayAction action;

	/**
	 * Date/time event occurred
	 */
	private Date datetime;

	/**
	 * User who performed event
	 */
	private String username;

	/**
	 * Descriptive message about event
	 */
	private String message;

	public AuditEvent(GatewayAction action, Date datetime, String username, String message) {
		this.action = action;
		this.datetime = datetime;
		this.username = username;
		this.message = message;
	}

	public GatewayAction getAction() {
		return action;
	}

	public void setAction(GatewayAction action) {
		this.action = action;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
