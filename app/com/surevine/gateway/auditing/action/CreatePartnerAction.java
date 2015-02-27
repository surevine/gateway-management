package com.surevine.gateway.auditing.action;

import models.Partner;

public class CreatePartnerAction implements AuditAction {

	protected Partner destination;

	public CreatePartnerAction(Partner destination) {
		this.destination = destination;
	}

	@Override
	public String getDescription() {
		return String.format("Created destination [%s][%s][%s]",
								destination.name,
								destination.url,
								destination.sourceKey);
	}

	@Override
	public String serialize() {
		return getDescription();
	}

}
