package com.surevine.gateway.auditing.action;

import models.Destination;

public class DeleteDestinationAction implements AuditAction {

	protected Destination destination;

	public DeleteDestinationAction(Destination destination) {
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
