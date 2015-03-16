package com.surevine.gateway.auditing.action.xml;

import models.Partner;
import models.FederationConfiguration;
import models.Repository;

import com.surevine.gateway.auditing.action.AuditActionFactory;
import com.surevine.gateway.auditing.action.CreatePartnerAction;
import com.surevine.gateway.auditing.action.CreateRepositoryAction;
import com.surevine.gateway.auditing.action.DeletePartnerAction;
import com.surevine.gateway.auditing.action.DeleteRepositoryAction;
import com.surevine.gateway.auditing.action.ModifyPartnerRulesAction;
import com.surevine.gateway.auditing.action.ModifyGlobalRulesAction;
import com.surevine.gateway.auditing.action.ResendRepositoryAction;
import com.surevine.gateway.auditing.action.ShareRepositoryAction;
import com.surevine.gateway.auditing.action.UpdateFederationAction;
import com.surevine.gateway.auditing.action.UnshareRepositoryAction;
import com.surevine.gateway.auditing.action.UpdatePartnerAction;
import com.surevine.gateway.auditing.action.UpdateRepositoryAction;
import com.surevine.gateway.auditing.action.UserLoginAction;

/**
 * AuditActionFactory producing actions to be audited in XML file
 *
 * @author jonnyheavey
 *
 */
@org.springframework.stereotype.Component
public class XMLAuditActionFactory implements AuditActionFactory {

	@Override
	public CreatePartnerAction getCreatePartnerAction(Partner partner) {
		return new XMLCreatePartnerAction(partner);
	}

	@Override
	public UserLoginAction getUserLoginAction(String username) {
		return new XMLUserLoginAction(username);
	}

	@Override
	public UpdatePartnerAction getUpdatePartnerAction(
			Partner originalPartner, Partner updatedPartner) {
		return new XMLUpdatePartnerAction(originalPartner, updatedPartner);
	}

	@Override
	public DeletePartnerAction getDeletePartnerAction(
			Partner partner) {
		return new XMLDeletePartnerAction(partner);
	}

	@Override
	public CreateRepositoryAction getCreateRepositoryAction(Repository repository) {
		return new XMLCreateRepositoryAction(repository);
	}

	@Override
	public UpdateRepositoryAction getUpdateRepositoryAction(
			Repository originalRepo, Repository updatedRepo) {
		return new XMLUpdateRepositoryAction(originalRepo, updatedRepo);
	}

	@Override
	public DeleteRepositoryAction getDeleteRepositoryAction(Repository repository) {
		return new XMLDeleteRepositoryAction(repository);
	}

	@Override
	public ModifyGlobalRulesAction getModifyGlobalRulesAction(String ruleFile,
			String ruleFileContents) {
		return new XMLModifyGlobalRulesAction(ruleFile, ruleFileContents);
	}

	@Override
	public ModifyPartnerRulesAction getModifyPartnerRulesAction(
			Partner partner, String ruleFileContents) {
		return new XMLModifyPartnerRulesAction(partner, ruleFileContents);
	}

	@Override
	public ShareRepositoryAction getShareRepositoryAction(FederationConfiguration config) {
		return new XMLShareRepositoryAction(config);
	}

	@Override
	public UnshareRepositoryAction getUnshareRepositoryAction(FederationConfiguration config) {
		return new XMLUnshareRepositoryAction(config);
	}

	@Override
	public ResendRepositoryAction getResendRepositoryAction(FederationConfiguration config) {
		return new XMLResendRepositoryAction(config);
	}

	@Override
	public UpdateFederationAction getUpdateFederationAction(
			FederationConfiguration config, String updatedDirection, boolean federationEnabled) {
		return new XMLUpdateFederationAction(config, updatedDirection, federationEnabled);
	}

}
