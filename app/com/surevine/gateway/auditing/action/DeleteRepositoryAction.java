package com.surevine.gateway.auditing.action;

import models.OutboundProject;

public class DeleteRepositoryAction implements AuditAction {

	protected OutboundProject project;

	public DeleteRepositoryAction(OutboundProject project) {
		this.project = project;
	}

	@Override
	public String getDescription() {
		return String.format("Deleted repository %s(%s/%s)",
								project.displayName,
								project.projectKey,
								project.repositorySlug);
	}

	@Override
	public String serialize() {
		return getDescription();
	}

}