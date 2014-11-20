package com.surevine.gateway.auditing.action;

import models.Destination;

public class CreateDestinationAction implements AuditAction {

	private Destination destination;

	public CreateDestinationAction(Destination destination) {
		this.destination = destination;
	}

	@Override
	public String getDescription() {
		return String.format("Created destination %s(%s)",
								destination.name,
								destination.url);
	}

	@Override
	public String serialize() {
		return getDescription();
	}

}
