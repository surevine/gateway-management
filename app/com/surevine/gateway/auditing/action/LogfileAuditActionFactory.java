package com.surevine.gateway.auditing.action;

import models.Destination;
import models.OutboundProject;

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
	public CreateRepositoryAction getCreateRepositoryAction(OutboundProject project) {
		return new CreateRepositoryAction(project);
	}

	@Override
	public UpdateRepositoryAction getUpdateRepositoryAction(
			OutboundProject originalProject, OutboundProject updatedProject) {
		return new UpdateRepositoryAction(originalProject, updatedProject);
	}

	@Override
	public DeleteRepositoryAction getDeleteRepositoryAction(OutboundProject project) {
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
	public ShareRepositoryAction getShareRepositoryAction(OutboundProject project,
			Destination destination) {
		return new ShareRepositoryAction(project, destination);
	}

	@Override
	public UnshareRepositoryAction getUnshareRepositoryAction(OutboundProject project,
			Destination destination) {
		return new UnshareRepositoryAction(project, destination);
	}

	@Override
	public ResendRepositoryAction getResendRepositoryAction(OutboundProject project,
			Destination destination) {
		return new ResendRepositoryAction(project, destination);
	}

}
