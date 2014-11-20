package com.surevine.gateway.auditing.action;

import models.Destination;
import models.Project;

public class ShareRepositoryAction implements AuditAction {

	private Project project;
	private Destination destination;

	public ShareRepositoryAction(Project project, Destination destination) {
		this.project = project;
		this.destination = destination;
	}

	@Override
	public String getDescription() {
		return String.format("Shared repository %s(%s/%s) with destination %s(%s)",
								project.displayName,
								project.projectKey,
								project.repositorySlug,
								destination.name,
								destination.url);
	}

	@Override
	public String serialize() {
		return getDescription();
	}

}