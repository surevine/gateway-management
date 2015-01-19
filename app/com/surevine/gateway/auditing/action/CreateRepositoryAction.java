package com.surevine.gateway.auditing.action;

import models.OutboundProject;

public class CreateRepositoryAction implements AuditAction {

	protected OutboundProject project;

	public CreateRepositoryAction(OutboundProject project) {
		this.project = project;
	}

	@Override
	public String getDescription() {
		return String.format("Created repository %s(%s/%s)",
								project.displayName,
								project.projectKey,
								project.repositorySlug);
	}

	@Override
	public String serialize() {
		return getDescription();
	}

}
