package com.surevine.gateway.auth;

/**
 * Retrieve information on authenticated user from remote/central authentication system
 *
 * @author jonnyheavey
 *
 */
public interface AuthServiceProxy {

	/**
	 * Get username of currently authenticated user
	 * @return String username of authenticated user or null if unauthorised.
	 * @throws AuthServiceProxyException
	 */
	public String getAuthenticatedUsername() throws AuthServiceProxyException;

}
