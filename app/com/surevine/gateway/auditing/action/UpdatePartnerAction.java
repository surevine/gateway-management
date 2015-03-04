package com.surevine.gateway.auditing.action;

import models.Partner;

public class UpdatePartnerAction implements AuditAction {

	protected Partner originalPartner;
	protected Partner updatedPartner;

	public UpdatePartnerAction(Partner originalPartner, Partner updatedPartner) {
		this.originalPartner = originalPartner;
		this.updatedPartner = updatedPartner;
	}

	@Override
	public String getDescription() {
		return String.format("Updated partner [%s][%s][%s]",
				updatedPartner.getName(),
				updatedPartner.getURL(),
				updatedPartner.getSourceKey());
	}

	@Override
	public String serialize() {
		return getDescription();
	}

}
