package com.surevine.gateway.auditing;

import models.Destination;
import models.OutboundProject;

import com.surevine.gateway.auditing.action.AuditActionFactory;
import com.surevine.gateway.auditing.action.CreateDestinationAction;
import com.surevine.gateway.auditing.action.CreateRepositoryAction;
import com.surevine.gateway.auditing.action.DeleteDestinationAction;
import com.surevine.gateway.auditing.action.DeleteRepositoryAction;
import com.surevine.gateway.auditing.action.LogfileAuditActionFactory;
import com.surevine.gateway.auditing.action.ModifyDestinationRulesAction;
import com.surevine.gateway.auditing.action.ModifyGlobalRulesAction;
import com.surevine.gateway.auditing.action.ResendRepositoryAction;
import com.surevine.gateway.auditing.action.ShareRepositoryAction;
import com.surevine.gateway.auditing.action.UnshareRepositoryAction;
import com.surevine.gateway.auditing.action.UpdateDestinationAction;
import com.surevine.gateway.auditing.action.UpdateRepositoryAction;
import com.surevine.gateway.auditing.action.UserLoginAction;
import com.surevine.gateway.auditing.action.xml.XMLAuditActionFactory;
import com.typesafe.config.ConfigFactory;

public abstract class Audit {

	private static AuditService auditServiceImpl;
	private static AuditActionFactory auditActionFactoryImpl;

	private static AuditActionFactory getAuditActionFactory() {
		if(auditActionFactoryImpl == null) {
			switch(AuditMode.getMode(ConfigFactory.load().getString("gateway.audit.mode"))) {
				case LOG:
					auditActionFactoryImpl = new LogfileAuditActionFactory();
					break;
				case XML:
					auditActionFactoryImpl = new XMLAuditActionFactory();
					break;
				default:
					throw new AuditServiceException("Could not initialise application auditing. Auditing mode not correctly configured.");
			}
		}
		return auditActionFactoryImpl;
	}

	private static AuditService getAuditService() {
		if(auditServiceImpl == null) {
			switch(AuditMode.getMode(ConfigFactory.load().getString("gateway.audit.mode"))) {
				case LOG:
					auditServiceImpl = LogfileAuditServiceImpl.getInstance();
					break;
				case XML:
					auditServiceImpl = XMLAuditServiceImpl.getInstance();
					break;
				default:
					throw new AuditServiceException("Could not initialise application auditing. Auditing mode not correctly configured.");
			}
		}
		return auditServiceImpl;
	}

	/**
	 * Audit a system event
	 * @param date
	 * @param event event to be audited
	 */
	public static void audit(AuditEvent event) {
		getAuditService().audit(event);
	}

	public static CreateDestinationAction getCreateDestinationAction(Destination destination) {
		return getAuditActionFactory().getCreateDestinationAction(destination);
	}

	public static UpdateDestinationAction getUpdateDestinationAction(
			Destination originalDestination, Destination updatedDestination) {
		return getAuditActionFactory().getUpdateDestinationAction(originalDestination, updatedDestination);
	}

	public static DeleteDestinationAction getDeleteDestinationAction(
			Destination destination) {
		return getAuditActionFactory().getDeleteDestinationAction(destination);
	}

	public static ModifyDestinationRulesAction getModifyDestinationRulesAction(
			Destination destination, String newRuleFileContent) {
		return getAuditActionFactory().getModifyDestinationRulesAction(destination, newRuleFileContent);
	}

	public static ModifyGlobalRulesAction getModifyGlobalRulesAction(
			String slug, String newRuleFileContent) {
		return getAuditActionFactory().getModifyGlobalRulesAction(slug, newRuleFileContent);
	}

	public static CreateRepositoryAction getCreateRepositoryAction(
			OutboundProject project) {
		return getAuditActionFactory().getCreateRepositoryAction(project);
	}

	public static UpdateRepositoryAction getUpdateRepositoryAction(
			OutboundProject originalProject, OutboundProject updatedProject) {
		return getAuditActionFactory().getUpdateRepositoryAction(originalProject, updatedProject);
	}

	public static DeleteRepositoryAction getDeleteRepositoryAction(
			OutboundProject project) {
		return getAuditActionFactory().getDeleteRepositoryAction(project);
	}

	public static UnshareRepositoryAction getUnshareRepositoryAction(
			OutboundProject project, Destination destination) {
		return getAuditActionFactory().getUnshareRepositoryAction(project, destination);
	}

	public static ResendRepositoryAction getResendRepositoryAction(
			OutboundProject project, Destination destination) {
		return getAuditActionFactory().getResendRepositoryAction(project, destination);
	}

	public static ShareRepositoryAction getShareRepositoryAction(
			OutboundProject project, Destination destination) {
		return getAuditActionFactory().getShareRepositoryAction(project, destination);
	}

	public static UserLoginAction getUserLoginAction(String authenticatedUser) {
		return getAuditActionFactory().getUserLoginAction(authenticatedUser);
	}

	/**
	 * Explicitly set an audit service (ignoring configuration).
	 * Primarily used for unit test mocking.
	 *
	 * @param auditService
	 */
	public static void setAuditService(AuditService auditService) {
		auditServiceImpl = auditService;
	}

	/**
	 * Explicitly set an audit action factory (ignoring configuration).
	 * Primarily used for unit test mocking.
	 *
	 * @param auditActionFactory
	 */
	public static void setAuditActionFactory(AuditActionFactory auditActionFactory) {
		auditActionFactoryImpl = auditActionFactory;
	}

}
