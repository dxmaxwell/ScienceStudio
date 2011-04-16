/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectPersonFormController class.
 *     
 */
package ca.sciencestudio.service.project.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
import ca.sciencestudio.service.project.validators.ProjectPersonFormValidator;
import ca.sciencestudio.service.utilities.ModelPathUtils;
import ca.sciencestudio.util.web.BindAndValidateUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class ProjectPersonFormController extends AbstractModelController {

	@Autowired
	private PersonDAO personDAO;
	
	@Autowired
	private ProjectDAO projectDAO;
	
	@Autowired
	private ProjectPersonDAO projectPersonDAO;
	
	@Autowired
	private ProjectPersonFormValidator projectPersonFormValidator;
	
	@RequestMapping(value = "/project/{projectId}/persons/form/add.{format}", method = RequestMethod.POST)
	public String projectPersonFormAdd(@PathVariable int projectId, @PathVariable String format, HttpServletRequest request, ModelMap model) {
		
		ProjectPersonFormBacker projectPersonFormBacker = new ProjectPersonFormBacker(projectId, ProjectRole.OBSERVER);
		BindException errors = BindAndValidateUtils.buildBindException(projectPersonFormBacker);
		model.put("errors", errors);
		
		String responseView = "response-" + format;
		
		Project project = projectDAO.getProjectById(projectId);
		if(project == null) {
			errors.reject("project.notfound", "Project not found.");
			return responseView;
		}
		
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		Object exptr = AuthorityUtil.buildProjectExperimenterAuthority(projectId);
		
		if(!SecurityUtil.hasAnyAuthority(admin, exptr)) {
			errors.reject("permission.denied", "Permission denied.");
			return responseView;
		}
		
		errors = BindAndValidateUtils.bindAndValidate(projectPersonFormBacker, request, projectPersonFormValidator);
		model.put("errors", errors);
		
		if(errors.hasErrors()) {
			return responseView;
		}
		
		ProjectPerson projectPerson = 
			projectPersonDAO.getProjectPersonByProjectIdAndPersonUid(projectId, projectPersonFormBacker.getPersonUid());

		if(projectPerson != null) {
			errors.rejectValue("personUid", "", "Person is already a team member.");
			return responseView;
		}
		
		if(!SecurityUtil.hasAuthority(admin) && (projectPersonFormBacker.getProjectRole() != ProjectRole.OBSERVER)) {
			errors.rejectValue("projectRole", "", "Only permitted to add OBSERVERS.");
			return responseView;
		}
		
		projectPerson = projectPersonFormBacker.createProjectPerson(projectPersonDAO);
		projectPersonDAO.addProjectPerson(projectPerson);
		
		Map<String,Object> response = new HashMap<String,Object>();
		response.put("viewUrl", getModelPath(request) + ModelPathUtils.getProjectPersonPath(projectPerson.getId(), ".html"));
		
		model.put("response", response);
		return responseView;
	}

	@RequestMapping(value = "/project/person/{projectPersonId}/form/edit.{format}", method = RequestMethod.POST)
	public String projectPersonEdit(@PathVariable int projectPersonId, @PathVariable String format, 
															HttpServletRequest request, ModelMap model) {
		
		BindException errors = BindAndValidateUtils.buildBindException();
		model.put("errors", errors);
		
		String responseView = "response-" + format;
		
		ProjectPerson projectPerson = projectPersonDAO.getProjectPersonById(projectPersonId);
		if(projectPerson == null) {
			errors.reject("projectperson.notfound", "Project person not found.");
			return responseView;
		}
		
		Person person = personDAO.getPersonByUid(projectPerson.getPersonUid());
		if(person == null) {
			errors.reject("person.notfound", "Person not found.");
			return responseView;
		}
		
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		
		if(!SecurityUtil.hasAuthority(admin)) {
			errors.reject("permission.denied", "Permission denied.");
			return responseView;
		}
		
		ProjectPersonFormBacker projectPersonFormBacker = new ProjectPersonFormBacker(projectPerson, person);
		
		errors = BindAndValidateUtils.bindAndValidate(projectPersonFormBacker, request, projectPersonFormValidator);
		model.put("errors", errors);
		
		if(errors.hasErrors()) {
			return responseView;
		}
		
		// Ensure only project role modified //
		projectPerson.setProjectRole(projectPersonFormBacker.getProjectRole());
		///////////////////////////////////////
		
		projectPersonDAO.editProjectPerson(projectPerson);
		
		Map<String,Object> response = new HashMap<String,Object>();
		response.put("message", "Person saved.");
		
		model.put("response", response);
		return responseView;
	}
	
	@RequestMapping(value = "/project/{projectId}/persons/form/query.{format}", method = RequestMethod.POST)
	public String projectPersonQuery(@PathVariable int projectId, @PathVariable String format, @RequestParam String query, ModelMap model) {
		
		List<ProjectPersonFormBacker> projectPersonList = new ArrayList<ProjectPersonFormBacker>();
		
		query = query.toLowerCase();
		
		if((query != null) && (query.length() > 0)) {
			List<Person> personList = personDAO.getPersonList();
			
			for(Person person : personList) {
				String firstName = person.getFirstName().toLowerCase();
				String lastName = person.getLastName().toLowerCase();
				if(firstName.startsWith(query) || lastName.startsWith(query)) {
					projectPersonList.add(new ProjectPersonFormBacker(projectId, ProjectRole.OBSERVER, person));
				}
			}
		}
		
		model.put("response", projectPersonList);
		return "response-" + format;
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

	public ProjectPersonFormValidator getProjectPersonFormValidator() {
		return projectPersonFormValidator;
	}
	public void setProjectPersonFormValidator(ProjectPersonFormValidator projectPersonFormValidator) {
		this.projectPersonFormValidator = projectPersonFormValidator;
	}
}
