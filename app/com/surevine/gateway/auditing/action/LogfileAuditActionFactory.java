package com.surevine.gateway.auditing.action;

import models.Partner;
import models.FederationConfiguration;
import models.Repository;

/**
 * AuditActionFactory producing actions to be audited in logfile
 *
 * @author jonnyheavey
 *
 */
@org.springframework.stereotype.Component
public class LogfileAuditActionFactory implements AuditActionFactory {

	@Override
	public CreatePartnerAction getCreatePartnerAction(Partner partner) {
		return new CreatePartnerAction(partner);
	}

	@Override
	public UserLoginAction getUserLoginAction(String username) {
		return new UserLoginAction(username);
	}

	@Override
	public UpdatePartnerAction getUpdatePartnerAction(
			Partner originalPartner, Partner updatedPartner) {
		return new UpdatePartnerAction(originalPartner, originalPartner);
	}

	@Override
	public DeletePartnerAction getDeletePartnerAction(
			Partner partner) {
		return new DeletePartnerAction(partner);
	}

	@Override
	public CreateRepositoryAction getCreateRepositoryAction(Repository repository) {
		return new CreateRepositoryAction(repository);
	}

	@Override
	public UpdateRepositoryAction getUpdateRepositoryAction(
			Repository originalRepo, Repository updatedRepo) {
		return new UpdateRepositoryAction(originalRepo, updatedRepo);
	}

	@Override
	public DeleteRepositoryAction getDeleteRepositoryAction(Repository repository) {
		return new DeleteRepositoryAction(repository);
	}

	@Override
	public ModifyGlobalRulesAction getModifyGlobalRulesAction(String ruleFile,
			String ruleFileContents) {
		return new ModifyGlobalRulesAction(ruleFile, ruleFileContents);
	}

	@Override
	public ModifyPartnerRulesAction getModifyPartnerRulesAction(
			Partner partner, String ruleFileContents) {
		return new ModifyPartnerRulesAction(partner, ruleFileContents);
	}

	@Override
	public ShareRepositoryAction getShareRepositoryAction(FederationConfiguration config) {
		return new ShareRepositoryAction(config);
	}

	@Override
	public UnshareRepositoryAction getUnshareRepositoryAction(FederationConfiguration config) {
		return new UnshareRepositoryAction(config);
	}

	@Override
	public ResendRepositoryAction getResendRepositoryAction(FederationConfiguration config) {
		return new ResendRepositoryAction(config);
	}

	@Override
	public UpdateFederationAction getUpdateFederationAction(
			FederationConfiguration config, String updatedDirection, boolean federationEnabled) {
		return new UpdateFederationAction(config, updatedDirection, federationEnabled);
	}

}
