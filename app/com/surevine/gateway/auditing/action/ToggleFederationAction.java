package com.surevine.gateway.auditing.action;

import models.Destination;
import models.Repository;

public class ToggleFederationAction implements AuditAction {

	protected Repository repository;
	protected Destination destination;
	protected String direction;
	protected String action;

	public ToggleFederationAction(Repository repository, Destination destination, String direction, boolean enabled) {
		this.repository = repository;
		this.destination = destination;
		this.direction = direction;

		this.action = "Disabled";
		if(enabled) {
			this.action = "Enabled";
		}
	}

	@Override
	public String getDescription() {
		return String.format("%s %s federation between repository [%s][%s] and destination [%s][%s].",
				action,
				direction,
				repository.repoType,
				repository.identifier,
				destination.name,
				destination.url);
	}

	@Override
	public String serialize() {
		return getDescription();
	}

}
