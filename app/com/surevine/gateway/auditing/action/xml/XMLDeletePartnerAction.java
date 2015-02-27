package com.surevine.gateway.auditing.action.xml;

import models.Partner;

import com.surevine.gateway.auditing.action.DeletePartnerAction;

public class XMLDeletePartnerAction extends DeletePartnerAction {

	public XMLDeletePartnerAction(Partner destination) {
		super(destination);
	}

	@Override
	public String serialize() {
		StringBuilder xml = new StringBuilder();
		xml.append(String.format("<Description>%s</Description>", getDescription()) + System.getProperty("line.separator"));
		xml.append("<Delete>");
		xml.append("<Outcome>");
		xml.append(String.format("<Data name=\"destinationName\" value=\"%s\" />", destination.name));
		xml.append(String.format("<Data name=\"destinationURL\" value=\"%s\" />", destination.url));
		xml.append(String.format("<Data name=\"destinationSourceKey\" value=\"%s\" />", destination.sourceKey));
		xml.append("</Outcome>");
		xml.append("</Delete>");

		return xml.toString();
	}

}
