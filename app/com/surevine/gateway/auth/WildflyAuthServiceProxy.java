package com.surevine.gateway.auth;

import static play.mvc.Http.Status.OK;
import play.Logger;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;

import com.typesafe.config.ConfigFactory;

/**
 * Interacts with Wildfly auth-proxy component to retrieve
 * information on currently (centrally) authenticated user.
 *
 * @author jonnyheavey
 *
 */
public class WildflyAuthServiceProxy implements AuthServiceProxy {

	private static int REQUEST_TIMEOUT = 5000;
	private static String WILDFLY_AUTH_SERVICE_BASE_URL =  ConfigFactory.load().getString("wildfly.auth.service.base.url");

	private static WildflyAuthServiceProxy _instance = null;

	private WildflyAuthServiceProxy() {

	}

	public static WildflyAuthServiceProxy getInstance() {
		if(_instance == null) {
			_instance = new WildflyAuthServiceProxy();
		}
		return _instance;
	}

	/**
	 * Get username of remotely authenticated user
	 * @return
	 * @throws AuthServiceProxyException
	 */
	public String getAuthenticatedUsername() throws AuthServiceProxyException {

		// TODO confirm actual API URL
		Promise<WSResponse> promise = WS.url(WILDFLY_AUTH_SERVICE_BASE_URL + "/currentuser")
										.setTimeout(REQUEST_TIMEOUT)
						    			.get();

    	WSResponse response;
    	try {
        	response = promise.get(REQUEST_TIMEOUT);
    	} catch(Exception e) {
    		Logger.warn("Error connecting to Wildfly auth-proxy component. " + e.getMessage());
    		throw new AuthServiceProxyException("Failed to connect to wildfly PKI authentication component.");
    	}

    	if(response.getStatus() != OK) {
    		Logger.warn(String.format("Wildfly auth-proxy component returned non-ok response code: ", response.getStatus()));
    		throw new AuthServiceProxyException(String.format("Error response %s from get authenticated username request. %s.",
    															response.getStatus(),
    															response.getBody()));
    	}

		return null;
	}

}
