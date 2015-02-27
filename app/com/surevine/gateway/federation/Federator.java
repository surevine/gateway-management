package com.surevine.gateway.federation;

import com.surevine.gateway.federation.issues.IssuesFederatorServiceFacade;
import com.surevine.gateway.federation.scm.SCMFederatorServiceFacade;

import models.Partner;
import models.Repository;

public abstract class Federator {

	public static void distribute(Partner destination, Repository repository) throws FederatorServiceException {

		switch(repository.repoType) {
			case SCM:
				SCMFederatorServiceFacade.getInstance().distribute(destination, repository);
				break;
			case ISSUE:
				IssuesFederatorServiceFacade.getInstance().distribute(destination, repository);
				break;
			default:
				throw new FederatorServiceException("Unexpected repository type. Cannot distribute.");
		}

	}

	public static void resend(Partner destination, Repository repository) {
		// TODO as above
	}

}
