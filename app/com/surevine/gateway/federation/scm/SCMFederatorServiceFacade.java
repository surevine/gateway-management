package com.surevine.gateway.federation.scm;

import static play.mvc.Http.Status.OK;
import models.Destination;
import models.Repository;

import play.Logger;
import play.libs.F.Callback;
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

	private static final String SCM_FEDERATOR_API_DISTRIBUTE_PATH = "/rest/federator/distribute";

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
	public void distribute(Destination destination, Repository repository) throws FederatorServiceException {

		Logger.info(String.format("Requesting distribution of repo (%s) by scm-federator [%s]",
				repository.identifier, SCM_FEDERATOR_API_BASE_URL + SCM_FEDERATOR_API_DISTRIBUTE_PATH));

    	Promise<WSResponse> promise = postDistributionRequest(destination.id, repository.identifier);

    	WSResponse response;
    	try {
        	response = promise.get(REQUEST_TIMEOUT);
    	} catch(Exception e) {
    		String errorMessage = "Error connecting to scm-federator service.";
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
	 * @param destinationId
	 * @param projectKey
	 * @param repoSlug
	 * @return
	 */
	private Promise<WSResponse> postDistributionRequest(Long destinationId, String identifier) {
		return WS.url(SCM_FEDERATOR_API_BASE_URL + SCM_FEDERATOR_API_DISTRIBUTE_PATH)
				.setTimeout(REQUEST_TIMEOUT)
				.setQueryParameter("destination", destinationId.toString())
    			.setQueryParameter("identifier", identifier)
    			.setContentType("application/json")
    			.post("");
	}

}
