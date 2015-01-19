package com.surevine.gateway.auditing.action;

import models.OutboundProject;

public class UpdateRepositoryAction implements AuditAction {

	protected OutboundProject originalProject;
	protected OutboundProject updatedProject;

	public UpdateRepositoryAction(OutboundProject originalProject, OutboundProject updatedProject) {
		this.originalProject = originalProject;
		this.updatedProject = updatedProject;
	}

	@Override
	public String getDescription() {
		return String.format("Updated repository %s(%s/%s)",
								updatedProject.displayName,
								updatedProject.projectKey,
								updatedProject.repositorySlug);
	}

	@Override
	public String serialize() {
		return getDescription();
	}

}
