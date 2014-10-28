package com.surevine.gateway.scm.service;

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
	 * Instruct federator to perform ad-hoc distribution of project to destination
	 *
	 * @param destinationId Id of destination to transfer project to
	 * @param projectKey project key slug of project's SCM URL
	 * @param repoSlug repository slug of project's SCM URL
	 */
	public void distribute(String destinationId, String projectKey, String repoSlug) {

		String scmFederatorBaseURL = ConfigFactory.load().getString("scm.federator.api.base.url");

		Logger.info(String.format("Informing SCM component of new sharing partnership %s/%s [%s]", projectKey, repoSlug, scmFederatorBaseURL + "/rest/federator/distribute"));

    	Promise<WSResponse> promise = WS.url(scmFederatorBaseURL + "/rest/federator/distribute")
    			.setTimeout(5000)
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

}
