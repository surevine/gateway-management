package controllers;

import java.util.Calendar;

import play.mvc.Controller;

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
    	// TODO load implementation according to configuration
    	//this.auditService = LogfileAuditServiceImpl.getInstance();
    	this.auditService = XMLAuditServiceImpl.getInstance();
    }

    public void audit(GatewayAction action, String message) {
    	auditService.audit(action, Calendar.getInstance().getTime(), session().get("username"), message);
    }

    public void setAuditService(AuditService auditService) {
    	this.auditService = auditService;
    }

}
