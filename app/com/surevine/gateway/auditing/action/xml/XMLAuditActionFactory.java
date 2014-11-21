package com.surevine.gateway.auditing.action.xml;

import models.Destination;
import models.Project;

import com.surevine.gateway.auditing.action.AuditActionFactory;
import com.surevine.gateway.auditing.action.CreateDestinationAction;
import com.surevine.gateway.auditing.action.CreateRepositoryAction;
import com.surevine.gateway.auditing.action.DeleteDestinationAction;
import com.surevine.gateway.auditing.action.DeleteRepositoryAction;
import com.surevine.gateway.auditing.action.ModifyDestinationRulesAction;
import com.surevine.gateway.auditing.action.ModifyGlobalRulesAction;
import com.surevine.gateway.auditing.action.ResendRepositoryAction;
import com.surevine.gateway.auditing.action.ShareRepositoryAction;
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
	public CreateRepositoryAction getCreateRepositoryAction(Project project) {
		return new XMLCreateRepositoryAction(project);
	}

	@Override
	public UpdateRepositoryAction getUpdateRepositoryAction(
			Project originalProject, Project updatedProject) {
		return new XMLUpdateRepositoryAction(originalProject, updatedProject);
	}

	@Override
	public DeleteRepositoryAction getDeleteRepositoryAction(Project project) {
		return new XMLDeleteRepositoryAction(project);
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
	public ShareRepositoryAction getShareRepositoryAction(Project project,
			Destination destination) {
		return new XMLShareRepositoryAction(project, destination);
	}

	@Override
	public UnshareRepositoryAction getUnshareRepositoryAction(Project project,
			Destination destination) {
		return new XMLUnshareRepositoryAction(project, destination);
	}

	@Override
	public ResendRepositoryAction getResendRepositoryAction(Project project,
			Destination destination) {
		return new XMLResendRepositoryAction(project, destination);
	}

}
