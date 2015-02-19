package com.surevine.gateway.auditing.action;

import models.Destination;
import models.Repository;

public class ResendRepositoryAction implements AuditAction {

	protected Repository repository;
	protected Destination destination;

	public ResendRepositoryAction(Repository repository, Destination destination) {
		this.repository = repository;
		this.destination = destination;
	}

	@Override
	public String getDescription() {
		return String.format("Resent repository (%s) to gateway for export to destination %s(%s)",
								repository.identifier,
								destination.name,
								destination.url);
	}

	@Override
	public String serialize() {
		return getDescription();
	}

}