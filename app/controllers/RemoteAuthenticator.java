package controllers;

import java.util.Calendar;

import com.surevine.gateway.auditing.AuditEvent;
import com.surevine.gateway.auditing.AuditService;
import com.surevine.gateway.auditing.GatewayAction;
import com.surevine.gateway.auditing.LogfileAuditServiceImpl;
import com.surevine.gateway.auth.AuthServiceProxy;
import com.surevine.gateway.auth.AuthServiceProxyException;
import com.surevine.gateway.auth.WildflyAuthServiceProxy;

import play.Logger;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Application authenticator.
 * Interacts with wildfly auth proxy component to retrieve authenticated user information.
 *
 * @author jonnyheavey
 *
 */
public class RemoteAuthenticator extends Security.Authenticator {

	private AuthServiceProxy authServiceProxy;
	private AuditService auditService;

	public RemoteAuthenticator() {
		this.authServiceProxy = WildflyAuthServiceProxy.getInstance();
		this.auditService = LogfileAuditServiceImpl.getInstance();
	}

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
				authenticatedUser = authServiceProxy.getAuthenticatedUsername();
			} catch (AuthServiceProxyException e) {
				Logger.warn("Could not authenticate current user. " + e.getMessage());
				return null;
			}

			if(authenticatedUser != null) {
				// Store authenticated username in session for future requests
				ctx.session().put("username", authenticatedUser);

				// Audit login
				AuditEvent event = new AuditEvent(GatewayAction.USER_LOG_IN, Calendar.getInstance().getTime(), authenticatedUser, "User logged in.");
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

    public void setAuthServiceProxy(AuthServiceProxy authServiceProxy) {
    	this.authServiceProxy = authServiceProxy;
    }

}
