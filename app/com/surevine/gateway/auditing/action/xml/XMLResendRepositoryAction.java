package com.surevine.gateway.auditing.action.xml;

import models.Destination;
import models.Project;

import com.surevine.gateway.auditing.action.ResendRepositoryAction;

public class XMLResendRepositoryAction extends ResendRepositoryAction {

	public XMLResendRepositoryAction(Project project, Destination destination) {
		super(project, destination);
	}

	@Override
	public String serialize() {
		return "<Export/>";
	}

}
