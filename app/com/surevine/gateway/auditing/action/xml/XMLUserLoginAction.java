package com.surevine.gateway.auditing.action.xml;

import com.surevine.gateway.auditing.action.UserLoginAction;

public class XMLUserLoginAction extends UserLoginAction {

	public XMLUserLoginAction(String username) {
		super(username);
	}

	@Override
	public String serialize() {
		return "<Login/>";
	}

}
