package controllers;

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
public class WildflyRemoteAuthenticator extends Security.Authenticator {

	// TODO init default authenticator, have setService method (for testability)

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
				authenticatedUser = WildflyAuthServiceProxy.getInstance().getAuthenticatedUsername();
			} catch (AuthServiceProxyException e) {
				Logger.warn("Could not authenticate current user. " + e.getMessage());
				return null;
			}

			if(authenticatedUser != null) {
				// Store authenticated username in session for future requests
				ctx.session().put("username", authenticatedUser);
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

}
