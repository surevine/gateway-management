package com.surevine.gateway.auditing.action.xml;

import models.Repository;

import com.surevine.gateway.auditing.action.CreateRepositoryAction;

public class XMLCreateRepositoryAction extends CreateRepositoryAction {

	public XMLCreateRepositoryAction(Repository repository) {
		super(repository);
	}

	@Override
	public String serialize() {
		StringBuilder xml = new StringBuilder();
		xml.append(String.format("<Description>%s</Description>", getDescription()) + System.getProperty("line.separator"));
		xml.append("<Create>");
		xml.append("<Outcome>");
		xml.append(String.format("<Data name=\"repositoryType\" value=\"%s\" />", repository.repoType));
		xml.append(String.format("<Data name=\"repositoryIdentifier\" value=\"%s\" />", repository.identifier));
		xml.append("</Outcome>");
		xml.append("</Create>");

		return xml.toString();
	}

}
