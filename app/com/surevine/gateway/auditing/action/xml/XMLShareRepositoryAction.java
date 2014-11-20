package com.surevine.gateway.auditing.action.xml;

import models.Destination;
import models.Project;

import com.surevine.gateway.auditing.action.ShareRepositoryAction;

public class XMLShareRepositoryAction extends ShareRepositoryAction {

	public XMLShareRepositoryAction(Project project, Destination destination) {
		super(project, destination);
	}

	@Override
	public String serialize() {
		return "<Share/>";
	}

}
