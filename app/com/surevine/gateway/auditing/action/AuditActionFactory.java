package com.surevine.gateway.auditing.action;

import models.Destination;
import models.Repository;

/**
 * @author jonnyheavey
 */
public interface AuditActionFactory {

	/**
	 * Get an AuditService specific implementation of UserLoginAction
	 * @param username User who logged in
	 * @return AuditService specific implementation of UserLoginAction
	 */
	UserLoginAction getUserLoginAction(String username);

	/**
	 * Get an AuditService specific implementation of CreateDestinationAction
	 * @param destination Created destination
	 * @return AuditService specific implementation of CreateDestinationAction
	 */
	CreateDestinationAction getCreateDestinationAction(Destination destination);

	/**
	 * Get an AuditService specific implementation of UpdateDestinationAction
	 * @param originalDestination Destination state before update
	 * @param updatedDestination Destination state after update
	 * @return AuditService specific implementation of UpdateDestinationAction
	 */
	UpdateDestinationAction getUpdateDestinationAction(Destination originalDestination, Destination updatedDestination);

	/**
	 * Get an AuditService specific implementation of DeleteDestinationAction
	 * @param destination Deleted destination
	 * @return AuditService specific implementation of DeleteDestinationAction
	 */
	DeleteDestinationAction getDeleteDestinationAction(Destination destination);

	/**
	 * Get an AuditService specific implementation of CreateRepositoryAction
	 * @param project Created project
	 * @return AuditService specific implementation of CreateRepositoryAction
	 */
	CreateRepositoryAction getCreateRepositoryAction(Repository repository);

	/**
	 * Get an AuditService specific implementation of UpdateRepositoryAction
	 * @param originalProject Project state before update
	 * @param updatedProject Project state before update
	 * @return AuditService specific implementation of UpdateRepositoryAction
	 */
	UpdateRepositoryAction getUpdateRepositoryAction(Repository originalRepo, Repository updatedRepo);

	/**
	 * Get an AuditService specific implementation of DeleteRepositoryAction
	 * @param project Deleted project
	 * @return AuditService specific implementation of DeleteRepositoryAction
	 */
	DeleteRepositoryAction getDeleteRepositoryAction(Repository repository);

	/**
	 * Get an AuditService specific implementation of ModifyGlobalRulesAction
	 * @param ruleFile Rulefile that was updated (export or import)
	 * @param ruleFileContents New contents of rulefile
	 * @return AuditService specific implementation of ModifyGlobalRulesAction
	 */
	ModifyGlobalRulesAction getModifyGlobalRulesAction(String ruleFile, String ruleFileContents);

	/**
	 * Get an AuditService specific implementation of ModifyDestinationRulesAction
	 * @param destination Destination who's rulefile was updated
	 * @param ruleFileContents New contents of rulefile
	 * @return AuditService specific implementation of ModifyDestinationRulesAction
	 */
	ModifyDestinationRulesAction getModifyDestinationRulesAction(Destination destination, String ruleFileContents);

	/**
	 * Get an AuditService specific implementation of ShareRepositoryAction
	 * @param project Shared project
	 * @param destination Destination the project was shared to
	 * @return AuditService specific implementation of ShareRepositoryAction
	 */
	ShareRepositoryAction getShareRepositoryAction(Repository repository, Destination destination);

	/**
	 * Get an AuditService specific implementation of UnshareRepositoryAction
	 * @param project Unshared project
	 * @param destination Destination the project was unshared from
	 * @return AuditService specific implementation of UnshareRepositoryAction
	 */
	UnshareRepositoryAction getUnshareRepositoryAction(Repository repository, Destination destination);

	/**
	 * Get an AuditService specific implementation of ResendRepositoryAction
	 * @param project Shared project
	 * @param destination Destination the project was resent to
	 * @return AuditService specific implementation of ResendRepositoryAction
	 */
	ResendRepositoryAction getResendRepositoryAction(Repository repository, Destination destination);

	/**
	 * Get an AuditService specific implementation of the ToggleFederationAction
	 * @param repository repository being federated
	 * @param destination the destination the repo is federated with
	 * @param direction the direction of federation
	 * @param enabled whether federation has been enabled or disabled
	 * @return
	 */
	ToggleFederationAction getToggleFederationAction(Repository repository, Destination destination, String direction, boolean enabled);

}
