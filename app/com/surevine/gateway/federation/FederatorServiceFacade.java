package com.surevine.gateway.federation;

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
	 * @param destinationId Id of destination to transfer project to
	 * @param identifier Unique repository identifier
	 * @throws FederatorServiceException
	 */
	public void distribute(String destinationId, String identifier) throws FederatorServiceException;

	/**
	 * Instruct federator to perform ad-hoc redistribution of repository to destination.
	 * This method performs synchronous request, response is required.
	 *
	 * @param destinationId Id of destination to transfer project to
	 * @param identifier Unique repository identifier
	 * @throws FederatorServiceException
	 */
	public void resend(String destinationId, String identifier) throws FederatorServiceException;

}
