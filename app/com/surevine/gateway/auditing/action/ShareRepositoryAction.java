package com.surevine.gateway.auditing.action;

import models.Destination;
import models.Repository;

public class ShareRepositoryAction implements AuditAction {

	protected Repository repository;
	protected Destination destination;

	public ShareRepositoryAction(Repository repository, Destination destination) {
		this.repository = repository;
		this.destination = destination;
	}

	@Override
	public String getDescription() {
		return String.format("Shared repository [%s][%s] with destination [%s][%s]. Repository sent to gateway for export.",
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