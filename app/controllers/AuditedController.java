package controllers;

import java.util.Calendar;

import play.mvc.Controller;

import com.surevine.gateway.auditing.AuditEvent;
import com.surevine.gateway.auditing.AuditService;
import com.surevine.gateway.auditing.GatewayAction;
import com.surevine.gateway.auditing.LogfileAuditServiceImpl;
import com.surevine.gateway.auditing.XMLAuditServiceImpl;

/**
 * Play controller with audit service available
 *
 * @author jonnyheavey
 *
 */
public class AuditedController extends Controller {

    protected AuditService auditService;

    public AuditedController() {
    	// TODO load implementation according to configuration (spring)
    	//this.auditService = LogfileAuditServiceImpl.getInstance();
    	this.auditService = XMLAuditServiceImpl.getInstance();
    }

    public void audit(GatewayAction action, String message) {
    	AuditEvent event = new AuditEvent(action, Calendar.getInstance().getTime(), session().get("username"), message);
    	auditService.audit(event);
    }

    public void setAuditService(AuditService auditService) {
    	this.auditService = auditService;
    }

}
