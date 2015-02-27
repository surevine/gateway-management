package com.surevine.gateway.federation.issues;

import static play.mvc.Http.Status.OK;
import models.Partner;
import models.Repository;
import play.Logger;
import play.libs.F.Callback;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;

import com.surevine.gateway.federation.FederatorServiceException;
import com.surevine.gateway.federation.FederatorServiceFacade;
import com.typesafe.config.ConfigFactory;

public class IssuesFederatorServiceFacade implements FederatorServiceFacade {

	private static final String ISSUE_FEDERATOR_API_BASE_URL = ConfigFactory.load().getString("issue.federator.api.base.url");
	private static final String ISSUE_FEDERATOR_API_DISTRIBUTE_PATH = "/distribute";

	/**
	 * Default request timeout in milliseconds
	 */
	private static final int REQUEST_TIMEOUT = 10000;

	private static IssuesFederatorServiceFacade _instance = null;

	private IssuesFederatorServiceFacade() {

	}

	public static IssuesFederatorServiceFacade getInstance() {
		if(_instance == null) {
			_instance = new IssuesFederatorServiceFacade();
		}
		return _instance;
	}

	@Override
	public void distribute(Partner partner, Repository repository)
			throws FederatorServiceException {

		Logger.info(String.format("Informing issue-federator of new sharing partnership %s [%s]",
				repository.identifier, ISSUE_FEDERATOR_API_BASE_URL + ISSUE_FEDERATOR_API_DISTRIBUTE_PATH));

    	Promise<WSResponse> promise = postDistributionRequest(partner.sourceKey, repository.identifier);

    	WSResponse response;
    	try {
        	response = promise.get(REQUEST_TIMEOUT);
    	} catch(Exception e) {
    		String errorMessage = "Error connecting to issue federator service.";
    		Logger.warn(errorMessage, e);
    		throw new FederatorServiceException(errorMessage, e);
    	}

    	if(response.getStatus() != OK) {
    		Logger.warn(String.format("Issue Federator service returned non-ok response: %s, %s", response.getStatus(), response.getStatusText()));
    		throw new FederatorServiceException(String.format("Error response %s from distribute repository request. %s.",
    															response.getStatus(),
    															response.getBody()));
    	}

	}

	/**
	 * Post request to issue federator distribution service
	 *
	 * @param destinationId
	 * @param identifier
	 * @return
	 */
	private Promise<WSResponse> postDistributionRequest(String partnerSourceKey,
			String identifier) {
		return WS.url(ISSUE_FEDERATOR_API_BASE_URL + ISSUE_FEDERATOR_API_DISTRIBUTE_PATH)
				.setTimeout(REQUEST_TIMEOUT)
				.setQueryParameter("partner", partnerSourceKey)
    			.setQueryParameter("identifier", identifier)
    			.setContentType("application/json")
    			.post("");
	}

}
