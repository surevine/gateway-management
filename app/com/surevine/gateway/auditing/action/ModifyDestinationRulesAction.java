package com.surevine.gateway.auditing.action;

import models.Destination;

public class ModifyDestinationRulesAction implements AuditAction {

	private Destination destination;
	private String ruleFileContents;

	public ModifyDestinationRulesAction(Destination destination, String ruleFileContents) {
		this.destination = destination;
		this.ruleFileContents = ruleFileContents;
	}

	@Override
	public String getDescription() {
		return String.format("Updated destination %s(%s) rules.",
								destination.name,
								destination.url);
	}

	@Override
	public String serialize() {
		return getDescription();
	}

}
