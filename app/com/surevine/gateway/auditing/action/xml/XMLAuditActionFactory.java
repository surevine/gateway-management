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


public class XMLAuditActionFactory implements AuditActionFactory {

	@Override
	public CreateDestinationAction getCreateDestinationAction(Destination destination) {
		return new XMLCreateDestinationAction(destination);
	}

	@Override
	public UserLoginAction getUserLoginAction(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpdateDestinationAction getUpdateDestinationAction(
			Destination originalDestination, Destination updatedDestination) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeleteDestinationAction getDeleteDestinationAction(
			Destination destination) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CreateRepositoryAction getCreateRepositoryAction(Project project) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpdateRepositoryAction getUpdateRepositoryAction(
			Project originalProject, Project updatedProject) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeleteRepositoryAction getDeleteRepositoryAction(Project project) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModifyGlobalRulesAction getModifyGlobalRulesAction(String ruleFile,
			String ruleFileContents) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModifyDestinationRulesAction getModifyDestinationRulesAction(
			Destination destination, String ruleFileContents) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ShareRepositoryAction getShareRepositoryAction(Project project,
			Destination destination) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UnshareRepositoryAction getUnshareRepositoryAction(Project project,
			Destination destination) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResendRepositoryAction getResendRepositoryAction(Project project,
			Destination destination) {
		// TODO Auto-generated method stub
		return null;
	}

}
