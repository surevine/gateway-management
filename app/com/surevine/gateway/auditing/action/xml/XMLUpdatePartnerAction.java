package com.surevine.gateway.auditing.action.xml;

import models.Partner;

import com.surevine.gateway.auditing.action.UpdatePartnerAction;

public class XMLUpdatePartnerAction extends UpdatePartnerAction {

	public XMLUpdatePartnerAction(Partner originalDestination,
			Partner updatedDestination) {
		super(originalDestination, updatedDestination);
	}

	@Override
	public String serialize() {
		StringBuilder xml = new StringBuilder();
		xml.append(String.format("<Description>%s</Description>", getDescription()) + System.getProperty("line.separator"));
		xml.append("<Update>");

		xml.append("<Before>");
		xml.append(String.format("<Data name=\"destinationName\" value=\"%s\" />", originalDestination.name));
		xml.append(String.format("<Data name=\"destinationURL\" value=\"%s\" />", originalDestination.url));
		xml.append(String.format("<Data name=\"destinationSourceKey\" value=\"%s\" />", originalDestination.sourceKey));
		xml.append("</Before>");

		xml.append("<After>");
		xml.append(String.format("<Data name=\"destinationName\" value=\"%s\" />", updatedDestination.name));
		xml.append(String.format("<Data name=\"destinationURL\" value=\"%s\" />", updatedDestination.url));
		xml.append(String.format("<Data name=\"destinationSourceKey\" value=\"%s\" />", updatedDestination.sourceKey));
		xml.append("</After>");

		xml.append("</Update>");

		return xml.toString();
	}

}