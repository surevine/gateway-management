package com.surevine.gateway.auditing.action;

import models.Partner;

public class ModifyPartnerRulesAction implements AuditAction {

	protected Partner destination;
	protected String ruleFileContents;

	public ModifyPartnerRulesAction(Partner destination, String ruleFileContents) {
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
