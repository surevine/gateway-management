package com.surevine.gateway.scm.service;

import static play.mvc.Http.Status.OK;

import play.Logger;
import play.libs.F.Callback;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;

import com.typesafe.config.ConfigFactory;

/**
 * Service Facade for the SCM Federator API
 *
 * @author jonnyheavey
 *
 */
public class SCMFederatorServiceFacade {

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

	/**
	 * Instruct federator to perform distribution of project to destination.
	 * Asynchronous 'fire and forget' request.
	 *
	 * @param destinationId Id of destination to transfer project to
	 * @param projectKey project key slug of project's SCM URL
	 * @param repoSlug repository slug of project's SCM URL
	 */
	public void distribute(String destinationId, String projectKey, String repoSlug) {

		Logger.info(String.format("Informing SCM component of new sharing partnership %s/%s [%s]", projectKey, repoSlug, SCM_FEDERATOR_API_BASE_URL + SCM_FEDERATOR_API_DISTRIBUTE_PATH));

		Promise<WSResponse> promise = postDistributionRequest(destinationId, projectKey, repoSlug);

    	promise.onFailure(new Callback<Throwable>(){
			@Override
			public void invoke(Throwable t) throws Throwable {
				Logger.warn("Unable to inform SCM federation component of new sharing partnership. " + t.getMessage());
			}
    	});

	}

	/**
	 * Instruct federator to perform ad-hoc redistribution of project to destination.
	 * This method performs synchronous request response is required.
	 *
	 * @param destinationId
	 * @param projectKey
	 * @param repoSlug
	 * @throws Exception
	 */
	public void resend(String destinationId, String projectKey, String repoSlug) throws SCMFederatorServiceException {

		Logger.info(String.format("Requesting redistribution of repo (%s/%s) by SCM component [%s]",
				projectKey, repoSlug, SCM_FEDERATOR_API_BASE_URL + SCM_FEDERATOR_API_DISTRIBUTE_PATH));

    	Promise<WSResponse> promise = postDistributionRequest(destinationId, projectKey, repoSlug);

    	WSResponse response;
    	try {
        	response = promise.get(REQUEST_TIMEOUT);
    	} catch(Exception e) {
    		String errorMessage = "Error connecting to SCM federator service.";
    		Logger.warn(errorMessage, e);
    		throw new SCMFederatorServiceException(errorMessage, e);
    	}

    	if(response.getStatus() != OK) {
    		Logger.warn(String.format("SCM Federator service returned non-ok response: %s, %s", response.getStatus(), response.getStatusText()));
    		throw new SCMFederatorServiceException(String.format("Error response %s from resend repository request. %s.",
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
	private Promise<WSResponse> postDistributionRequest(String destinationId, String projectKey, String repoSlug) {
		return WS.url(SCM_FEDERATOR_API_BASE_URL + SCM_FEDERATOR_API_DISTRIBUTE_PATH)
				.setTimeout(REQUEST_TIMEOUT)
				.setQueryParameter("destination", destinationId)
    			.setQueryParameter("projectKey", projectKey)
    			.setQueryParameter("repositorySlug", repoSlug)
    			.setContentType("application/json")
    			.post("");
	}

}
