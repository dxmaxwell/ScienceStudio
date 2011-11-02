/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AuthorityUtil class.
 *     
 */
package ca.sciencestudio.security.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.sciencestudio.login.model.LoginGroup;
import ca.sciencestudio.login.model.LoginRole;
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.ProjectPerson;
import ca.sciencestudio.model.project.ProjectPerson.Role;

/**
 * @author maxweld
 *
 */
@Deprecated
public abstract class AuthorityUtil {
	
	public static final String FALLBACK_SANITIZED_AUTHORITY = "";
	public static final String SANATIZE_WHITE_SPACE_REPACEMENT = "_";
	public static final Pattern SANATIZE_WHITE_SPACE_PATTERN = Pattern.compile("\\s+");

	public static final String ROLE_AUTHORITY_PREFIX = "ROLE_";
	public static final String GROUP_AUTHORITY_PREFIX = "GROUP_";
	public static final String PROJECT_ROLE_AUTHORITY_PREFIX = "PROJECT_ROLE_";
	public static final String PROJECT_GROUP_AUTHORITY_PREFIX = "PROJECT_GROUP_TEAM_";
	
	public static final Object ROLE_EVERYBODY = buildRoleAuthority("EVERYBODY");
	public static final Object ROLE_ADMIN_DATA = buildRoleAuthority("ADMIN_DATA");
	public static final Object ROLE_ADMIN_PROJECTS = buildRoleAuthority("ADMIN_PROJECTS");

	public static final Object GROUP_EVERYBODY = buildGroupAuthority("EVERYBODY");
	public static final Object GROUP_ADMINISTRATORS = buildGroupAuthority("ADMINISTRATORS");
	
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
	
	// Utilities for building project role authorities. //
	public static Object buildProjectObserverAuthority(String projectGid) {
		return buildProjectRoleAuthority(projectGid, Role.COLLABORATOR.name());
	}
	
	public static Object buildProjectExperimenterAuthority(String projectGid) {
		return buildProjectRoleAuthority(projectGid, Role.RESEARCHER.name());
	}
	
	public static Object buildProjectRoleAuthority(String projectGid, String projectRoleName) {
		return PROJECT_ROLE_AUTHORITY_PREFIX + sanitize(projectRoleName) + "_" + projectGid;
	}
	
	public static Object buildProjectRoleAuthority(ProjectPerson projectPerson) {
		return buildProjectRoleAuthority(projectPerson.getProjectGid(), projectPerson.getRole().name());
	}
	
	public static Object buildProjectRoleAuthority(Project project, Role role) {
		return buildProjectRoleAuthority(project.getGid(), role.name());
	}
	
	// Utilities for building project group authorities. //
	public static Object buildProjectGroupAuthority(String projectGid) {
		return  PROJECT_GROUP_AUTHORITY_PREFIX + projectGid;
	}
	
	public static Object buildProjectGroupAuthority(Project project) {
		return buildProjectGroupAuthority(project.getGid());
	}
	
	public static Object buildProjectGroupAuthority(ProjectPerson projectPerson) {
		return buildProjectGroupAuthority(projectPerson.getProjectGid());
	}
	
	public static String sanitize(Object authority) {
		if(authority == null) {
			return FALLBACK_SANITIZED_AUTHORITY;
		}
		
		String trimmedAuthority = authority.toString().trim();
		Matcher whiteSpaceMatcher = SANATIZE_WHITE_SPACE_PATTERN.matcher(trimmedAuthority);
		return whiteSpaceMatcher.replaceAll(SANATIZE_WHITE_SPACE_REPACEMENT).toUpperCase();
	}
}
