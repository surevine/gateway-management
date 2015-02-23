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
	 *
	 * @param destination destination to transfer project to
	 * @param repository repository to transfer
	 * @throws FederatorServiceException
	 */
	public void distribute(Destination destination, Repository repository) throws FederatorServiceException;

}
