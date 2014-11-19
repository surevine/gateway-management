package com.surevine.gateway.auditing;

/**
 * Auditable actions in gateway management console
 *
 * @author jonnyheavey
 *
 */
public enum GatewayAction {

	USER_LOG_IN,

	CREATE_DESTINATION,
	MODIFY_DESTINATION,
	DELETE_DESTINATION,

	CREATE_REPOSITORY,
	MODIFY_REPOSITORY,
	DELETE_REPOSITORY,

	MODIFY_GLOBAL_RULES,
	MODIFY_DESTINATION_RULES,

	SHARE_REPOSITORY,
	UNSHARE_REPOSITORY,
	RESEND_REPOSITORY;

}
