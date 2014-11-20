package controllers;

import java.util.Calendar;

import play.mvc.Controller;

import com.surevine.gateway.auditing.AuditEvent;
import com.surevine.gateway.auditing.AuditService;
import com.surevine.gateway.auditing.LogfileAuditServiceImpl;
import com.surevine.gateway.auditing.XMLAuditServiceImpl;
import com.surevine.gateway.auditing.action.AuditAction;
import com.surevine.gateway.auditing.action.AuditActionFactory;
import com.surevine.gateway.auditing.action.LogfileAuditActionFactory;
import com.surevine.gateway.auditing.action.xml.XMLAuditActionFactory;

/**
 * Play controller with audit service available
 *
 * @author jonnyheavey
 *
 */
public class AuditedController extends Controller {

    private AuditService auditService;
    protected AuditActionFactory auditActionFactory;

    public AuditedController() {
    	// TODO load implementation according to configuration (spring)
//    	this.auditService = XMLAuditServiceImpl.getInstance();
//    	this.auditActionFactory = new XMLAuditActionFactory();
    	this.auditService = LogfileAuditServiceImpl.getInstance();
    	this.auditActionFactory = new LogfileAuditActionFactory();
    }

    public void audit(AuditAction action) {
    	AuditEvent event = new AuditEvent(action, Calendar.getInstance().getTime(), session().get("username"));
    	auditService.audit(event);
    }

    public void setAuditService(AuditService auditService) {
    	this.auditService = auditService;
    }

}
