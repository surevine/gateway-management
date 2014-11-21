package com.surevine.gateway.auditing.action;

import models.Destination;

public class UpdateDestinationAction implements AuditAction {

	protected Destination originalDestination;
	protected Destination updatedDestination;

	public UpdateDestinationAction(Destination originalDestination, Destination updatedDestination) {
		this.originalDestination = originalDestination;
		this.updatedDestination = updatedDestination;
	}

	@Override
	public String getDescription() {
		return String.format("Updated destination %s (%s)",
								updatedDestination.name,
								updatedDestination.url);
	}

	@Override
	public String serialize() {
		return getDescription();
	}

}
