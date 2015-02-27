package com.surevine.gateway.auditing.action;

import models.FederationConfiguration;

public class UpdateFederationAction implements AuditAction {

	protected FederationConfiguration config;
	protected String updatedDirection;
	protected String action;

	public UpdateFederationAction(FederationConfiguration config, String updatedDirection, boolean federationEnabled) {
		this.config = config;
		this.updatedDirection = updatedDirection;

		this.action = "Disabled";
		if(federationEnabled) {
			this.action = "Enabled";
		}
	}

	@Override
	public String getDescription() {
		return String.format("%s %s federation between repository [%s][%s] and destination [%s][%s].",
				action,
				updatedDirection,
				config.repository.repoType,
				config.repository.identifier,
				config.partner.name,
				config.partner.url);
	}

	@Override
	public String serialize() {
		return getDescription();
	}

}
