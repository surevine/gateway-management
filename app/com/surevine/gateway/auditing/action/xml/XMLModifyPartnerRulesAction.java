package com.surevine.gateway.auditing.action.xml;

import org.apache.commons.lang3.StringEscapeUtils;

import models.Partner;

import com.surevine.gateway.auditing.action.ModifyPartnerRulesAction;

public class XMLModifyPartnerRulesAction extends
		ModifyPartnerRulesAction {

	public XMLModifyPartnerRulesAction(Partner destination,
			String ruleFileContents) {
		super(destination, ruleFileContents);
	}

	@Override
	public String serialize() {
		StringBuilder xml = new StringBuilder();
		xml.append(String.format("<Description>%s</Description>", getDescription()) + System.getProperty("line.separator"));
		xml.append("<Update>");

		xml.append("<After>");
		xml.append(String.format("<Data name=\"destinationName\" value=\"%s\" />", destination.name));
		xml.append(String.format("<Data name=\"destinationExportRules\" value=\"%s\" />", StringEscapeUtils.escapeXml(ruleFileContents)));
		xml.append("</After>");

		xml.append("</Update>");

		return xml.toString();
	}

}
