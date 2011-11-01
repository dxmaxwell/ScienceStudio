/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectPersonPageController class.
 *     
 */
package ca.sciencestudio.service.project.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sciencestudio.model.Permissions;
import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.model.person.dao.PersonAuthzDAO;
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.ProjectPerson;
import ca.sciencestudio.model.project.dao.ProjectAuthzDAO;
import ca.sciencestudio.model.project.dao.ProjectPersonAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.controllers.AbstractModelController;
import ca.sciencestudio.service.project.backers.ProjectPersonFormBacker;
import ca.sciencestudio.service.utilities.ModelPathUtils;
import ca.sciencestudio.util.web.EnumToOptionUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class ProjectPersonPageController extends AbstractModelController {
		
	private PersonAuthzDAO personAuthzDAO;

	private ProjectAuthzDAO projectAuthzDAO;

	private ProjectPersonAuthzDAO projectPersonAuthzDAO;
	
	@RequestMapping(value = ModelPathUtils.PROJECT_PERSON_PATH + ".html", params = "project")
	public String projectPersonsPage(@RequestParam("project") String projectGid, ModelMap model) throws Exception {
		
		String user = SecurityUtil.getPersonGid();
		
		Data<Permissions> dataPermissions = projectPersonAuthzDAO.permissions(user);
		
		Project project = projectAuthzDAO.get(user, projectGid).get();		
		if(project == null) {
			model.put("error", "Project not found.");
			return ERROR_VIEW;
		}
		
		Permissions permissions = dataPermissions.get();
		if(permissions == null) {
			model.put("error", "Permissions not found.");
			return ERROR_VIEW;
		}
		
		model.put("projectRoleOptions", EnumToOptionUtils.toList(ProjectPerson.Role.values()));
		model.put("permissions", permissions);
		model.put("project", project);
		return "frag/projectPersons";
	}
	
	@RequestMapping(value = ModelPathUtils.PROJECT_PERSON_PATH + "/{projectPersonGid}.html", method = RequestMethod.GET)
	public String projectPersonPage(@PathVariable String projectPersonGid, ModelMap model) throws Exception {
		
		String user = SecurityUtil.getPersonGid();
		
		Data<Permissions> dataPermissions = projectPersonAuthzDAO.permissions(user, projectPersonGid);
		
		ProjectPerson projectPerson = projectPersonAuthzDAO.get(user, projectPersonGid).get();
		if(projectPerson == null) {
			model.put("error", "Project person not found.");
			return ERROR_VIEW;
		}
		
		Data<Person> dataPerson = personAuthzDAO.get(user, projectPerson.getPersonGid());
		
		Data<Project> dataProject = projectAuthzDAO.get(user, projectPerson.getProjectGid());
		
		Permissions permissions = dataPermissions.get();
		if(permissions == null) {
			model.put("error", "Permissions not found.");
			return ERROR_VIEW;
		}
		
		Person person = dataPerson.get();
		if(person == null) {
			model.put("error", "Person not found.");
			return ERROR_VIEW;
		}
		
		Project project = dataProject.get();
		if(project == null) {
			model.put("error", "Project not found.");
			return ERROR_VIEW;
		}
				
		model.put("projectPerson", new ProjectPersonFormBacker(projectPerson, person));
		model.put("projectRoleOptions", EnumToOptionUtils.toList(ProjectPerson.Role.values()));
		model.put("permissions", permissions);
		model.put("project", project);
		return "frag/projectPerson";
	}

	public PersonAuthzDAO getPersonAuthzDAO() {
		return personAuthzDAO;
	}
	public void setPersonAuthzDAO(PersonAuthzDAO personAuthzDAO) {
		this.personAuthzDAO = personAuthzDAO;
	}

	public ProjectAuthzDAO getProjectAuthzDAO() {
		return projectAuthzDAO;
	}
	public void setProjectAuthzDAO(ProjectAuthzDAO projectAuthzDAO) {
		this.projectAuthzDAO = projectAuthzDAO;
	}

	public ProjectPersonAuthzDAO getProjectPersonAuthzDAO() {
		return projectPersonAuthzDAO;
	}
	public void setProjectPersonAuthzDAO(ProjectPersonAuthzDAO projectPersonAuthzDAO) {
		this.projectPersonAuthzDAO = projectPersonAuthzDAO;
	}
}
