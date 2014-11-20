package com.surevine.gateway.auditing.action.xml;

import models.Destination;

import com.surevine.gateway.auditing.action.DeleteDestinationAction;

public class XMLDeleteDestinationAction extends DeleteDestinationAction {

	public XMLDeleteDestinationAction(Destination destination) {
		super(destination);
	}

	@Override
	public String serialize() {
		return "<Delete/>";
	}

}
