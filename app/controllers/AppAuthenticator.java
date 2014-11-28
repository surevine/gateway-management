package controllers;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.surevine.gateway.auditing.AuditEvent;
import com.surevine.gateway.auditing.AuditService;
import com.surevine.gateway.auditing.LogfileAuditServiceImpl;
import com.surevine.gateway.auditing.action.AuditAction;
import com.surevine.gateway.auditing.action.AuditActionFactory;
import com.surevine.gateway.auditing.action.LogfileAuditActionFactory;
import com.surevine.gateway.auditing.action.xml.XMLAuditActionFactory;
import com.surevine.gateway.auth.AuthService;
import com.surevine.gateway.auth.AuthServiceException;
import com.surevine.gateway.auth.WildflyAuthService;

import play.Logger;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Application authenticator.
 * Interacts with auth service to authenticate user.
 *
 * @author jonnyheavey
 *
 */
@org.springframework.stereotype.Controller
public class AppAuthenticator extends Security.Authenticator {

	@Autowired
	@Qualifier("authService")
	private AuthService authService;

	@Autowired
	@Qualifier("auditService")
    private AuditService auditService;

	@Autowired
	@Qualifier("auditActionFactory")
    private AuditActionFactory auditActionFactory;

	/**
	 * Gets username of authenticated user.
	 * @return String username of authenticated user, or null if not authorised.
	 */
	@Override
    public String getUsername(Context ctx) {

		String authenticatedUser = ctx.session().get("username");

		if(authenticatedUser != null) {
			return authenticatedUser;
		} else {
			try {
				authenticatedUser = authService.getAuthenticatedUsername();
			} catch (AuthServiceException e) {
				Logger.warn("Could not authenticate current user. " + e.getMessage());
				return null;
			}

			if(authenticatedUser != null) {
				// Store authenticated username in session for future requests
				ctx.session().put("username", authenticatedUser);

				// Audit login
		    	AuditAction action = auditActionFactory.getUserLoginAction(authenticatedUser);
				AuditEvent event = new AuditEvent(action, Calendar.getInstance().getTime(), authenticatedUser);
				auditService.audit(event);

				return authenticatedUser;
			}
		}

		// Authentication check failed
		return null;
    }

	/**
	 * Action performed when current user session is unauthorised.
	 */
    @Override
    public Result onUnauthorized(Context ctx) {
        return unauthorized(views.html.unauthorised.render());
    }

    public void setAuthServiceProxy(AuthService authServiceProxy) {
    	this.authService = authServiceProxy;
    }

    public void setAuditService(AuditService auditService) {
    	this.auditService = auditService;
    }

    public void setAuditActionFactory(AuditActionFactory auditActionFactory) {
    	this.auditActionFactory = auditActionFactory;
    }

}
