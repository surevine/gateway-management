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
				config.getRepository().getRepoType(),
				config.getRepository().getIdentifier(),
				config.getPartner().getName(),
				config.getPartner().getURL());
	}

	@Override
	public String serialize() {
		return getDescription();
	}

}
