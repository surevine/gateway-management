package com.surevine.gateway.auditing.action;

import models.FederationConfiguration;

public class ResendRepositoryAction implements AuditAction {

	protected FederationConfiguration config;

	public ResendRepositoryAction(FederationConfiguration config) {
		this.config = config;
	}

	@Override
	public String getDescription() {
		return String.format("Resent repository [%s][%s] to gateway for export to partner [%s][%s]",
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