package com.surevine.gateway.federation.scm;

import static play.mvc.Http.Status.OK;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import models.Partner;
import models.Repository;

import play.Logger;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;

import com.surevine.gateway.federation.FederatorServiceException;
import com.surevine.gateway.federation.FederatorServiceFacade;
import com.typesafe.config.ConfigFactory;

/**
 * Service Facade for the SCM Federator API
 *
 * @author jonnyheavey
 *
 */
public class SCMFederatorServiceFacade implements FederatorServiceFacade {

	/**
	 * Default request timeout in milliseconds
	 */
	private static final int REQUEST_TIMEOUT = 10000;

	/**
	 * API Base URL loaded from config
	 */
	private static final String SCM_FEDERATOR_API_BASE_URL = ConfigFactory.load().getString("scm.federator.api.base.url");

	private static final String SCM_FEDERATOR_API_DISTRIBUTE_PATH = "/distribute";

	private static SCMFederatorServiceFacade _instance = null;

	private SCMFederatorServiceFacade() {

	}

	public static SCMFederatorServiceFacade getInstance() {
		if(_instance == null) {
			_instance = new SCMFederatorServiceFacade();
		}
		return _instance;
	}

	@Override
	public void distribute(Partner partner, Repository repository) throws FederatorServiceException {

		Logger.info(String.format("Requesting distribution of repo (%s) by scm-federator [%s]",
				repository.identifier, SCM_FEDERATOR_API_BASE_URL + SCM_FEDERATOR_API_DISTRIBUTE_PATH));

    	Promise<WSResponse> promise = postDistributionRequest(partner.getSourceKey(), repository.getIdentifier());

    	WSResponse response;
    	try {
        	response = promise.get(REQUEST_TIMEOUT);
    	} catch(Exception e) {
    		String errorMessage = "Error interacting with scm-federator service.";
    		Logger.warn(errorMessage, e);
    		throw new FederatorServiceException(errorMessage, e);
    	}

    	if(response.getStatus() != OK) {
    		Logger.warn(String.format("SCM Federator service returned non-ok response: %s, %s", response.getStatus(), response.getStatusText()));
    		throw new FederatorServiceException(String.format("Error response %s from distribute repository request. %s.",
    															response.getStatus(),
    															response.getBody()));
    	}

	}

	/**
	 * Post request to SCM federator distribution service
	 *
	 * @param partnerId
	 * @param identifier
	 * @return
	 * @throws FederatorServiceException
	 */
	private Promise<WSResponse> postDistributionRequest(String partnerSourceKey,
			String identifier) throws FederatorServiceException {

		try {
			return WS.url(SCM_FEDERATOR_API_BASE_URL + SCM_FEDERATOR_API_DISTRIBUTE_PATH)
					.setTimeout(REQUEST_TIMEOUT)
					.setQueryParameter("partner", partnerSourceKey)
					.setQueryParameter("identifier", URLEncoder.encode(identifier, "UTF-8"))
					.setContentType("application/json")
					.post("");
		} catch (UnsupportedEncodingException e) {
			throw new FederatorServiceException("Failed to URL encode identifier query parameter.", e);
		}
	}

}
