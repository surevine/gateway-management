package com.surevine.gateway.auditing.action;

import models.Destination;
import models.OutboundProject;

public class ShareRepositoryAction implements AuditAction {

	protected OutboundProject project;
	protected Destination destination;

	public ShareRepositoryAction(OutboundProject project, Destination destination) {
		this.project = project;
		this.destination = destination;
	}

	@Override
	public String getDescription() {
		return String.format("Shared repository %s(%s/%s) with destination %s(%s). Repository sent to gateway for export.",
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