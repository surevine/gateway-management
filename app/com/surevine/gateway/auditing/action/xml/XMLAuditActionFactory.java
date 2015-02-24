package com.surevine.gateway.auditing.action.xml;

import models.Destination;
import models.FederationConfiguration;
import models.Repository;

import com.surevine.gateway.auditing.action.AuditActionFactory;
import com.surevine.gateway.auditing.action.CreateDestinationAction;
import com.surevine.gateway.auditing.action.CreateRepositoryAction;
import com.surevine.gateway.auditing.action.DeleteDestinationAction;
import com.surevine.gateway.auditing.action.DeleteRepositoryAction;
import com.surevine.gateway.auditing.action.ModifyDestinationRulesAction;
import com.surevine.gateway.auditing.action.ModifyGlobalRulesAction;
import com.surevine.gateway.auditing.action.ResendRepositoryAction;
import com.surevine.gateway.auditing.action.ShareRepositoryAction;
import com.surevine.gateway.auditing.action.ToggleFederationAction;
import com.surevine.gateway.auditing.action.UnshareRepositoryAction;
import com.surevine.gateway.auditing.action.UpdateDestinationAction;
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
	public CreateDestinationAction getCreateDestinationAction(Destination destination) {
		return new XMLCreateDestinationAction(destination);
	}

	@Override
	public UserLoginAction getUserLoginAction(String username) {
		return new XMLUserLoginAction(username);
	}

	@Override
	public UpdateDestinationAction getUpdateDestinationAction(
			Destination originalDestination, Destination updatedDestination) {
		return new XMLUpdateDestinationAction(originalDestination, updatedDestination);
	}

	@Override
	public DeleteDestinationAction getDeleteDestinationAction(
			Destination destination) {
		return new XMLDeleteDestinationAction(destination);
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
	public ModifyDestinationRulesAction getModifyDestinationRulesAction(
			Destination destination, String ruleFileContents) {
		return new XMLModifyDestinationRulesAction(destination, ruleFileContents);
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
	public ToggleFederationAction getToggleFederationAction(
			Repository repository, Destination destination, String direction,
			boolean enabled) {
		return new ToggleFederationAction(repository, destination, direction, enabled);
	}

}
