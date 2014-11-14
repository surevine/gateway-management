package com.surevine.gateway.auditing;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

	private SimpleDateFormat dateFormat;

	private LogfileAuditServiceImpl() {
		dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	}

	public static LogfileAuditServiceImpl getInstance() {
		if(_instance == null) {
			_instance = new LogfileAuditServiceImpl();
		}
		return _instance;
	}

	@Override
	public void audit(GatewayAction action, Date datetime, String username, String message) {
		Logger.info(String.format("[%s][%s][%s] %s", dateFormat.format(datetime), action, username, message));
	}

}
