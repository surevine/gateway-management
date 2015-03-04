package com.surevine.gateway.auditing.action.xml;

import models.Partner;

import com.surevine.gateway.auditing.action.DeletePartnerAction;

public class XMLDeletePartnerAction extends DeletePartnerAction {

	public XMLDeletePartnerAction(Partner partner) {
		super(partner);
	}

	@Override
	public String serialize() {
		StringBuilder xml = new StringBuilder();
		xml.append(String.format("<Description>%s</Description>", getDescription()) + System.getProperty("line.separator"));
		xml.append("<Delete>");
		xml.append("<Outcome>");
		xml.append(String.format("<Data name=\"partnerName\" value=\"%s\" />", partner.getName()));
		xml.append(String.format("<Data name=\"partnerURL\" value=\"%s\" />", partner.getUrl()));
		xml.append(String.format("<Data name=\"partnerSourceKey\" value=\"%s\" />", partner.getSourceKey()));
		xml.append("</Outcome>");
		xml.append("</Delete>");

		return xml.toString();
	}

}
