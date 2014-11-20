package com.surevine.gateway.auditing.action;

import models.Project;

public class CreateRepositoryAction implements AuditAction {

	private Project project;

	public CreateRepositoryAction(Project project) {
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
