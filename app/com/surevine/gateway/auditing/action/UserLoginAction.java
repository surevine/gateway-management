package com.surevine.gateway.auditing.action;

public class UserLoginAction implements AuditAction {

	private String username;

	public UserLoginAction(String username) {
		this.username = username;
	}

	@Override
	public String getDescription() {
		return String.format("User %s logged in.", username);
	}

	@Override
	public String serialize() {
		return getDescription();
	}

}