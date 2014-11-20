package com.surevine.gateway.auditing.action;

import models.Destination;
import models.Project;

public class ResendRepositoryAction implements AuditAction {

	private Project project;
	private Destination destination;

	public ResendRepositoryAction(Project project, Destination destination) {
		this.project = project;
		this.destination = destination;
	}

	@Override
	public String getDescription() {
		return String.format("Resent repository %s(%s/%s) resent to destination %s(%s)",
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