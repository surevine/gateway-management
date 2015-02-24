package com.surevine.gateway.auditing.action;

import models.Destination;

public class ModifyDestinationRulesAction implements AuditAction {

	protected Destination destination;
	protected String ruleFileContents;

	public ModifyDestinationRulesAction(Destination destination, String ruleFileContents) {
		this.destination = destination;
		this.ruleFileContents = ruleFileContents;
	}

	@Override
	public String getDescription() {
		return String.format("Updated destination [%s][%s] export rules.",
								destination.name,
								destination.url);
	}

	@Override
	public String serialize() {
		return getDescription();
	}

}
