package com.surevine.gateway.auditing.action.xml;

import models.FederationConfiguration;

import com.surevine.gateway.auditing.action.ShareRepositoryAction;

public class XMLShareRepositoryAction extends ShareRepositoryAction {

	public XMLShareRepositoryAction(FederationConfiguration config) {
		super(config);
	}

	@Override
	public String serialize() {
		StringBuilder xml = new StringBuilder();
		xml.append(String.format("<Description>%s</Description>", getDescription()) + System.getProperty("line.separator"));
		xml.append("<Unknown>");
		xml.append("<Data name=\"action\" value=\"share\" />");
		xml.append(String.format("<Data name=\"destinationName\" value=\"%s\" />", config.partner.name));
		xml.append(String.format("<Data name=\"destinationURL\" value=\"%s\" />", config.partner.url));
		xml.append(String.format("<Data name=\"repositoryType\" value=\"%s\" />", config.repository.repoType));
		xml.append(String.format("<Data name=\"repositoryIdentifier\" value=\"%s\" />", config.repository.identifier));
		xml.append(String.format("<Data name=\"inboundFederationEnabled\" value=\"%s\" />", config.inboundEnabled));
		xml.append(String.format("<Data name=\"outboundFederationEnabled\" value=\"%s\" />", config.outboundEnabled));
		xml.append("</Unknown>");

		return xml.toString();
	}

}
