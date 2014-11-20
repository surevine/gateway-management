package com.surevine.gateway.auditing.action;

import models.Destination;
import models.Project;

/**
 * @author jonnyheavey
 */
public interface AuditActionFactory {

	UserLoginAction getUserLoginAction(String username);

	CreateDestinationAction getCreateDestinationAction(Destination destination);

	UpdateDestinationAction getUpdateDestinationAction(Destination originalDestination, Destination updatedDestination);

	DeleteDestinationAction getDeleteDestinationAction(Destination destination);

	CreateRepositoryAction getCreateRepositoryAction(Project project);

	UpdateRepositoryAction getUpdateRepositoryAction(Project originalProject, Project updatedProject);

	DeleteRepositoryAction getDeleteRepositoryAction(Project project);

	ModifyGlobalRulesAction getModifyGlobalRulesAction(String ruleFile, String ruleFileContents);

	ModifyDestinationRulesAction getModifyDestinationRulesAction(Destination destination, String ruleFileContents);

	ShareRepositoryAction getShareRepositoryAction(Project project, Destination destination);

	UnshareRepositoryAction getUnshareRepositoryAction(Project project, Destination destination);

	ResendRepositoryAction getResendRepositoryAction(Project project, Destination destination);

}
