package com.surevine.gateway.auditing.action;

import models.Project;

public class UpdateRepositoryAction implements AuditAction {

	private Project originalProject;
	private Project updatedProject;

	public UpdateRepositoryAction(Project originalProject, Project updatedProject) {
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
