package com.surevine.gateway.auditing.action;

import models.Repository;

public class CreateRepositoryAction implements AuditAction {

	protected Repository repository;

	public CreateRepositoryAction(Repository repository) {
		this.repository = repository;
	}

	@Override
	public String getDescription() {
		return String.format("Created repository [%s][%s]",
								repository.getRepoType(),
								repository.getIdentifier());
	}

	@Override
	public String serialize() {
		return getDescription();
	}

}
