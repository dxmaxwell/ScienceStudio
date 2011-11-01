/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectAuthzController class.
 *     
 */
package ca.sciencestudio.rest.service.project.controllers;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.AddResult;
import ca.sciencestudio.model.EditResult;
import ca.sciencestudio.model.Permissions;
import ca.sciencestudio.model.dao.ModelBasicDAO;
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectBasicDAO;
import ca.sciencestudio.model.project.validators.ProjectValidator;
import ca.sciencestudio.model.validators.ModelValidator;
import ca.sciencestudio.rest.service.controllers.AbstractProjectAuthzController;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 * 
 *
 */
@Controller
public class ProjectAuthzController extends AbstractProjectAuthzController<Project> {

	private static final String PROJECT_MODEL_PATH = "/projects";
	
	private ProjectBasicDAO projectBasicDAO;

	private ProjectValidator projectValidator;

	@ResponseBody
	@RequestMapping(value = PROJECT_MODEL_PATH + "/perms*", method = RequestMethod.GET)
	public Permissions permissions(@RequestParam String user) {  
		//boolean admin = hasLoginRole(user, LOGIN_ROLE_ADMIN_PROJECTS);
		//return new Permissions(admin, true, admin, admin);
		return new Permissions(true);
	 }
	
	@ResponseBody
	@RequestMapping(value = PROJECT_MODEL_PATH + "/{gid}/perms*", method = RequestMethod.GET)
	public Permissions permissions(@RequestParam String user, @PathVariable String gid) {
		//Permissions permissions = permissions(user);
		//if(permissions.isAdmin()) {
		//	return permissions;
		//}
		//boolean expr = hasProjectRole(user, gid, ProjectPerson.Role.EXPERIMENTER);
		//return new Permissions(false, true, expr, expr);
		return new Permissions(true);
	}
	
	@ResponseBody 
	@RequestMapping(value = PROJECT_MODEL_PATH + "/{facility}*", method = RequestMethod.POST)
	public AddResult add(@RequestBody Project project, @RequestParam String user, @PathVariable String facility, HttpServletRequest request, HttpServletResponse response) throws Exception{
		if(!facility.equals(projectBasicDAO.getGidFacility())) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return new AddResult();
		}
		// TODO: Check permissions. //
		
		AddResult result = doAdd(project, request, response);
		
		if(result.hasErrors()) {
			return result;
		}
		
		//ProjectPerson projectPerson = new ProjectPerson();
		//getProjectPersonBasicDAO().add(projectPerson);

		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = PROJECT_MODEL_PATH + "/{gid}*", method = RequestMethod.PUT)
	public EditResult edit(@RequestBody Project project, @RequestParam String user, @PathVariable String gid, HttpServletResponse response) throws Exception{		
		project.setGid(gid);
		// TODO: Check permissions. //
		return doEdit(project, response);
	}
	
	@RequestMapping(value = PROJECT_MODEL_PATH + "/{gid}*", method = RequestMethod.DELETE)
	public void remove(@PathVariable String gid, HttpServletResponse response) throws Exception{
		// TODO: Check permissions. //
		response.setStatus(HttpStatus.FORBIDDEN.value());
		//doRemove(gid, response);
	}
	
	@ResponseBody 
	@RequestMapping(value = PROJECT_MODEL_PATH + "/{gid}*", method = RequestMethod.GET)
	public Object get(@RequestParam String user, @PathVariable String gid, HttpServletResponse response) throws Exception {
		if(isProjectMember(user, gid)) {
			return doGet(gid, response);
		}
		
		if(hasLoginRole(user, LOGIN_ROLE_ADMIN_PROJECTS)) {
			return doGet(gid, response);
		}
		
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		return Collections.emptyMap();
	}
	
	@ResponseBody 
	@RequestMapping(value = PROJECT_MODEL_PATH + "*", method = RequestMethod.GET)
	public Object getAll(@RequestParam String user, HttpServletResponse response) throws Exception {
		try {
			if(hasLoginRole(user, LOGIN_ROLE_ADMIN_PROJECTS)) {
				return projectBasicDAO.getAll();
			}
			
			return projectBasicDAO.getAllByPersonGid(user);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return Collections.emptyList();
		}
	}
	
	@Override
	public String getModelPath() {
		return PROJECT_MODEL_PATH;
	}
	
	@Override
	public ModelBasicDAO<Project> getModelBasicDAO() {
		return projectBasicDAO;
	}
	
	@Override
	public ModelValidator<Project> getModelValidator() {
		return projectValidator;
	}

	public ProjectBasicDAO getProjectBasicDAO() {
		return projectBasicDAO;
	}
	public void setProjectBasicDAO(ProjectBasicDAO projectBasicDAO) {
		this.projectBasicDAO = projectBasicDAO;
	}

	public ProjectValidator getProjectValidator() {
		return projectValidator;
	}
	public void setProjectValidator(ProjectValidator projectValidator) {
		this.projectValidator = projectValidator;
	}	
}
