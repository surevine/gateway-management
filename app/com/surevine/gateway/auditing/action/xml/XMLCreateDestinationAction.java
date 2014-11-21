package com.surevine.gateway.auditing.action.xml;

import models.Destination;

import com.surevine.gateway.auditing.action.CreateDestinationAction;

public class XMLCreateDestinationAction extends CreateDestinationAction {

	public XMLCreateDestinationAction(Destination destination) {
		super(destination);
	}

	@Override
	public String serialize() {

		StringBuilder xml = new StringBuilder();
		xml.append(String.format("<Description>%s</Description>", getDescription()) + System.getProperty("line.separator"));
		xml.append("<Create>");
		xml.append("<Outcome>");
		xml.append(String.format("<Data name=\"destinationName\" value=\"%s\" />", destination.name));
		xml.append(String.format("<Data name=\"destinationURL\" value=\"%s\" />", destination.url));
		xml.append("</Outcome>");
		xml.append("</Create>");

		return xml.toString();
	}

}
