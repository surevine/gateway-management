package com.surevine.gateway.auditing.action;

import models.Destination;
import models.Project;

public class UnshareRepositoryAction implements AuditAction {

	protected Project project;
	protected Destination destination;

	public UnshareRepositoryAction(Project project, Destination destination) {
		this.project = project;
		this.destination = destination;
	}

	@Override
	public String getDescription() {
		return String.format("Unshared repository %s(%s/%s) with destination %s(%s)",
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