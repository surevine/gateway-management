package com.surevine.gateway.auditing;

import java.util.Date;

import com.surevine.gateway.auditing.action.AuditAction;

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
	private AuditAction action;

	/**
	 * Date/time event occurred
	 */
	private Date datetime;

	/**
	 * User who performed event
	 */
	private String username;

	public AuditEvent(AuditAction action, Date datetime, String username) {
		this.action = action;
		this.datetime = datetime;
		this.username = username;
	}

	public AuditAction getAction() {
		return action;
	}

	public void setAction(AuditAction action) {
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

}
