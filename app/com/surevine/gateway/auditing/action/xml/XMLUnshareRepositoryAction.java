package com.surevine.gateway.auditing.action.xml;

import models.FederationConfiguration;

import com.surevine.gateway.auditing.action.UnshareRepositoryAction;

public class XMLUnshareRepositoryAction extends UnshareRepositoryAction {

	public XMLUnshareRepositoryAction(FederationConfiguration config) {
		super(config);
	}

	@Override
	public String serialize() {
		StringBuilder xml = new StringBuilder();
		xml.append(String.format("<Description>%s</Description>", getDescription()) + System.getProperty("line.separator"));
		xml.append("<Unknown>");
		xml.append("<Data name=\"action\" value=\"unshare\" />");
		xml.append(String.format("<Data name=\"partnerName\" value=\"%s\" />", config.getPartner().getName()));
		xml.append(String.format("<Data name=\"partnerURL\" value=\"%s\" />", config.getPartner().getURL()));
		xml.append(String.format("<Data name=\"repositoryType\" value=\"%s\" />", config.getRepository().getRepoType()));
		xml.append(String.format("<Data name=\"repositoryIdentifier\" value=\"%s\" />", config.getRepository().getIdentifier()));
		xml.append("</Unknown>");

		return xml.toString();
	}

}
