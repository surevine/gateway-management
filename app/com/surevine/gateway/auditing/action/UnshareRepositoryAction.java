package com.surevine.gateway.auditing.action;

import models.Destination;
import models.Repository;

public class UnshareRepositoryAction implements AuditAction {

	protected Repository repository;
	protected Destination destination;

	public UnshareRepositoryAction(Repository repository, Destination destination) {
		this.repository = repository;
		this.destination = destination;
	}

	@Override
	public String getDescription() {
		return String.format("Unshared repository (%s) with destination %s(%s)",
								repository.identifier,
								destination.name,
								destination.url);
	}

	@Override
	public String serialize() {
		return getDescription();
	}

}