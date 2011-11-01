/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectAuthorityAccessor class.
 *     
 */
package ca.sciencestudio.security.authz.accessors;

import java.util.List;

import ca.sciencestudio.model.project.ProjectPerson;
import ca.sciencestudio.model.project.dao.ProjectPersonBasicDAO;
import ca.sciencestudio.security.authz.AccessorAuthorites;

/**
 * @author maxweld
 * 
 *
 */
public class ProjectAuthorityAccessor extends FacilityAuthorityAccessor {
	
	private ProjectPersonBasicDAO projectPersonBasicDAO;

	@Override
	public AccessorAuthorites getAuthorities(String user, String gid) {
		
		AccessorAuthorites authorities = getAuthorities(user);
		
		List<ProjectPerson> projectPersons = projectPersonBasicDAO.getAllByProjectGid(gid);
		for(ProjectPerson projectPerson : projectPersons) {
			if(projectPerson.getPersonGid().equalsIgnoreCase(user)) {
				authorities.addProjectAuthority(projectPerson.getRole());
			}
		}
		
		return authorities;
	}

	public ProjectPersonBasicDAO getProjectPersonBasicDAO() {
		return projectPersonBasicDAO;
	}
	public void setProjectPersonBasicDAO(ProjectPersonBasicDAO projectPersonBasicDAO) {
		this.projectPersonBasicDAO = projectPersonBasicDAO;
	}
}
