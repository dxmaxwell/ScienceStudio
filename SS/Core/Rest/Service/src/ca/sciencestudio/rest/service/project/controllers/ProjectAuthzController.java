/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectAuthzController class.
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
import ca.sciencestudio.model.facility.Facility;
import ca.sciencestudio.model.facility.dao.FacilityBasicDAO;
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.ProjectPerson;
import ca.sciencestudio.model.project.dao.ProjectBasicDAO;
import ca.sciencestudio.model.project.dao.ProjectPersonBasicDAO;
import ca.sciencestudio.model.project.validators.ProjectValidator;
import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.sample.dao.SampleBasicDAO;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.SessionAuthzDAO;
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
public class ProjectAuthzController extends AbstractProjectAuthzController<Project> {

	private static final String PROJECT_MODEL_PATH = "/projects";
	
	private SampleBasicDAO sampleBasicDAO;
	
	private FacilityBasicDAO facilityBasicDAO;
	
	private ProjectBasicDAO projectBasicDAO;

	private ProjectPersonBasicDAO projectPersonBasicDAO;
	
	private SessionAuthzDAO sessionAuthzDAO;
	
	private ProjectValidator projectValidator;
	
	@ResponseBody 
	@RequestMapping(value = PROJECT_MODEL_PATH + "*", method = RequestMethod.POST)
	public AddResult add(@RequestBody Project project, @RequestParam String user, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String facilityGid = project.getFacilityGid();
		
		try {
			Facility facility = facilityBasicDAO.get(facilityGid);
			if(facility == null) {
				response.setStatus(HttpStatus.NOT_FOUND.value());
				return new AddResult("Facility (" + facilityGid + ") not found.");
			}
		} 
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new AddResult(e.getMessage());
		}
		
		Authorities authorities;
		try {
			authorities = projectAuthorityAccessor.getAuthorities(user);
		} 
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new AddResult(e.getMessage());
		}
			
		// Everybody can add Projects, no authority check required. //
		
		AddResult result = doAdd(project, request, response);
		if(result.hasErrors()) {
			return result;
		}
		
		if(authorities.containsNone(FACILITY_ADMIN_PROJECTS)) {
			ProjectPerson projectPerson = new ProjectPerson();
			projectPerson.setRole(ProjectPerson.Role.RESEARCHER);
			projectPerson.setProjectGid(project.getGid());
			projectPerson.setPersonGid(user);
			projectPersonBasicDAO.add(projectPerson);	
		}
		
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = PROJECT_MODEL_PATH + "/{gid}*", method = RequestMethod.PUT)
	public EditResult edit(@RequestBody Project project, @RequestParam String user, @PathVariable String gid, HttpServletResponse response) throws Exception{		
		
		Project current;
		try {
			current = projectBasicDAO.get(gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new EditResult(e.getMessage());
		}
		
		if(current == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return new EditResult("Project (" + gid + ") not found.");
		}
		
		Authorities authorities;
		try {
			authorities = projectAuthorityAccessor.getAuthorities(user, gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new EditResult(e.getMessage());
		}
		
		if(authorities.containsNone(PROJECT_RESEARCHER, FACILITY_ADMIN_PROJECTS)) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return new EditResult("Required authorities not found.");
		}
		
		project.setGid(current.getGid());
		project.setFacilityGid(current.getFacilityGid());
		return doEdit(project, response);
	}
	
	@ResponseBody
	@RequestMapping(value = PROJECT_MODEL_PATH + "/{gid}*", method = RequestMethod.DELETE)
	public RemoveResult remove(@RequestParam String user, @PathVariable String gid, HttpServletResponse response) throws Exception{
		
		Project project;
		try {
			project = projectBasicDAO.get(gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new RemoveResult(e.getMessage());
		}
		
		if(project == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return new RemoveResult("Project (" + gid + ") not found.");
		}
		
		Authorities authorities;
		try {
			authorities = projectAuthorityAccessor.getAuthorities(user, gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new RemoveResult(e.getMessage());
		}
			
		if(authorities.containsNone(PROJECT_RESEARCHER, FACILITY_ADMIN_PROJECTS)) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return new RemoveResult("Required authorities not found.");
		}
		
		List<Session> sessions;
		try {
			sessions = sessionAuthzDAO.getAllByProjectGid(user, gid).get();
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new RemoveResult(e.getMessage());
		}
		
		if(!sessions.isEmpty()) {
			response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
			return new RemoveResult("Project has associated Sessions.");
		}
		
		List<Sample> samples;
		try {
			samples = sampleBasicDAO.getAllByProjectGid(gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new RemoveResult(e.getMessage());
		}
		
		if(!samples.isEmpty()) {
			response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
			return new RemoveResult("Project has associated Samples.");
		}
		
		try {
			List<ProjectPerson> projectPersons = projectPersonBasicDAO.getAllByProjectGid(gid);
			for(ProjectPerson projectPerson : projectPersons) {
				projectPersonBasicDAO.remove(projectPerson.getGid());
			}
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new RemoveResult(e.getMessage());
		}
		
		return doRemove(gid, response);
	}
	
	@ResponseBody 
	@RequestMapping(value = PROJECT_MODEL_PATH + "/{gid}*", method = RequestMethod.GET)
	public Object get(@RequestParam String user, @PathVariable String gid, HttpServletResponse response) throws Exception {
		
		Project project;
		try {
			project = projectBasicDAO.get(gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new RemoveResult(e.getMessage());
		}
		
		if(project == null) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return Collections.emptyMap();
		}

		Authorities authorities;
		try {
			authorities = projectAuthorityAccessor.getAuthorities(user, gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyMap();
		}
	
		if(!authorities.containsProjectAuthority() && authorities.containsNone(FACILITY_ADMIN_PROJECTS)) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return Collections.emptyMap();
		}

		return project;		
	}
	
	@ResponseBody 
	@RequestMapping(value = PROJECT_MODEL_PATH + "*", method = RequestMethod.GET)
	public Object getAll(@RequestParam String user, HttpServletResponse response) throws Exception {
		
		Authorities authorities;
		try {
			authorities = projectAuthorityAccessor.getAuthorities(user);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyList();
		}
		
		List<Project> projects;
		try {
			if(authorities.containsNone(FACILITY_ADMIN_PROJECTS)) {
				projects = projectBasicDAO.getAllByPersonGid(user);
			} else {
				projects = projectBasicDAO.getAll();
			}
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return Collections.emptyList();
		}
		
		return projects;
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

	public FacilityBasicDAO getFacilityBasicDAO() {
		return facilityBasicDAO;
	}
	public void setFacilityBasicDAO(FacilityBasicDAO facilityBasicDAO) {
		this.facilityBasicDAO = facilityBasicDAO;
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

	public ProjectValidator getProjectValidator() {
		return projectValidator;
	}
	public void setProjectValidator(ProjectValidator projectValidator) {
		this.projectValidator = projectValidator;
	}

	public SampleBasicDAO getSampleBasicDAO() {
		return sampleBasicDAO;
	}
	public void setSampleBasicDAO(SampleBasicDAO sampleBasicDAO) {
		this.sampleBasicDAO = sampleBasicDAO;
	}

	public SessionAuthzDAO getSessionAuthzDAO() {
		return sessionAuthzDAO;
	}
	public void setSessionAuthzDAO(SessionAuthzDAO sessionAuthzDAO) {
		this.sessionAuthzDAO = sessionAuthzDAO;
	}
}
