package com.surevine.gateway.auditing.action;

import models.Destination;

public class CreateDestinationAction implements AuditAction {

	protected Destination destination;

	public CreateDestinationAction(Destination destination) {
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
