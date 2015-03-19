package com.surevine.gateway.auditing;

import java.text.SimpleDateFormat;

import play.Logger;

/**
 * Basic AuditService implementation that logs audit messages
 * using Play framework logger.
 *
 * @author jonnyheavey
 *
 */
@org.springframework.stereotype.Service
public class LogfileAuditServiceImpl implements AuditService {

	private static LogfileAuditServiceImpl _instance;

	private SimpleDateFormat dateFormat;

	private LogfileAuditServiceImpl() {
		dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	}

	public static LogfileAuditServiceImpl getInstance() {
		if(_instance == null) {
			_instance = new LogfileAuditServiceImpl();
		}
		return _instance;
	}

	@Override
	public void audit(AuditEvent event) {
		Logger.info(String.format("[%s][%s][%s]",
				dateFormat.format(event.getDatetime()),
				event.getUsername(),
				event.getAction().serialize()));
	}

}
