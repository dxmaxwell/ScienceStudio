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
import ca.sciencestudio.model.project.ProjectRole;

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
	public static Object buildProjectObserverAuthority(int projectId) {
		return buildProjectRoleAuthority(projectId, ProjectRole.OBSERVER.name());
	}
	
	public static Object buildProjectExperimenterAuthority(int projectId) {
		return buildProjectRoleAuthority(projectId, ProjectRole.EXPERIMENTER.name());
	}
	
	public static Object buildProjectRoleAuthority(int projectId, String projectRoleName) {
		return PROJECT_ROLE_AUTHORITY_PREFIX + sanitize(projectRoleName) + "_" + projectId;
	}
	
	public static Object buildProjectRoleAuthority(ProjectPerson projectPerson) {
		return buildProjectRoleAuthority(projectPerson.getProjectId(), projectPerson.getProjectRole().name());
	}
	
	public static Object buildProjectRoleAuthority(Project project, ProjectRole projectRole) {
		return buildProjectRoleAuthority(project.getId(), projectRole.name());
	}
	
	// Utilities for building project group authorities. //
	public static Object buildProjectGroupAuthority(int projectId) {
		return  PROJECT_GROUP_AUTHORITY_PREFIX + projectId;
	}
	
	public static Object buildProjectGroupAuthority(Project project) {
		return buildProjectGroupAuthority(project.getId());
	}
	
	public static Object buildProjectGroupAuthority(ProjectPerson projectPerson) {
		return buildProjectGroupAuthority(projectPerson.getProjectId());
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
