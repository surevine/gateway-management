package com.surevine.gateway.auth;

import play.mvc.Http.Context;

/**
 * AuthService implementation to always allow access to restricted actions.
 * Used mostly for development purposes.
 *
 * @author jonnyheavey
 *
 */
public class PublicAuthService implements AuthService {

	@Override
	public String getAuthenticatedUsername(Context ctx) throws AuthServiceException {
		// Always return string (actions accessible to all users)
		return "Unauthenticated";
	}

}
