package controllers;

import java.util.Date;

import com.surevine.gateway.auditing.Audit;
import com.surevine.gateway.auditing.AuditEvent;
import com.surevine.gateway.auditing.action.AuditAction;

import play.mvc.Controller;

public class AuditedController extends Controller {

	/**
	 * Use configured audit mode to record action by user
	 * @param action action to be recorded
	 */
	public void audit(AuditAction action) {
		AuditEvent event = new AuditEvent(action, new Date(), session().get("username"));
		Audit.audit(event);
	}

}
