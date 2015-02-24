package com.surevine.gateway.auditing.action.xml;

import models.Destination;
import models.Repository;

import com.surevine.gateway.auditing.action.ToggleFederationAction;

public class XMLToggleFederationAction extends ToggleFederationAction {

	public XMLToggleFederationAction(Repository repository,
			Destination destination, String direction, boolean enabled) {
		super(repository, destination, direction, enabled);
	}

	@Override
	public String serialize() {

		StringBuilder xml = new StringBuilder();
		xml.append(String.format("<Description>%s</Description>", getDescription()) + System.getProperty("line.separator"));
		xml.append("<Unknown>");
		xml.append(String.format("<Data name=\"action\" value=\"%s federation\" />", action));
		xml.append(String.format("<Data name=\"direction\" value=\"%s\" />", direction));
		xml.append(String.format("<Data name=\"destinationName\" value=\"%s\" />", destination.name));
		xml.append(String.format("<Data name=\"destinationURL\" value=\"%s\" />", destination.url));
		xml.append(String.format("<Data name=\"repositoryType\" value=\"%s\" />", repository.repoType));
		xml.append(String.format("<Data name=\"repositoryIdentifier\" value=\"%s\" />", repository.identifier));
		xml.append("</Unknown>");

		return xml.toString();
	}

}
