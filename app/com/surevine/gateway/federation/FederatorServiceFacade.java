package com.surevine.gateway.federation;

import models.Destination;
import models.Repository;

/**
 *
 * @author jonnyheavey
 *
 */
public interface FederatorServiceFacade {

	/**
	 * Instruct federator to perform distribution of repository to destination.
	 * Asynchronous 'fire and forget' request.
	 *
	 * @param destination destination to transfer project to
	 * @param repository repository to transfer
	 * @throws FederatorServiceException
	 */
	public void distribute(Destination destination, Repository repository) throws FederatorServiceException;

	/**
	 * Instruct federator to perform ad-hoc redistribution of repository to destination.
	 * This method performs synchronous request, response is required.
	 *
	 * @param destination destination to transfer project to
	 * @param repository repository to transfer
	 * @throws FederatorServiceException
	 */
	public void resend(Destination destination, Repository repository) throws FederatorServiceException;

}
