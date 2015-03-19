package com.surevine.gateway.auditing.action.xml;

import org.apache.commons.lang3.StringEscapeUtils;

import com.surevine.gateway.auditing.action.ModifyGlobalRulesAction;

public class XMLModifyGlobalRulesAction extends ModifyGlobalRulesAction {

	public XMLModifyGlobalRulesAction(String ruleFile, String ruleFileContents) {
		super(ruleFile, ruleFileContents);
	}

	@Override
	public String serialize() {
		StringBuilder xml = new StringBuilder();
		xml.append(String.format("<Description>%s</Description>", getDescription()) + System.getProperty("line.separator"));
		xml.append("<Update>");

		xml.append("<After>");
		xml.append(String.format("<Data name=\"globalRuleFile\" value=\"%s\" />", ruleFile));
		xml.append(String.format("<Data name=\"globalRuleFileContent\" value=\"%s\" />", StringEscapeUtils.escapeXml(ruleFileContents)));
		xml.append("</After>");

		xml.append("</Update>");

		return xml.toString();
	}

}
