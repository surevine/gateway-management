package com.surevine.gateway.auditing.action.xml;

import models.Destination;
import models.Project;

import com.surevine.gateway.auditing.action.UnshareRepositoryAction;

public class XMLUnshareRepositoryAction extends UnshareRepositoryAction {

	public XMLUnshareRepositoryAction(Project project, Destination destination) {
		super(project, destination);
	}

	@Override
	public String serialize() {
		return "<Unshare/>";
	}

}
