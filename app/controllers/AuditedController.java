package controllers;

import play.mvc.Controller;

import com.surevine.gateway.auditing.AuditService;
import com.surevine.gateway.auditing.LogfileAuditServiceImpl;

/**
 * Play controller with audit service available
 *
 * @author jonnyheavey
 *
 */
public class AuditedController extends Controller {

    protected AuditService auditService;

    public AuditedController() {
    	this.auditService = LogfileAuditServiceImpl.getInstance();
    }

    public void setAuditService(AuditService auditService) {
    	this.auditService = auditService;
    }

}
