package controllers;

import java.util.Calendar;

import com.surevine.gateway.auditing.Audit;
import com.surevine.gateway.auditing.AuditEvent;
import com.surevine.gateway.auditing.AuditServiceException;
import com.surevine.gateway.auditing.action.UserLoginAction;
import com.surevine.gateway.auth.AuthService;
import com.surevine.gateway.auth.AuthServiceException;
import com.surevine.gateway.auth.PublicAuthService;
import com.surevine.gateway.auth.WildflyAuthService;
import com.typesafe.config.ConfigFactory;

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
public class AppAuthenticator extends Security.Authenticator {

	private static final String PUBLIC = "public";
	private static final String REMOTE = "remote";

	private AuthService authService;

	public AppAuthenticator() {

		switch(ConfigFactory.load().getString("gateway.auth.mode")) {
			case PUBLIC:
				this.authService = new PublicAuthService();
				break;
			case REMOTE:
				this.authService = WildflyAuthService.getInstance();
				break;
			default:
				throw new AuditServiceException("Could not initialise App authentication. Authentication mode not correctly configured.");
		}

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
				authenticatedUser = authService.getAuthenticatedUsername();
			} catch (AuthServiceException e) {
				Logger.warn("Could not authenticate current user. " + e.getMessage());
				return null;
			}

			if(authenticatedUser != null) {
				// Store authenticated username in session for future requests
				ctx.session().put("username", authenticatedUser);

				// Audit login
				UserLoginAction action = Audit.getUserLoginAction(authenticatedUser);
				AuditEvent event = new AuditEvent(action, Calendar.getInstance().getTime(), authenticatedUser);
				Audit.audit(event);

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

    /**
     * Explicitly set an AuthService.
	 * Primarily used for unit test mocking.
     *
     * @param authService
     */
    public void setAuthService(AuthService authService) {
    	this.authService = authService;
    }

}
