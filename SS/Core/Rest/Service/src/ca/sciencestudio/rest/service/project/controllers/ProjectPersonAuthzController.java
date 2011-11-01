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

import ca.sciencestudio.model.dao.ModelBasicDAO;
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.ProjectPerson;
import ca.sciencestudio.model.project.dao.ProjectBasicDAO;
import ca.sciencestudio.model.project.dao.ProjectPersonBasicDAO;
import ca.sciencestudio.model.project.validators.ProjectPersonValidator;
import ca.sciencestudio.model.validators.ModelValidator;
import ca.sciencestudio.rest.service.controllers.AbstractProjectAuthzController;
import ca.sciencestudio.util.authz.Authorities;
import ca.sciencestudio.util.exceptions.ModelAccessException;
import ca.sciencestudio.util.rest.AddResult;
import ca.sciencestudio.util.rest.EditResult;
import ca.sciencestudio.util.rest.RemoveResult;

/**
 * @author maxweld
 *
 *
 */
@Controller
public class ProjectPersonAuthzController extends AbstractProjectAuthzController<ProjectPerson> {

	private static final String PROJECT_PERSON_MODEL_PATH = "/project/persons";
	
	private ProjectBasicDAO projectBasicDAO;
	
	private ProjectPersonBasicDAO projectPersonBasicDAO;
	
	private ProjectPersonValidator projectPersonValidator;
	
	@ResponseBody
	@RequestMapping(value = PROJECT_PERSON_MODEL_PATH + "*", method = RequestMethod.POST)
	public AddResult add(@RequestBody ProjectPerson projectPerson, @RequestParam String user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String projectGid = projectPerson.getProjectGid();
		
		try {
			Project project = projectBasicDAO.get(projectGid);
			if(project == null) {
				response.setStatus(HttpStatus.NOT_FOUND.value());
				return new AddResult("Project (" + projectGid + ") not found.");
			}
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new AddResult(e.getMessage());
		}
		
		Authorities authorities;
		try {
			authorities = projectAuthorityAccessor.getAuthorities(user, projectGid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new AddResult(e.getMessage());
		}
	
		if(authorities.containsNone(PROJECT_RESEARCHER, FACILITY_ADMIN_PROJECTS)) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return new AddResult("Required authorities not found.");
		}
		
		List<ProjectPerson> projectPersons;
		try {
			projectPersons = projectPersonBasicDAO.getAllByProjectGid(projectGid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new AddResult(e.getMessage());
		}
		
		for(ProjectPerson pp : projectPersons) {
			if(pp.getPersonGid().equalsIgnoreCase(projectPerson.getPersonGid())) {
				response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
				return new AddResult("Already a member of the project team.");
			}
		}
		
		return doAdd(projectPerson, request, response);
	}

	@ResponseBody
	@RequestMapping(value = PROJECT_PERSON_MODEL_PATH + "/{gid}*", method = RequestMethod.PUT)
	public EditResult edit(@RequestBody ProjectPerson projectPerson, @RequestParam String user, @PathVariable String gid, HttpServletResponse response) throws Exception {
		
		ProjectPerson current; 
		try {
			current = projectPersonBasicDAO.get(gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new EditResult(e.getMessage());
		}
		
		if(current == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return new EditResult("ProjectPerson (" + gid + ") not found.");
		}
		
		Authorities authorities;
		try {
			authorities = projectAuthorityAccessor.getAuthorities(user, current.getProjectGid());
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new EditResult(e.getMessage());
		}
		
		if(authorities.containsNone(PROJECT_RESEARCHER, FACILITY_ADMIN_PROJECTS)) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return new EditResult("Required authorities not found.");
		}
	
		projectPerson.setGid(current.getGid());
		projectPerson.setPersonGid(current.getPersonGid());
		
		// A user may not change their own role to OBSERVER. //
		if(projectPerson.getPersonGid().equalsIgnoreCase(user) && (projectPerson.getRole() == ProjectPerson.Role.COLLABORATOR)) {
			response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
			return new EditResult("Not allowed to edit your project role.");
		}
		
		return doEdit(projectPerson, response);
	}

	@ResponseBody
	@RequestMapping(value = PROJECT_PERSON_MODEL_PATH + "/{gid}*", method = RequestMethod.DELETE)
	public RemoveResult remove(@PathVariable String gid, @RequestParam String user, HttpServletResponse response) throws Exception {
		
		ProjectPerson projectPerson; 
		try {
			projectPerson = projectPersonBasicDAO.get(gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new RemoveResult(e.getMessage());
		}
		
		if(projectPerson == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return new RemoveResult("ProjectPerson (" + gid + ") not found.");
		}
		
		Authorities authorities;
		try {
			authorities = projectAuthorityAccessor.getAuthorities(user, projectPerson.getProjectGid());
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new RemoveResult(e.getMessage());
		}
		
		if(authorities.containsNone(PROJECT_RESEARCHER, FACILITY_ADMIN_PROJECTS)) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return new RemoveResult("Required authorities not found.");
		}
		
		// A user may not remove themselves from a project. //
		if(user.equalsIgnoreCase(projectPerson.getPersonGid())) {
			response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
			return new RemoveResult("Not allowed to remove yourself from project.");
		}
		
		return doRemove(gid, response);
	}

	@ResponseBody
	@RequestMapping(value = PROJECT_PERSON_MODEL_PATH + "/{gid}*", method = RequestMethod.GET)
	public Object get(@RequestParam String user, @PathVariable String gid, HttpServletResponse response) throws Exception {
		
		ProjectPerson projectPerson;
		try {
			projectPerson = projectPersonBasicDAO.get(gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyList();
		}
			
		if(projectPerson == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.emptyMap();
		}
		
		Authorities authorities;
		try {
			authorities = projectAuthorityAccessor.getAuthorities(user, projectPerson.getProjectGid());
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new RemoveResult(e.getMessage());
		}
		
		if(!authorities.containsProjectAuthority() && authorities.containsNone(FACILITY_ADMIN_PROJECTS)) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return Collections.emptyMap();
		}
		
		return projectPerson;
	}
	
	@ResponseBody
	@RequestMapping(value = PROJECT_PERSON_MODEL_PATH + "*", method = RequestMethod.GET, params = "person")
	public List<ProjectPerson> getAllByPersonGid(@RequestParam String user, @RequestParam("person") String personGid, HttpServletResponse response) throws Exception {
		
		Authorities authorities;
		try {
			authorities = projectAuthorityAccessor.getAuthorities(user);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyList();
		}
		
		if(!user.equalsIgnoreCase(personGid) || authorities.containsNone(FACILITY_ADMIN_PROJECTS)) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return Collections.emptyList();		
		}
			
		try {
			return projectPersonBasicDAO.getAllByPersonGid(personGid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return Collections.emptyList();
		}
	}
	
	@ResponseBody
	@RequestMapping(value = PROJECT_PERSON_MODEL_PATH + "*", method = RequestMethod.GET, params = "project")
	public List<ProjectPerson> getAllByProjectGid(@RequestParam String user, @RequestParam("project") String gid, HttpServletResponse response) throws Exception {
		
		Authorities authorities;
		try {
			authorities = projectAuthorityAccessor.getAuthorities(user, gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyList();
		}
		
		if(!authorities.containsProjectAuthority() && authorities.containsNone(FACILITY_ADMIN_PROJECTS)) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return Collections.emptyList();
		}
		
		try {
			return getProjectPersonBasicDAO().getAllByProjectGid(gid);
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
		return projectPersonBasicDAO;
	}

	@Override
	public ModelValidator<ProjectPerson> getModelValidator() {
		return projectPersonValidator;
	}

	public ProjectBasicDAO getProjectBasicDAO() {
		return projectBasicDAO;
	}
	public void setProjectBasicDAO(ProjectBasicDAO projectBasicDAO) {
		this.projectBasicDAO = projectBasicDAO;
	}

	public ProjectPersonBasicDAO getProjectPersonBasicDAO() {
		return projectPersonBasicDAO;
	}
	public void setProjectPersonBasicDAO(ProjectPersonBasicDAO projectPersonBasicDAO) {
		this.projectPersonBasicDAO = projectPersonBasicDAO;
	}

	public ProjectPersonValidator getProjectPersonValidator() {
		return projectPersonValidator;
	}
	public void setProjectPersonValidator(ProjectPersonValidator projectPersonValidator) {
		this.projectPersonValidator = projectPersonValidator;
	}
}
