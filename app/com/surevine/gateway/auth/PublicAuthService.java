package com.surevine.gateway.auth;

/**
 * AuthService implementation to always allow access to restricted actions.
 * Used mostly for development purposes.
 *
 * @author jonnyheavey
 *
 */
public class PublicAuthService implements AuthService {

	@Override
	public String getAuthenticatedUsername() throws AuthServiceException {
		// Always return string (actions accessible to all users)
		return "Unautheticated";
	}

}
