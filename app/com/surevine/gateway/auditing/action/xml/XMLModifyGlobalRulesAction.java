package com.surevine.gateway.auditing.action.xml;

import com.surevine.gateway.auditing.action.ModifyGlobalRulesAction;

public class XMLModifyGlobalRulesAction extends ModifyGlobalRulesAction {

	public XMLModifyGlobalRulesAction(String ruleFile, String ruleFileContents) {
		super(ruleFile, ruleFileContents);
	}

	@Override
	public String serialize() {
		return "<Update/>";
	}

}
