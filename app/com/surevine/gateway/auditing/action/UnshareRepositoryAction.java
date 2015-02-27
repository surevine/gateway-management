package com.surevine.gateway.auditing.action;

import models.FederationConfiguration;

public class UnshareRepositoryAction implements AuditAction {

	protected FederationConfiguration config;

	public UnshareRepositoryAction(FederationConfiguration config) {
		this.config = config;
	}

	@Override
	public String getDescription() {
		return String.format("Unshared repository [%s][%s] with destination [%s][%s]",
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