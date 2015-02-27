package com.surevine.gateway.auditing.action;

import models.Partner;

public class UpdatePartnerAction implements AuditAction {

	protected Partner originalDestination;
	protected Partner updatedDestination;

	public UpdatePartnerAction(Partner originalDestination, Partner updatedDestination) {
		this.originalDestination = originalDestination;
		this.updatedDestination = updatedDestination;
	}

	@Override
	public String getDescription() {
		return String.format("Updated destination [%s][%s][%s]",
								updatedDestination.name,
								updatedDestination.url,
								updatedDestination.sourceKey);
	}

	@Override
	public String serialize() {
		return getDescription();
	}

}
