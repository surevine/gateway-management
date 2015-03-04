package com.surevine.gateway.auditing.action.xml;

import models.FederationConfiguration;

import com.surevine.gateway.auditing.action.UpdateFederationAction;

public class XMLUpdateFederationAction extends UpdateFederationAction {

	public XMLUpdateFederationAction(
			FederationConfiguration config, String updatedDirection, boolean federationEnabled) {
		super(config, updatedDirection, federationEnabled);
	}

	@Override
	public String serialize() {

		StringBuilder xml = new StringBuilder();
		xml.append(String.format("<Description>%s</Description>", getDescription()) + System.getProperty("line.separator"));
		xml.append("<Unknown>");
		xml.append(String.format("<Data name=\"action\" value=\"%s federation\" />", action));
		xml.append(String.format("<Data name=\"direction\" value=\"%s\" />", updatedDirection));
		xml.append(String.format("<Data name=\"partnerName\" value=\"%s\" />", config.getPartner().getName()));
		xml.append(String.format("<Data name=\"partnerURL\" value=\"%s\" />", config.getPartner().getUrl()));
		xml.append(String.format("<Data name=\"repositoryType\" value=\"%s\" />", config.getRepository().getRepoType()));
		xml.append(String.format("<Data name=\"repositoryIdentifier\" value=\"%s\" />", config.getRepository().getIdentifier()));
		xml.append("</Unknown>");

		return xml.toString();
	}

}
