package com.surevine.gateway.auditing.action.xml;

import models.Destination;

import com.surevine.gateway.auditing.action.UpdateDestinationAction;

public class XMLUpdateDestinationAction extends UpdateDestinationAction {

	public XMLUpdateDestinationAction(Destination originalDestination,
			Destination updatedDestination) {
		super(originalDestination, updatedDestination);
	}

	@Override
	public String serialize() {
		return "<Update/>";
	}

}
