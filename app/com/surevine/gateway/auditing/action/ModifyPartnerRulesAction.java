package com.surevine.gateway.auditing.action;

import models.Partner;

public class ModifyPartnerRulesAction implements AuditAction {

	protected Partner partner;
	protected String ruleFileContents;

	public ModifyPartnerRulesAction(Partner partner, String ruleFileContents) {
		this.partner = partner;
		this.ruleFileContents = ruleFileContents;
	}

	@Override
	public String getDescription() {
		return String.format("Updated partner [%s][%s] export rules.",
				partner.getName(),
				partner.getURL());
	}

	@Override
	public String serialize() {
		return getDescription();
	}

}
