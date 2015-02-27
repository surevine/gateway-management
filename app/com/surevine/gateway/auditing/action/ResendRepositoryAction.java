package com.surevine.gateway.auditing.action;

import models.FederationConfiguration;

public class ResendRepositoryAction implements AuditAction {

	protected FederationConfiguration config;

	public ResendRepositoryAction(FederationConfiguration config) {
		this.config = config;
	}

	@Override
	public String getDescription() {
		return String.format("Resent repository [%s][%s] to gateway for export to destination [%s][%s]",
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