package com.surevine.gateway.auditing.action.xml;

import org.apache.commons.lang3.StringEscapeUtils;

import models.Destination;

import com.surevine.gateway.auditing.action.ModifyDestinationRulesAction;

public class XMLModifyDestinationRulesAction extends
		ModifyDestinationRulesAction {

	public XMLModifyDestinationRulesAction(Destination destination,
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
