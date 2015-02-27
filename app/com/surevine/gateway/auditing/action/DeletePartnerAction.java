package com.surevine.gateway.auditing.action;

import models.Partner;

public class DeletePartnerAction implements AuditAction {

	protected Partner destination;

	public DeletePartnerAction(Partner destination) {
		this.destination = destination;
	}

	@Override
	public String getDescription() {
		return String.format("Deleted destination [%s][%s][%s]",
								destination.name,
								destination.url,
								destination.sourceKey);
	}

	@Override
	public String serialize() {
		return getDescription();
	}

}
