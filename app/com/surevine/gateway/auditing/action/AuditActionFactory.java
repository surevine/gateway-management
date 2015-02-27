package com.surevine.gateway.auditing.action;

import models.Partner;
import models.FederationConfiguration;
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
	 * Get an AuditService specific implementation of CreatePartnerAction
	 * @param partner Created partner
	 * @return AuditService specific implementation of CreatePartnerAction
	 */
	CreatePartnerAction getCreatePartnerAction(Partner partner);

	/**
	 * Get an AuditService specific implementation of UpdatePartnerAction
	 * @param originalPartner Partner state before update
	 * @param updatedPartner Partner state after update
	 * @return AuditService specific implementation of UpdatePartnerAction
	 */
	UpdatePartnerAction getUpdatePartnerAction(Partner originalPartner, Partner updatedPartner);

	/**
	 * Get an AuditService specific implementation of DeletePartnerAction
	 * @param partner Deleted partner
	 * @return AuditService specific implementation of DeletePartnerAction
	 */
	DeletePartnerAction getDeletePartnerAction(Partner partner);

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
	 * Get an AuditService specific implementation of ModifyPartnerRulesAction
	 * @param partner Partner who's rulefile was updated
	 * @param ruleFileContents New contents of rulefile
	 * @return AuditService specific implementation of ModifyPartnerRulesAction
	 */
	ModifyPartnerRulesAction getModifyPartnerRulesAction(Partner partner, String ruleFileContents);

	/**
	 * Get an AuditService specific implementation of ShareRepositoryAction
	 * @param project Shared project
	 * @param partner Partner the project was shared to
	 * @return AuditService specific implementation of ShareRepositoryAction
	 */
	ShareRepositoryAction getShareRepositoryAction(FederationConfiguration config);

	/**
	 * Get an AuditService specific implementation of UnshareRepositoryAction
	 * @param project Unshared project
	 * @param partner Partner the project was unshared from
	 * @return AuditService specific implementation of UnshareRepositoryAction
	 */
	UnshareRepositoryAction getUnshareRepositoryAction(FederationConfiguration config);

	/**
	 * Get an AuditService specific implementation of ResendRepositoryAction
	 * @param project Shared project
	 * @param partner Partner the project was resent to
	 * @return AuditService specific implementation of ResendRepositoryAction
	 */
	ResendRepositoryAction getResendRepositoryAction(FederationConfiguration config);

	/**
	 * Get an AuditService specific implementation of the ToggleFederationAction
	 * @param repository repository being federated
	 * @param partner the partner the repo is federated with
	 * @param direction the direction of federation
	 * @param enabled whether federation has been enabled or disabled
	 * @return
	 */
	UpdateFederationAction getUpdateFederationAction(FederationConfiguration config, String updatedDirection, boolean federationEnabled);

}
