package com.surevine.gateway.auditing.action;

import models.Destination;
import models.Project;

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
	public CreateRepositoryAction getCreateRepositoryAction(Project project) {
		return new CreateRepositoryAction(project);
	}

	@Override
	public UpdateRepositoryAction getUpdateRepositoryAction(
			Project originalProject, Project updatedProject) {
		return new UpdateRepositoryAction(originalProject, updatedProject);
	}

	@Override
	public DeleteRepositoryAction getDeleteRepositoryAction(Project project) {
		return new DeleteRepositoryAction(project);
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
	public ShareRepositoryAction getShareRepositoryAction(Project project,
			Destination destination) {
		return new ShareRepositoryAction(project, destination);
	}

	@Override
	public UnshareRepositoryAction getUnshareRepositoryAction(Project project,
			Destination destination) {
		return new UnshareRepositoryAction(project, destination);
	}

	@Override
	public ResendRepositoryAction getResendRepositoryAction(Project project,
			Destination destination) {
		return new ResendRepositoryAction(project, destination);
	}

}
