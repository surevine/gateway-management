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
		StringBuilder xml = new StringBuilder();
		xml.append(String.format("<Description>%s</Description>", getDescription()) + System.getProperty("line.separator"));
		xml.append("<Export>");
		xml.append("<Data name=\"action\" value=\"resend\" />");
		xml.append(String.format("<Data name=\"destinationName\" value=\"%s\" />", destination.name));
		xml.append(String.format("<Data name=\"repositoryName\" value=\"%s\" />", project.displayName));
		xml.append("</Export>");

		return xml.toString();
	}

}
