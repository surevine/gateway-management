package com.surevine.gateway.auditing.action;

import models.Destination;
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
	public CreateDestinationAction getCreateDestinationAction(Destination destination) {
		return new CreateDestinationAction(destination);
	}

	@Override
	public UserLoginAction getUserLoginAction(String username) {
		return new UserLoginAction(username);
	}

	@Override
	public UpdateDestinationAction getUpdateDestinationAction(
			Destination originalDestination, Destination updatedDestination) {
		return new UpdateDestinationAction(originalDestination, originalDestination);
	}

	@Override
	public DeleteDestinationAction getDeleteDestinationAction(
			Destination destination) {
		return new DeleteDestinationAction(destination);
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
	public ModifyDestinationRulesAction getModifyDestinationRulesAction(
			Destination destination, String ruleFileContents) {
		return new ModifyDestinationRulesAction(destination, ruleFileContents);
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
	public ToggleFederationAction getToggleFederationAction(
			Repository repository, Destination destination, String direction,
			boolean enabled) {
		return new ToggleFederationAction(repository, destination, direction, enabled);
	}

}
