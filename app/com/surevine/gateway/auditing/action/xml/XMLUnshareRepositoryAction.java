package com.surevine.gateway.auditing.action.xml;

import models.Destination;
import models.OutboundProject;

import com.surevine.gateway.auditing.action.UnshareRepositoryAction;

public class XMLUnshareRepositoryAction extends UnshareRepositoryAction {

	public XMLUnshareRepositoryAction(OutboundProject project, Destination destination) {
		super(project, destination);
	}

	@Override
	public String serialize() {
		StringBuilder xml = new StringBuilder();
		xml.append(String.format("<Description>%s</Description>", getDescription()) + System.getProperty("line.separator"));
		xml.append("<Unknown>");
		xml.append("<Data name=\"action\" value=\"unshare\" />");
		xml.append(String.format("<Data name=\"destinationName\" value=\"%s\" />", destination.name));
		xml.append(String.format("<Data name=\"repositoryName\" value=\"%s\" />", project.displayName));
		xml.append("</Unknown>");

		return xml.toString();
	}

}
