package com.surevine.gateway.auditing.action.xml;

import models.Destination;
import models.Repository;

import com.surevine.gateway.auditing.action.UnshareRepositoryAction;

public class XMLUnshareRepositoryAction extends UnshareRepositoryAction {

	public XMLUnshareRepositoryAction(Repository repository, Destination destination) {
		super(repository, destination);
	}

	@Override
	public String serialize() {
		StringBuilder xml = new StringBuilder();
		xml.append(String.format("<Description>%s</Description>", getDescription()) + System.getProperty("line.separator"));
		xml.append("<Unknown>");
		xml.append("<Data name=\"action\" value=\"unshare\" />");
		xml.append(String.format("<Data name=\"destinationName\" value=\"%s\" />", destination.name));
		xml.append(String.format("<Data name=\"repositoryIdentifier\" value=\"%s\" />", repository.identifier));
		xml.append("</Unknown>");

		return xml.toString();
	}

}
