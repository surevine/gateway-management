package com.surevine.gateway.auditing.action.xml;

import com.surevine.gateway.auditing.action.UserLoginAction;

public class XMLUserLoginAction extends UserLoginAction {

	public XMLUserLoginAction(String username) {
		super(username);
	}

	@Override
	public String serialize() {
		StringBuilder xml = new StringBuilder();
		xml.append(String.format("<Description>%s</Description>", getDescription()) + System.getProperty("line.separator"));
		xml.append("<Authenticate>");
		xml.append("<Action>Logon</Action>");
		xml.append("<User>");
		xml.append(String.format("<EmailAddress>%s</EmailAddress>", username));
		xml.append("</User>");
		xml.append("</Authenticate>");

		return xml.toString();
	}

}
