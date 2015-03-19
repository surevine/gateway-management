package com.surevine.gateway.federation;

import models.Partner;
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
	 * @param partner partner to transfer project to
	 * @param repository repository to transfer
	 * @throws FederatorServiceException
	 */
	public void distribute(Partner partner, Repository repository) throws FederatorServiceException;

}
