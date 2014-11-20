package com.surevine.gateway.auditing.action.xml;

import models.Destination;

import com.surevine.gateway.auditing.action.CreateDestinationAction;


public class XMLCreateDestinationAction extends CreateDestinationAction {

	public XMLCreateDestinationAction(Destination destination) {
		super(destination);
	}

	@Override
	public String serialize() {
		return "<xml></xml>";
		// TODO produce proper expected XML
	}

}
