/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectPersonAuthzController class.
 *     
 */
package ca.sciencestudio.rest.service.project.controllers;

import java.util.Collections;
import java.util.List;

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
import ca.sciencestudio.model.project.ProjectPerson;
import ca.sciencestudio.model.project.validators.ProjectPersonValidator;
import ca.sciencestudio.model.validators.ModelValidator;
import ca.sciencestudio.rest.service.controllers.AbstractProjectAuthzController;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 *
 *
 */
@Controller
public class ProjectPersonAuthzController extends AbstractProjectAuthzController<ProjectPerson> {

	private static final String PROJECT_PERSON_MODEL_PATH = "/project/persons";
	
	private ProjectPersonValidator projectPersonValidator;
	
	@ResponseBody
	@RequestMapping(value = PROJECT_PERSON_MODEL_PATH + "/perms*", method = RequestMethod.GET)
	public Permissions permissions(@RequestParam String user) {
		return new Permissions(true);
	}
	
	@ResponseBody
	@RequestMapping(value = PROJECT_PERSON_MODEL_PATH + "/{gid}/perms*", method = RequestMethod.GET)
	public Permissions permissions(@RequestParam String user, @PathVariable String gid) {
		return new Permissions(true);
	}
	
	@ResponseBody
	@RequestMapping(value = PROJECT_PERSON_MODEL_PATH + "/{facility}*", method = RequestMethod.POST)
	public AddResult add(@RequestBody ProjectPerson projectPerson, @RequestParam String user, @PathVariable String facility, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(!facility.equals(getModelBasicDAO().getGidFacility())) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return new AddResult();
		}
		
		// Check permissions. //
		return doAdd(projectPerson, request, response);
	}

	@ResponseBody
	@RequestMapping(value = PROJECT_PERSON_MODEL_PATH + "/{gid}*", method = RequestMethod.PUT)
	public EditResult edit(@RequestBody ProjectPerson projectPerson, @PathVariable String gid, HttpServletResponse response) throws Exception {
		projectPerson.setGid(gid);
		
		// Check permissions. //
		return doEdit(projectPerson, response);
	}

	@RequestMapping(value = PROJECT_PERSON_MODEL_PATH + "/{gid}*", method = RequestMethod.DELETE)
	public void remove(@PathVariable String gid, HttpServletResponse response) throws Exception {
		// Check permissions. //
		
		response.setStatus(HttpStatus.FORBIDDEN.value());
		//doRemove(gid, response);
	}

	@ResponseBody
	@RequestMapping(value = PROJECT_PERSON_MODEL_PATH + "/{gid}*", method = RequestMethod.GET)
	public Object get(@RequestParam String user, @PathVariable String gid, HttpServletResponse response) throws Exception {
		try {
			ProjectPerson projectPerson = getProjectPersonBasicDAO().get(gid);
			if(projectPerson == null) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return Collections.emptyMap();
			}
			
			if(isProjectMember(user, projectPerson.getProjectGid())) { 
				return projectPerson;
			}
			
			if(hasLoginRole(user, LOGIN_ROLE_ADMIN_PROJECTS)) {
				return projectPerson;
			}
			
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return Collections.emptyMap();
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return Collections.emptyList();
		}
	}

//	@ResponseBody
//	@RequestMapping(value = PROJECT_PERSON_MODEL_URL + "*", method = RequestMethod.GET)
//	public List<ProjectPerson> getAll(@RequestParam String user, HttpServletResponse response) {
//		try {
//			if(hasLoginRole(user, LOGIN_ROLE_ADMIN_PROJECTS)) {
//				return getProjectPersonBasicDAO().getAll();
//			} else {
//				return getProjectPersonBasicDAO().getAllByProjectMember(user);
//			}
//		}
//		catch(ModelAccessException e) {
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			return Collections.emptyList();
//		}
//	}
	
	@ResponseBody
	@RequestMapping(value = PROJECT_PERSON_MODEL_PATH + "*", method = RequestMethod.GET, params = "person")
	public List<ProjectPerson> getAllByPersonGid(@RequestParam String user, @RequestParam("person") String personGid, HttpServletResponse response) throws Exception {
		try {
			if(user.equalsIgnoreCase(personGid)) {
				return getProjectPersonBasicDAO().getAllByPersonGid(personGid);
			}
		
			if(hasLoginRole(user, LOGIN_ROLE_ADMIN_PROJECTS)) {
				return getProjectPersonBasicDAO().getAllByPersonGid(personGid);
			}
			
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return Collections.emptyList();
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return Collections.emptyList();
		}
	}
	
	@ResponseBody
	@RequestMapping(value = PROJECT_PERSON_MODEL_PATH + "*", method = RequestMethod.GET, params = "project")
	public List<ProjectPerson> getAllByProjectGid(@RequestParam String user, @RequestParam("project") String projectGid, HttpServletResponse response) throws Exception {	
		try {
			if(isProjectMember(user, projectGid)) {
				return getProjectPersonBasicDAO().getAllByProjectGid(projectGid);
			}
			
			if(hasLoginRole(user, LOGIN_ROLE_ADMIN_PROJECTS)) {
				return getProjectPersonBasicDAO().getAllByProjectGid(projectGid);
			}
			
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return Collections.emptyList();
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return Collections.emptyList();
		}
	}
	
	@Override
	public String getModelPath() {
		return PROJECT_PERSON_MODEL_PATH;
	}
	
	@Override
	public ModelBasicDAO<ProjectPerson> getModelBasicDAO() {
		return getProjectPersonBasicDAO();
	}

	@Override
	public ModelValidator<ProjectPerson> getModelValidator() {
		return projectPersonValidator;
	}

	public ProjectPersonValidator getProjectPersonValidator() {
		return projectPersonValidator;
	}
	public void setProjectPersonValidator(ProjectPersonValidator projectPersonValidator) {
		this.projectPersonValidator = projectPersonValidator;
	}
}
