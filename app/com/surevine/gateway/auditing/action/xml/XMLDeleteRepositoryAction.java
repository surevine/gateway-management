package com.surevine.gateway.auditing.action.xml;

import models.Project;

import com.surevine.gateway.auditing.action.DeleteRepositoryAction;

public class XMLDeleteRepositoryAction extends DeleteRepositoryAction {

	public XMLDeleteRepositoryAction(Project project) {
		super(project);
	}

	@Override
	public String serialize() {
		return "<Delete/>";
	}

}
