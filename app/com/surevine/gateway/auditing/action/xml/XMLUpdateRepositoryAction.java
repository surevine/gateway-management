package com.surevine.gateway.auditing.action.xml;

import models.OutboundProject;

import com.surevine.gateway.auditing.action.UpdateRepositoryAction;

public class XMLUpdateRepositoryAction extends UpdateRepositoryAction {

	public XMLUpdateRepositoryAction(OutboundProject originalProject,
			OutboundProject updatedProject) {
		super(originalProject, updatedProject);
	}

	@Override
	public String serialize() {
		StringBuilder xml = new StringBuilder();
		xml.append(String.format("<Description>%s</Description>", getDescription()) + System.getProperty("line.separator"));
		xml.append("<Update>");

		xml.append("<Before>");
		xml.append(String.format("<Data name=\"respoitoryName\" value=\"%s\" />", originalProject.displayName));
		xml.append(String.format("<Data name=\"repositoryProjectKey\" value=\"%s\" />", originalProject.projectKey));
		xml.append(String.format("<Data name=\"repositoryRepoSlug\" value=\"%s\" />", originalProject.repositorySlug));
		xml.append("</Before>");

		xml.append("<After>");
		xml.append(String.format("<Data name=\"destinationName\" value=\"%s\" />", updatedProject.displayName));
		xml.append(String.format("<Data name=\"repositoryProjectKey\" value=\"%s\" />", updatedProject.projectKey));
		xml.append(String.format("<Data name=\"repositoryRepoSlug\" value=\"%s\" />", updatedProject.repositorySlug));
		xml.append("</After>");

		xml.append("</Update>");

		return xml.toString();
	}

}
