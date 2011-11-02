/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectPersonFormController class.
 *     
 */
package ca.sciencestudio.service.project.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.model.person.dao.PersonAuthzDAO;
import ca.sciencestudio.model.project.ProjectPerson.Role;

import ca.sciencestudio.model.project.dao.ProjectPersonAuthzDAO;
import ca.sciencestudio.service.controllers.AbstractModelController;
import ca.sciencestudio.service.project.backers.ProjectPersonFormBacker;
import ca.sciencestudio.service.utilities.ModelPathUtils;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.exceptions.AuthorizationException;
import ca.sciencestudio.util.rest.AddResult;
import ca.sciencestudio.util.rest.EditResult;
import ca.sciencestudio.util.rest.RemoveResult;
import ca.sciencestudio.util.web.FormResponseMap;

/**
 * @author maxweld
 *
 */
@Controller
public class ProjectPersonFormController extends AbstractModelController {
	
	private PersonAuthzDAO personAuthzDAO;
		
	private ProjectPersonAuthzDAO projectPersonAuthzDAO;

	@ResponseBody
	@RequestMapping(value = ModelPathUtils.PROJECT_PERSON_PATH + "/form/search*", method = RequestMethod.POST)
	public List<ProjectPersonFormBacker> projectPersonFormSearch(@RequestParam String name, @RequestParam String project) {
		
		String user = SecurityUtil.getPersonGid();

		List<ProjectPersonFormBacker> projectPersons = new ArrayList<ProjectPersonFormBacker>();
		
		List<Person> persons = personAuthzDAO.getAllByName(user, name).get();
		for(Person person : persons) {
			projectPersons.add(new ProjectPersonFormBacker(project, Role.COLLABORATOR, person));
		}
		
		return projectPersons;
	}
	
	@ResponseBody
	@RequestMapping(value = ModelPathUtils.PROJECT_PERSON_PATH + "/form/add*", method = RequestMethod.POST)
	public FormResponseMap projectPersonFormAdd(ProjectPersonFormBacker projectPerson, Errors errors) {
		// Bind errors are intentionally ignored, however the argument must be 
		// present to avoid automatic delegation to the exception handler.
				
		String user = SecurityUtil.getPersonGid();
		
		AddResult result = projectPersonAuthzDAO.add(user, projectPerson).get();
		
		FormResponseMap response = new FormResponseMap(ProjectPersonFormBacker.transformResult(result));
		
		if(response.isSuccess()) {				
			response.put("viewUrl", ModelPathUtils.getModelProjectPersonPath("/", projectPerson.getGid(), ".html"));
		}
		
		return response;
	}

	@ResponseBody
	@RequestMapping(value = ModelPathUtils.PROJECT_PERSON_PATH + "/form/edit*", method = RequestMethod.POST)
	public FormResponseMap projectPersonFormEdit(ProjectPersonFormBacker projectPerson, Errors errors) {
		// Bind errors are intentionally ignored, however the argument must be 
		// present to avoid automatic delegation to the exception handler.
		
		String user = SecurityUtil.getPersonGid();
		
		EditResult result = projectPersonAuthzDAO.edit(user, projectPerson).get();
		
		FormResponseMap response = new FormResponseMap(ProjectPersonFormBacker.transformResult(result));
		
		if(response.isSuccess()) {
			response.put("message", "Person Saved");
		}
		
		return response;
	}
	
	@ResponseBody
	@RequestMapping(value = ModelPathUtils.PROJECT_PERSON_PATH + "/form/remove*", method = RequestMethod.POST)
	public FormResponseMap projectPersonFormRemove(@RequestParam String gid) {
		
		String user = SecurityUtil.getPersonGid();
		
		RemoveResult result;
		try {
			result = projectPersonAuthzDAO.remove(user, gid).get();
		}
		catch(AuthorizationException e) {
			return new FormResponseMap(false, "Not Permitted");
		}
		
		return new FormResponseMap(result);
	}

	public PersonAuthzDAO getPersonAuthzDAO() {
		return personAuthzDAO;
	}
	public void setPersonAuthzDAO(PersonAuthzDAO personAuthzDAO) {
		this.personAuthzDAO = personAuthzDAO;
	}

	public ProjectPersonAuthzDAO getProjectPersonAuthzDAO() {
		return projectPersonAuthzDAO;
	}
	public void setProjectPersonAuthzDAO(ProjectPersonAuthzDAO projectPersonAuthzDAO) {
		this.projectPersonAuthzDAO = projectPersonAuthzDAO;
	}
}
