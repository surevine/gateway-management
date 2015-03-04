package com.surevine.gateway.auditing.action;

import models.FederationConfiguration;

public class ShareRepositoryAction implements AuditAction {

	protected FederationConfiguration config;

	public ShareRepositoryAction(FederationConfiguration config) {
		this.config = config;
	}

	@Override
	public String getDescription() {
		return String.format("Shared repository [%s][%s] with partner [%s][%s]. Inbound federation enabled: [%s]. Outbound federation enabled: [%s].",
								config.getRepository().getRepoType(),
								config.getRepository().getIdentifier(),
								config.getPartner().getName(),
								config.getPartner().getURL(),
								config.inboundEnabled,
								config.outboundEnabled);
	}

	@Override
	public String serialize() {
		return getDescription();
	}

}