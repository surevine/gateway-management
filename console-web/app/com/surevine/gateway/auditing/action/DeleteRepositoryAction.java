package com.surevine.gateway.auditing.action;

import models.Repository;

public class DeleteRepositoryAction implements AuditAction {

	protected Repository repository;

	public DeleteRepositoryAction(Repository repository) {
		this.repository = repository;
	}

	@Override
	public String getDescription() {
		return String.format("Deleted repository [%s][%s]",
								repository.getRepoType(),
								repository.getIdentifier());
	}

	@Override
	public String serialize() {
		return getDescription();
	}

}