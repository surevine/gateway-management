package com.surevine.gateway.auditing;

import play.Logger;

/**
 * Basic AuditService implementation that logs audit messages
 * using Play framework logger.
 *
 * @author jonnyheavey
 *
 */
public class LogfileAuditServiceImpl implements AuditService {

	private static LogfileAuditServiceImpl _instance;

	private LogfileAuditServiceImpl() {

	}

	public static LogfileAuditServiceImpl getInstance() {
		if(_instance == null) {
			_instance = new LogfileAuditServiceImpl();
		}
		return _instance;
	}

	@Override
	public void audit(GatewayAction action, String username, String message) {
		// TODO include datetime
		Logger.info(String.format("[%s][%s] %s", action, username, message));
	}

}
