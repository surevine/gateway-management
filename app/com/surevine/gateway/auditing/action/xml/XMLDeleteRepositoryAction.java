package com.surevine.gateway.auditing.action.xml;

import models.Project;

import com.surevine.gateway.auditing.action.DeleteRepositoryAction;

public class XMLDeleteRepositoryAction extends DeleteRepositoryAction {

	public XMLDeleteRepositoryAction(Project project) {
		super(project);
	}

	@Override
	public String serialize() {
		StringBuilder xml = new StringBuilder();
		xml.append(String.format("<Description>%s</Description>", getDescription()) + System.getProperty("line.separator"));
		xml.append("<Delete>");
		xml.append("<Outcome>");
		xml.append(String.format("<Data name=\"destinationName\" value=\"%s\" />", project.displayName));
		xml.append(String.format("<Data name=\"destinationURL\" value=\"%s/%s\" />", project.projectKey, project.repositorySlug));
		xml.append("</Outcome>");
		xml.append("</Delete>");

		return xml.toString();
	}

}
