package com.surevine.gateway.auditing.action.xml;

import models.Partner;

import com.surevine.gateway.auditing.action.CreatePartnerAction;

public class XMLCreatePartnerAction extends CreatePartnerAction {

	public XMLCreatePartnerAction(Partner partner) {
		super(partner);
	}

	@Override
	public String serialize() {

		StringBuilder xml = new StringBuilder();
		xml.append(String.format("<Description>%s</Description>", getDescription()) + System.getProperty("line.separator"));
		xml.append("<Create>");
		xml.append("<Outcome>");
		xml.append(String.format("<Data name=\"partnerName\" value=\"%s\" />", partner.getName()));
		xml.append(String.format("<Data name=\"partnerURL\" value=\"%s\" />", partner.getURL()));
		xml.append(String.format("<Data name=\"partnerSourceKey\" value=\"%s\" />", partner.getSourceKey()));
		xml.append("</Outcome>");
		xml.append("</Create>");

		return xml.toString();
	}

}
