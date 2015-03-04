package com.surevine.gateway.auditing.action;

import models.Partner;

public class DeletePartnerAction implements AuditAction {

	protected Partner partner;

	public DeletePartnerAction(Partner partner) {
		this.partner = partner;
	}

	@Override
	public String getDescription() {
		return String.format("Deleted partner [%s][%s][%s]",
				partner.getName(),
				partner.getURL(),
				partner.getSourceKey());
	}

	@Override
	public String serialize() {
		return getDescription();
	}

}
