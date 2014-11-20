package com.surevine.gateway.auditing.action.xml;

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
		return "<Update/>";
	}

}
