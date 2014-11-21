package controllers;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import play.mvc.Controller;

import com.surevine.gateway.auditing.AuditEvent;
import com.surevine.gateway.auditing.AuditService;
import com.surevine.gateway.auditing.action.AuditAction;
import com.surevine.gateway.auditing.action.AuditActionFactory;

/**
 * Play controller with audit service available
 *
 * @author jonnyheavey
 *
 */
@org.springframework.stereotype.Controller
public class AuditedController extends Controller {

	@Autowired
	@Qualifier("auditService")
    private AuditService auditService;

	@Autowired
	@Qualifier("auditActionFactory")
    protected AuditActionFactory auditActionFactory;

    public void audit(AuditAction action) {
    	AuditEvent event = new AuditEvent(action, Calendar.getInstance().getTime(), session().get("username"));
    	auditService.audit(event);
    }

    public void setAuditService(AuditService auditService) {
    	this.auditService = auditService;
    }

    public void setAuditActionFactory(AuditActionFactory auditActionFactory) {
    	this.auditActionFactory = auditActionFactory;
    }

}
