package com.surevine.gateway.auditing.action.xml;

import models.Project;

import com.surevine.gateway.auditing.action.CreateRepositoryAction;

public class XMLCreateRepositoryAction extends CreateRepositoryAction {

	public XMLCreateRepositoryAction(Project project) {
		super(project);
	}

	@Override
	public String serialize() {
		StringBuilder xml = new StringBuilder();
		xml.append(String.format("<Description>%s</Description>", getDescription()) + System.getProperty("line.separator"));
		xml.append("<Create>");
		xml.append("<Outcome>");
		xml.append(String.format("<Data name=\"repositoryName\" value=\"%s\" />", project.displayName));
		xml.append(String.format("<Data name=\"repositorySlug\" value=\"%s/%s\" />", project.projectKey, project.repositorySlug ));
		xml.append("</Outcome>");
		xml.append("</Create>");

		return xml.toString();
	}

}
