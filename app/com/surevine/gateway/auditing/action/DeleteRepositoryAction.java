package com.surevine.gateway.auditing.action;

import models.Project;

public class DeleteRepositoryAction implements AuditAction {

	private Project project;

	public DeleteRepositoryAction(Project project) {
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