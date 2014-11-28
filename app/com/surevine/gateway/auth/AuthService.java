package com.surevine.gateway.auth;

/**
 * Authenticates user.
 *
 * @author jonnyheavey
 *
 */
public interface AuthService {

	/**
	 * Get username of currently authenticated user
	 * @return String username of authenticated user or null if unauthorised.
	 * @throws AuthServiceException
	 */
	public String getAuthenticatedUsername() throws AuthServiceException;

}
