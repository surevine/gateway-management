package com.surevine.gateway.auditing.action.xml;

import models.Repository;

import com.surevine.gateway.auditing.action.UpdateRepositoryAction;

public class XMLUpdateRepositoryAction extends UpdateRepositoryAction {

	public XMLUpdateRepositoryAction(Repository originalRepository,
			Repository updatedRepository) {
		super(originalRepository, updatedRepository);
	}

	@Override
	public String serialize() {
		StringBuilder xml = new StringBuilder();
		xml.append(String.format("<Description>%s</Description>", getDescription()) + System.getProperty("line.separator"));
		xml.append("<Update>");

		xml.append("<Before>");
		xml.append(String.format("<Data name=\"repositoryIdentifier\" value=\"%s\" />", originalRepository.identifier));
		xml.append(String.format("<Data name=\"repositoryType\" value=\"%s\" />", originalRepository.repoType));
		xml.append("</Before>");

		xml.append("<After>");
		xml.append(String.format("<Data name=\"repositoryIdentifier\" value=\"%s\" />", updatedRepository.identifier));
		xml.append(String.format("<Data name=\"repositoryType\" value=\"%s\" />", updatedRepository.repoType));
		xml.append("</After>");

		xml.append("</Update>");

		return xml.toString();
	}

}
