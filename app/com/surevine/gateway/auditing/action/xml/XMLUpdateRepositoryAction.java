package com.surevine.gateway.auditing.action.xml;

import models.Project;

import com.surevine.gateway.auditing.action.UpdateRepositoryAction;

public class XMLUpdateRepositoryAction extends UpdateRepositoryAction {

	public XMLUpdateRepositoryAction(Project originalProject,
			Project updatedProject) {
		super(originalProject, updatedProject);
	}

	@Override
	public String serialize() {
		return "<Update/>";
	}

}
