package com.surevine.gateway.auditing;

import models.Destination;
import models.FederationConfiguration;
import models.Repository;

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

	private Audit() {

	}

	private static AuditActionFactory getAuditActionFactory() {
		if(auditActionFactoryImpl == null) {
			switch(getAuditModeSetting()) {
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
			switch(getAuditModeSetting()) {
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
	 * Retrieve audit mode setting from property file.
	 * Used to conditionally initialise services.
	 *
	 * @return configured audit mode
	 */
	private static AuditMode getAuditModeSetting() {
		String auditMode = ConfigFactory.load().getString("gateway.audit.mode");
		return AuditMode.getMode(auditMode);
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
			Repository repository) {
		return getAuditActionFactory().getCreateRepositoryAction(repository);
	}

	public static UpdateRepositoryAction getUpdateRepositoryAction(
			Repository originalRepo, Repository updatedRepo) {
		return getAuditActionFactory().getUpdateRepositoryAction(originalRepo, updatedRepo);
	}

	public static DeleteRepositoryAction getDeleteRepositoryAction(
			Repository repository) {
		return getAuditActionFactory().getDeleteRepositoryAction(repository);
	}

	public static UnshareRepositoryAction getUnshareRepositoryAction(FederationConfiguration config) {
		return getAuditActionFactory().getUnshareRepositoryAction(config);
	}

	public static ResendRepositoryAction getResendRepositoryAction(FederationConfiguration config) {
		return getAuditActionFactory().getResendRepositoryAction(config);
	}

	public static ShareRepositoryAction getShareRepositoryAction(FederationConfiguration config) {
		return getAuditActionFactory().getShareRepositoryAction(config);
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
