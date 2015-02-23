package com.surevine.gateway.federation;

import com.surevine.gateway.federation.issues.IssuesFederatorServiceFacade;

import models.Destination;
import models.Repository;

public abstract class Federator {

	public static void distribute(Destination destination, Repository repository) throws FederatorServiceException {

		switch(repository.repoType) {
			case SCM:
				// TODO refactor / move SCM federator and add support!
				break;
			case ISSUE:
				IssuesFederatorServiceFacade.getInstance().distribute(destination, repository);
				break;
			default:
				throw new FederatorServiceException("Unexpected repository type. Cannot distribute.");
		}

	}

	public static void resend(Destination destination, Repository repository) {
		// TODO as above
	}

}
