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
		xml.append(String.format("<Data name=\"repositoryIdentifier\" value=\"%s\" />", originalRepository.getIdentifier()));
		xml.append(String.format("<Data name=\"repositoryType\" value=\"%s\" />", originalRepository.getRepoType()));
		xml.append("</Before>");

		xml.append("<After>");
		xml.append(String.format("<Data name=\"repositoryIdentifier\" value=\"%s\" />", updatedRepository.getIdentifier()));
		xml.append(String.format("<Data name=\"repositoryType\" value=\"%s\" />", updatedRepository.getRepoType()));
		xml.append("</After>");

		xml.append("</Update>");

		return xml.toString();
	}

}
