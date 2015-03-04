package com.surevine.gateway.auditing.action;

import models.Partner;

public class CreatePartnerAction implements AuditAction {

	protected Partner partner;

	public CreatePartnerAction(Partner partner) {
		this.partner = partner;
	}

	@Override
	public String getDescription() {
		return String.format("Created partner [%s][%s][%s]",
								partner.getName(),
								partner.getURL(),
								partner.getSourceKey());
	}

	@Override
	public String serialize() {
		return getDescription();
	}

}
