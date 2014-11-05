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
	private static final int REQUEST_TIMEOUT = 5000;

	/**
	 * Instruct federator to perform ad-hoc distribution of project to destination.
	 * Asynchronous request.
	 *
	 * @param destinationId Id of destination to transfer project to
	 * @param projectKey project key slug of project's SCM URL
	 * @param repoSlug repository slug of project's SCM URL
	 */
	public void distribute(String destinationId, String projectKey, String repoSlug) {

		String scmFederatorBaseURL = ConfigFactory.load().getString("scm.federator.api.base.url");

		Logger.info(String.format("Informing SCM component of new sharing partnership %s/%s [%s]", projectKey, repoSlug, scmFederatorBaseURL + "/rest/federator/distribute"));

    	Promise<WSResponse> promise = WS.url(scmFederatorBaseURL + "/rest/federator/distribute")
    			.setTimeout(REQUEST_TIMEOUT)
    			.setQueryParameter("destination", destinationId)
    			.setQueryParameter("projectKey", projectKey)
    			.setQueryParameter("repositorySlug", repoSlug)
    			.setContentType("application/json")
    			.post("");

    	promise.onFailure(new Callback<Throwable>(){
			@Override
			public void invoke(Throwable t) throws Throwable {
				Logger.warn("Unable to inform SCM federation component of new sharing partnership. " + t.getMessage());
			}
    	});

	}

	/**
	 * Ad-hoc re-distribution of repo to destination.
	 * This method performs synchronous request response is required.
	 *
	 * @param destinationId
	 * @param projectKey
	 * @param repoSlug
	 * @throws Exception
	 */
	public void resend(String destinationId, String projectKey, String repoSlug) throws SCMFederatorServiceException {

		String scmFederatorBaseURL = ConfigFactory.load().getString("scm.federator.api.base.url");

		Logger.info(String.format("Requesting redistribution of repo (%s/%s) by SCM component [%s]", projectKey, repoSlug, scmFederatorBaseURL + "/rest/federator/distribute"));

    	Promise<WSResponse> promise = WS.url(scmFederatorBaseURL + "/rest/federator/distribute")
    			.setQueryParameter("destination", destinationId)
    			.setQueryParameter("projectKey", projectKey)
    			.setQueryParameter("repositorySlug", repoSlug)
    			.setContentType("application/json")
    			.post("");

    	WSResponse response;
    	try {
        	response = promise.get(REQUEST_TIMEOUT);
    	} catch(Exception e) {
    		Logger.warn("Error connecting to SCM federator service. " + e.getMessage());
    		throw new SCMFederatorServiceException("Error connecting to SCM federator service. " + e.getMessage());
    	}

    	if(response.getStatus() != OK) {
    		Logger.warn(String.format("SCM Federator service returned non-ok response code: ", response.getStatus()));
    		throw new SCMFederatorServiceException(String.format("Error response %s from resend repository request. %s.",
    															response.getStatus(),
    															response.getBody()));
    	}

	}

}
