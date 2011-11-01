/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractProjectAuthzController abstract class.
 *     
 */
package ca.sciencestudio.rest.service.controllers;

import java.util.List;

import ca.sciencestudio.model.Model;
import ca.sciencestudio.model.project.ProjectPerson;
import ca.sciencestudio.model.project.ProjectPerson.Role;
import ca.sciencestudio.model.project.dao.ProjectPersonBasicDAO;

/**
 * @author maxweld
 *
 *
 *
 */
public abstract class AbstractProjectAuthzController<T extends Model> extends AbstractModelAuthzController<T> {
	
	private ProjectPersonBasicDAO projectPersonBasicDAO;
	
	protected List<ProjectPerson> getProjectPersons(String personGid) {
		return projectPersonBasicDAO.getAllByPersonGid(personGid);
	}
	
	protected boolean isProjectMember(String personGid, String projectGid) {
		List<ProjectPerson> projectPersons = getProjectPersons(personGid);
		for(ProjectPerson projectPerson : projectPersons) {
			if(projectPerson.getProjectGid().equals(projectGid)) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean hasProjectRole(String personGid, String projectGid, Role projectRole) {
		return hasProjectRole(personGid, projectGid, projectRole.name());
	}
	
	protected boolean hasProjectRole(String personGid, String projectGid, String projectRole) {
		List<ProjectPerson> projectPersons = getProjectPersons(personGid);
		for(ProjectPerson projectPerson : projectPersons) {
			if(projectPerson.getProjectGid().equals(projectGid) && projectPerson.getRole().equals(projectRole)) {
				return true;
			}
		}
		return false;
	}

	public ProjectPersonBasicDAO getProjectPersonBasicDAO() {
		return projectPersonBasicDAO;
	}
	public void setProjectPersonBasicDAO(ProjectPersonBasicDAO projectPersonBasicDAO) {
		this.projectPersonBasicDAO = projectPersonBasicDAO;
	}
}
