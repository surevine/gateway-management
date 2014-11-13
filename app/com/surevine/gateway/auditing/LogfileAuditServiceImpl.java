package com.surevine.gateway.auditing;

import javax.inject.Singleton;

import play.Logger;

/**
 * Basic AuditService implementation that logs audit messages
 * using Play framework logger.
 *
 * @author jonnyheavey
 *
 */
@Singleton
public class LogfileAuditServiceImpl implements AuditService {

	@Override
	public void audit(GatewayAction action, String username, String message) {
		Logger.info(String.format("[%s][%s] %s", action, username, message));
	}

}
