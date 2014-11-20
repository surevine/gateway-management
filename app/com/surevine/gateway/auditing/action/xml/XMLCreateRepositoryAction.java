package com.surevine.gateway.auditing.action.xml;

import models.Project;

import com.surevine.gateway.auditing.action.CreateRepositoryAction;

public class XMLCreateRepositoryAction extends CreateRepositoryAction {

	public XMLCreateRepositoryAction(Project project) {
		super(project);
	}

	@Override
	public String serialize() {
		return "<Create/>";
	}

}
