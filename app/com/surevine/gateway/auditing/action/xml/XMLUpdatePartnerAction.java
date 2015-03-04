package com.surevine.gateway.auditing.action.xml;

import models.Partner;

import com.surevine.gateway.auditing.action.UpdatePartnerAction;

public class XMLUpdatePartnerAction extends UpdatePartnerAction {

	public XMLUpdatePartnerAction(Partner originalPartner,
			Partner updatedPartner) {
		super(originalPartner, updatedPartner);
	}

	@Override
	public String serialize() {
		StringBuilder xml = new StringBuilder();
		xml.append(String.format("<Description>%s</Description>", getDescription()) + System.getProperty("line.separator"));
		xml.append("<Update>");

		xml.append("<Before>");
		xml.append(String.format("<Data name=\"partnerName\" value=\"%s\" />", originalPartner.getName()));
		xml.append(String.format("<Data name=\"partnerURL\" value=\"%s\" />", originalPartner.getURL()));
		xml.append(String.format("<Data name=\"partnerSourceKey\" value=\"%s\" />", originalPartner.getSourceKey()));
		xml.append("</Before>");

		xml.append("<After>");
		xml.append(String.format("<Data name=\"partnerName\" value=\"%s\" />", updatedPartner.getName()));
		xml.append(String.format("<Data name=\"partnerURL\" value=\"%s\" />", updatedPartner.getURL()));
		xml.append(String.format("<Data name=\"partnerSourceKey\" value=\"%s\" />", updatedPartner.getSourceKey()));
		xml.append("</After>");

		xml.append("</Update>");

		return xml.toString();
	}

}
