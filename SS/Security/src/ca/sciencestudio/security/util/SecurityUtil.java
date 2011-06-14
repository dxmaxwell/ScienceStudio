/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    SecurityUtil abstract class.
 *     
 */
package ca.sciencestudio.security.util;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.sciencestudio.login.model.LoginGroup;
import ca.sciencestudio.login.model.LoginRole;
import ca.sciencestudio.model.Person;
import ca.sciencestudio.model.ProjectPerson;
import ca.sciencestudio.model.dao.ProjectPersonDAO;

/**
 * @author maxweld
 *
 */
public abstract class SecurityUtil {
	
	private static ProjectPersonDAO PROJECT_PERSON_DAO;
	
	private static SecurityStrategy SECURITY_STRATEGY;
	
	public static final String FALLBACK_SANITIZED_AUTHORITY = "";
	public static final String SANATIZE_WHITE_SPACE_REPACEMENT = "_";
	public static final Pattern SANATIZE_WHITE_SPACE_PATTERN = Pattern.compile("\\s+");
	
	public static final String ROLE_AUTHORITY_PREFIX = "ROLE_";
	public static final String GROUP_AUTHORITY_PREFIX = "GROUP_";
	//public static final String PROJECT_ROLE_AUTHORITY_PREFIX = "PROJECT_ROLE_";
	//public static final String PROJECT_GROUP_AUTHORITY_PREFIX = "PROJECT_GROUP_TEAM_";
	
	public static enum ROLE {
		EVERYBODY, ADMIN_DATA, ADMIN_PROJECTS;

		@Override
		public String toString() {
			return ROLE_AUTHORITY_PREFIX + name();
		}
	}
	
	public static enum GROUP {
		EVERYBODY, ADMINISTRATORS;

		@Override
		public String toString() {
			return GROUP_AUTHORITY_PREFIX + name();
		}
	}
	
	
//	static {	
//		try {
//			SECURITY_STRATEGY = new SpringSecurityStrategy();
//		}
//		catch(Exception e) {
//			LOGGER.warn("Cannot instantiate new SpringSecurityStategy", e);
//		}
//	}
	
	
	// Utilities for building role authorities. //
	public static Object buildRoleAuthority(Object roleName) {
		return ROLE_AUTHORITY_PREFIX + sanitize(roleName);
	}
	
	public static Object buildRoleAuthority(LoginRole loginRole) {
		return buildRoleAuthority(loginRole.getName());
	}

	// Utilities for building group authorities. //
	public static Object buildGroupAuthority(String groupName) {
		return GROUP_AUTHORITY_PREFIX + sanitize(groupName);
	}
	
	public static Object buildGroupAuthority(LoginGroup loginGroup) {
		return buildGroupAuthority(loginGroup.getName());
	}
	
	public static String sanitize(Object authority) {
		if(authority == null) {
			return FALLBACK_SANITIZED_AUTHORITY;
		}
		
		String trimmedAuthority = authority.toString().trim();
		Matcher whiteSpaceMatcher = SANATIZE_WHITE_SPACE_PATTERN.matcher(trimmedAuthority);
		return whiteSpaceMatcher.replaceAll(SANATIZE_WHITE_SPACE_REPACEMENT).toUpperCase();
	}
	
	public static String getUsername() {
		return SECURITY_STRATEGY.getUsername();
	}

	public static boolean isAuthenticated() {
		return SECURITY_STRATEGY.isAuthenticated();
	}
	
	public static Person getPerson() {
		return SECURITY_STRATEGY.getPerson();
	}
	
	public static boolean hasAuthority(Object authority) {
		return SECURITY_STRATEGY.hasAuthority(authority);
	}
	
	public static boolean hasAnyAuthority(Object... authority) {
		return SECURITY_STRATEGY.hasAnyAuthority(authority);
	}
	
	public static boolean hasAnyAuthority(Collection<?> authorities) {
		return SECURITY_STRATEGY.hasAnyAuthority(authorities);
	}
	
	public static boolean hasAllAuthorities(String... authorities) {
		return SECURITY_STRATEGY.hasAllAuthorities(authorities);
	}
	
	public static boolean hasAllAuthorities(Collection<?> authorities) {
		return SECURITY_STRATEGY.hasAllAuthorities(authorities);
	}
	
	public static boolean hasAnyProjectRole(Object projectGid) {
		return !PROJECT_PERSON_DAO.getAllByProjectGidAndPersonGid(projectGid, getPerson().getGid()).isEmpty();
	}
	
	public static boolean hasProjectRole(Object projectGid, String role) {
		for(ProjectPerson pp : PROJECT_PERSON_DAO.getAllByProjectGidAndPersonGid(projectGid, getPerson().getGid())) {
			if(pp.getRole().equalsIgnoreCase(role)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasProjectRole(Object projectGid, ProjectPerson.Role role) {
		return hasProjectRole(projectGid, role.name());
	}
	
	//public boolean hasAnySessionRole(Object sessionGid);
	//public boolean hasSessionRole(Object sessionGid, String role);
	//public boolean hasSessionRole(Object sessionGid, SessionPerson.Role role);
	
	public static ProjectPersonDAO getProjectPersonDAO() {
		return PROJECT_PERSON_DAO;
	}
	public static void setProjectPersonDAO(ProjectPersonDAO projectPersonDAO) {
		PROJECT_PERSON_DAO = projectPersonDAO;
	}

	public static SecurityStrategy getSecurityStrategy() {
		return SECURITY_STRATEGY;
	}
	public static void setSecurityStrategy(SecurityStrategy securityStrategy) {
		SECURITY_STRATEGY = securityStrategy;
	}
}
