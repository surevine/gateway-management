package com.surevine.gateway.auditing.action;

import models.Destination;

public class DeleteDestinationAction implements AuditAction {

	private Destination destination;

	public DeleteDestinationAction(Destination destination) {
		this.destination = destination;
	}

	@Override
	public String getDescription() {
		return String.format("Deleted destination %s(%s)",
								destination.name,
								destination.url);
	}

	@Override
	public String serialize() {
		return getDescription();
	}

}
