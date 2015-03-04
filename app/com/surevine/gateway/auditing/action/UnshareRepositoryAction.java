package com.surevine.gateway.auditing.action;

import models.FederationConfiguration;

public class UnshareRepositoryAction implements AuditAction {

	protected FederationConfiguration config;

	public UnshareRepositoryAction(FederationConfiguration config) {
		this.config = config;
	}

	@Override
	public String getDescription() {
		return String.format("Unshared repository [%s][%s] with partner [%s][%s]",
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