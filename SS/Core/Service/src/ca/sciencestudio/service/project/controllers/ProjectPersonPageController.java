/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectPersonPageController class.
 *     
 */
package ca.sciencestudio.service.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.model.person.dao.PersonDAO;
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.ProjectPerson;
import ca.sciencestudio.model.project.ProjectRole;
import ca.sciencestudio.model.project.dao.ProjectDAO;
import ca.sciencestudio.model.project.dao.ProjectPersonDAO;
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.controllers.AbstractModelController;
import ca.sciencestudio.service.project.backers.ProjectPersonFormBacker;

/**
 * @author maxweld
 *
 */
@Controller
public class ProjectPersonPageController extends AbstractModelController {
	
	private static final String ERROR_VIEW = "frag/error";
	
	@Autowired
	private PersonDAO personDAO;
	
	@Autowired
	private ProjectDAO projectDAO;
	
	@Autowired
	private ProjectPersonDAO projectPersonDAO;
	
	@RequestMapping(value = "/project/{projectId}/persons.html", method = RequestMethod.GET)
	public String projectPersonsPage(@PathVariable int projectId, ModelMap model) {
		
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		Object team = AuthorityUtil.buildProjectGroupAuthority(projectId);
		
		if(!SecurityUtil.hasAnyAuthority(admin, team)) {
			model.put("error", "Permission denied.");
			return ERROR_VIEW;
		}
		
		Project project = projectDAO.getProjectById(projectId);
		if(project == null) {
			model.put("error", "Project not found.");
			return ERROR_VIEW;
		}
		
		model.put("projectRoleList", ProjectRole.getMarshallableValues(ProjectRole.UNKNOWN));
		model.put("project", project);
		return "frag/projectPersons";
	}
	
	@RequestMapping(value = "project/person/{projectPersonId}.html", method = RequestMethod.GET)
	public String projectPersonPage(@PathVariable int projectPersonId, ModelMap model) {
		
		ProjectPerson projectPerson = projectPersonDAO.getProjectPersonById(projectPersonId);
		if(projectPerson == null) {
			model.put("error", "Project person not found.");
			return ERROR_VIEW;
		}
		
		Person person = personDAO.getPersonByUid(projectPerson.getPersonUid());
		if(person == null) {
			model.put("error", "Person not found.");
			return ERROR_VIEW;
		}
		
		Project project = projectDAO.getProjectById(projectPerson.getProjectId());
		if(project == null) {
			model.put("error", "Project not found.");
			return ERROR_VIEW;
		}
		
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		Object team = AuthorityUtil.buildProjectGroupAuthority(project.getId());
		
		if(!SecurityUtil.hasAnyAuthority(admin, team)) {
			model.put("error", "Permission denied.");
			return ERROR_VIEW;
		}
		
		model.put("projectPersonFormBacker", new ProjectPersonFormBacker(projectPerson, person));
		model.put("projectRoleList", ProjectRole.getMarshallableValues(ProjectRole.UNKNOWN));
		model.put("project", project);
		return "frag/projectPerson";
	}

	public PersonDAO getPersonDAO() {
		return personDAO;
	}
	public void setPersonDAO(PersonDAO personDAO) {
		this.personDAO = personDAO;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}
	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public ProjectPersonDAO getProjectPersonDAO() {
		return projectPersonDAO;
	}
	public void setProjectPersonDAO(ProjectPersonDAO projectPersonDAO) {
		this.projectPersonDAO = projectPersonDAO;
	}
}
