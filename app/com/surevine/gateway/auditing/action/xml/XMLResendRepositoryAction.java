package com.surevine.gateway.auditing.action.xml;

import models.Destination;
import models.Repository;

import com.surevine.gateway.auditing.action.ResendRepositoryAction;

public class XMLResendRepositoryAction extends ResendRepositoryAction {

	public XMLResendRepositoryAction(Repository repository, Destination destination) {
		super(repository, destination);
	}

	@Override
	public String serialize() {
		StringBuilder xml = new StringBuilder();
		xml.append(String.format("<Description>%s</Description>", getDescription()) + System.getProperty("line.separator"));
		xml.append("<Export>");
		xml.append("<Data name=\"action\" value=\"resend\" />");
		xml.append(String.format("<Data name=\"destinationName\" value=\"%s\" />", destination.name));
		xml.append(String.format("<Data name=\"destinationURL\" value=\"%s\" />", destination.url));
		xml.append(String.format("<Data name=\"repositoryIdentifier\" value=\"%s\" />", repository.repoType));
		xml.append(String.format("<Data name=\"repositoryIdentifier\" value=\"%s\" />", repository.identifier));
		xml.append("</Export>");

		return xml.toString();
	}

}
