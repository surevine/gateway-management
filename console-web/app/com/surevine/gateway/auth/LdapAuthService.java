package com.surevine.gateway.auth;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.typesafe.config.ConfigFactory;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;

import play.Logger;
import play.mvc.Http.Context;
import play.mvc.Http.Request;

/**
 * Interacts with LDAP directory to authorise current user.
 * @author jonnyheavey
 */
public class LdapAuthService implements AuthService {

	private static LdapAuthService _instance = null;

	private static String LDAP_HOST = ConfigFactory.load().getString("ldap.host");
	private static int LDAP_PORT = ConfigFactory.load().getInt("ldap.port");
	private static String LDAP_ADMIN_DN = ConfigFactory.load().getString("ldap.admin.dn");
	private static String LDAP_ADMIN_PASSWORD = ConfigFactory.load().getString("ldap.admin.password");
	private static String LDAP_AUTHORISED_GROUP_DN = ConfigFactory.load().getString("ldap.authorised.group.dn");
	private static String LDAP_GROUP_MEMBER_ATTRIBUTE = ConfigFactory.load().getString("ldap.group.member.attribute");
	private static String REMOTE_USER_HEADER =  ConfigFactory.load().getString("remote.user.header");

	private static String DN_PATTERN = "(?:(?:[^\\\\]\\\\(?:\\\\\\\\)*\\/)|[^\\/])+";

	private LdapAuthService() {

	}

	public static LdapAuthService getInstance() {
		if(_instance == null) {
			_instance = new LdapAuthService();
		}
		return _instance;
	}


	@Override
	public String getAuthenticatedUsername(Context ctx) throws AuthServiceException {
		String userDN = getUserDN(ctx.request());
	    if(isUserGroupMember(userDN, LDAP_AUTHORISED_GROUP_DN)) {
	    	return parseUsername(userDN);
	    }
		return null;
	}

	/**
	 * Parses user DN from request header.
	 * (Header provided by proxy)
	 * @param request Current request
	 * @return
	 * @throws AuthServiceException
	 */
	private String getUserDN(Request request) throws AuthServiceException {

		String remoteUser = request.getHeader(REMOTE_USER_HEADER);
		if(remoteUser == null) {
			throw new AuthServiceException(String.format("%s header not set in request.", REMOTE_USER_HEADER));
		}

		List<String> dnParts = new ArrayList<String>();
		Matcher m = Pattern.compile(DN_PATTERN).matcher(remoteUser);
		while (m.find()) {
			dnParts.add(m.group());
		}

		Collections.reverse(dnParts);

		StringBuilder parsedDN = new StringBuilder();
		Iterator<String> it = dnParts.iterator();
		while(it.hasNext()) {
			String dnPart = it.next();
			parsedDN.append(dnPart);
			if(it.hasNext()) {
				parsedDN.append(",");
			}
		}

		return parsedDN.toString();
	}

	/**
	 * Determines whether current user is member of authorised group
	 * @param userDN current user DN (to check authorisation)
	 * @param groupDN DN of group containing all authorised users
	 * @return
	 * @throws AuthServiceException
	 */
	private boolean isUserGroupMember(String userDN, String groupDN) throws AuthServiceException {

		boolean isMember = false;

		try {

			Entry groupEntry = getConnection().getEntry(groupDN);
			if(groupEntry == null) {
				throw new AuthServiceException(
						String.format("Couldn't find the configured authorised users group (%s) in LDAP directory.", groupDN));
			}

			String[] members = groupEntry.getAttribute(LDAP_GROUP_MEMBER_ATTRIBUTE).getValues();

			for(String member: members) {
				if(member.equalsIgnoreCase(userDN)) {
					isMember = true;
					break;
				}
			}

		} catch (LDAPException | GeneralSecurityException e) {
			Logger.error("Failed to determine group membership for user.", e);
		}

		return isMember;
	}

	/**
	 * Establish connection to LDAP directory.
	 * @return
	 * @throws LDAPException
	 * @throws GeneralSecurityException
	 * @throws AuthServiceException
	 */
	private LDAPConnection getConnection() throws LDAPException, GeneralSecurityException, AuthServiceException {

		LDAPConnection connection = new LDAPConnection(LDAP_HOST,
														LDAP_PORT,
														LDAP_ADMIN_DN,
														LDAP_ADMIN_PASSWORD);

		if(!connection.isConnected()) {
			throw new AuthServiceException(
					String.format("Could not connect to LDAP server (%s:%s)",
									LDAP_HOST,
									LDAP_PORT));
		}

		return connection;
	}

	/**
	 * Parses username from LDAP DN
	 * @param userDN DN to parse
	 * @return
	 */
	private String parseUsername(String userDN) {
		// Username starts after first equals
		int start = userDN.indexOf("=") + 1;
		// Username ends with first comma
		int end = userDN.indexOf(",");
		return userDN.substring(start, end);
	}

}
