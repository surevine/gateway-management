package com.surevine.gateway.auditing.action.xml;

import models.Repository;

import com.surevine.gateway.auditing.action.DeleteRepositoryAction;

public class XMLDeleteRepositoryAction extends DeleteRepositoryAction {

	public XMLDeleteRepositoryAction(Repository repository) {
		super(repository);
	}

	@Override
	public String serialize() {
		StringBuilder xml = new StringBuilder();
		xml.append(String.format("<Description>%s</Description>", getDescription()) + System.getProperty("line.separator"));
		xml.append("<Delete>");
		xml.append("<Outcome>");
		xml.append(String.format("<Data name=\"repositoryType\" value=\"%s\" />", repository.getRepoType()));
		xml.append(String.format("<Data name=\"repositoryIdentifier\" value=\"%s/%s\" />", repository.getIdentifier()));
		xml.append("</Outcome>");
		xml.append("</Delete>");

		return xml.toString();
	}

}
