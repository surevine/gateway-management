package com.surevine.gateway.auditing.action;

import models.Destination;
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
	public ShareRepositoryAction getShareRepositoryAction(Repository repository,
			Destination destination) {
		return new ShareRepositoryAction(repository, destination);
	}

	@Override
	public UnshareRepositoryAction getUnshareRepositoryAction(Repository repository,
			Destination destination) {
		return new UnshareRepositoryAction(repository, destination);
	}

	@Override
	public ResendRepositoryAction getResendRepositoryAction(Repository repository,
			Destination destination) {
		return new ResendRepositoryAction(repository, destination);
	}

}
