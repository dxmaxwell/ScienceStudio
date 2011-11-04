/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SampleAuthzController class.
 *     
 */
package ca.sciencestudio.rest.service.sample.controllers;

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
import ca.sciencestudio.model.project.dao.ProjectBasicDAO;
import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.sample.dao.SampleBasicDAO;
import ca.sciencestudio.model.sample.validators.SampleValidator;
import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.dao.ExperimentAuthzDAO;
import ca.sciencestudio.model.validators.ModelValidator;
import ca.sciencestudio.rest.service.controllers.AbstractProjectAuthzController;
import ca.sciencestudio.security.authz.accessors.ProjectAuthorityAccessor;
import ca.sciencestudio.util.authz.Authorities;
import ca.sciencestudio.util.exceptions.ModelAccessException;
import ca.sciencestudio.util.rest.AddResult;
import ca.sciencestudio.util.rest.EditResult;
import ca.sciencestudio.util.rest.RemoveResult;

/**
 * @author maxweld
 *
 */
@Controller
public class SampleAuthzController extends AbstractProjectAuthzController<Sample> {

	private static final String SAMPLE_MODEL_PATH = "/samples";
	
	private SampleBasicDAO sampleBasicDAO;

	private SampleValidator sampleValidator;
	
	private ProjectBasicDAO projectBasicDAO;
	
	private ProjectAuthorityAccessor projectAuthorityAccessor;
	
	private ExperimentAuthzDAO experimentAuthzDAO;
	

	@ResponseBody
	@RequestMapping(value = SAMPLE_MODEL_PATH + "*", method = RequestMethod.POST)
	public AddResult add(@RequestBody Sample sample, @RequestParam String user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String projectGid = sample.getProjectGid();
		
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
		
		return doAdd(sample, request, response);
	}

	@ResponseBody
	@RequestMapping(value = SAMPLE_MODEL_PATH + "/{gid}*", method = RequestMethod.PUT)
	public EditResult edit(@RequestBody Sample sample, @RequestParam String user, @PathVariable String gid, HttpServletResponse response) throws Exception {

		Sample current;
		try {
			current = sampleBasicDAO.get(gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new EditResult(e.getMessage());
		}
		
		if(current == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return new EditResult("Sample (" + gid + ") not found.");
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
		
		sample.setGid(current.getGid());
		sample.setProjectGid(current.getProjectGid());
		
		return doEdit(sample, response);
	}

	@ResponseBody
	@RequestMapping(value = SAMPLE_MODEL_PATH + "/{gid}*", method = RequestMethod.DELETE)
	public RemoveResult remove(@PathVariable String gid, @RequestParam String user, HttpServletResponse response) throws Exception {
		
		Sample sample;
		try {
			sample = sampleBasicDAO.get(gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new RemoveResult(e.getMessage());
		}
		
		if(sample == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return new RemoveResult("Sample (" + gid + ") not found.");
		}
		
		Authorities authorities;
		try {
			authorities = projectAuthorityAccessor.getAuthorities(user, sample.getProjectGid());
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new RemoveResult(e.getMessage());
		}
		
		if(authorities.containsNone(PROJECT_RESEARCHER, FACILITY_ADMIN_PROJECTS)) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return new RemoveResult("Required authorities not found.");
		}
		
		List<Experiment> experiments;
		try {
			experiments = experimentAuthzDAO.getAllBySourceGid(user, sample.getGid()).get();
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new RemoveResult(e.getMessage());
		}
		
		if(!experiments.isEmpty()) {
			response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
			return new RemoveResult("Sample has associated Experiments.");
		}
		
		return doRemove(gid, response);
	}

	@ResponseBody
	@RequestMapping(value = SAMPLE_MODEL_PATH + "/{gid}*", method = RequestMethod.GET)
	public Object get(@RequestParam String user, @PathVariable String gid, HttpServletResponse response) throws Exception {
		
		Sample sample;
		try {
			sample = sampleBasicDAO.get(gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyMap();
		}
		
		if(sample == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return Collections.emptyMap();
		}
		
		Authorities authorities;
		try {
			authorities = projectAuthorityAccessor.getAuthorities(user, sample.getProjectGid());
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyMap();
		}
		
		if(!authorities.containsProjectAuthority() && authorities.containsNone(FACILITY_ADMIN_PROJECTS)) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return Collections.emptyMap();
		}
		
		return sample;
	}
	
//	@ResponseBody
//	@RequestMapping(value = SAMPLE_MODEL_PATH + "*", method = RequestMethod.GET)
//	public List<Sample> getAll(@RequestParam String user, HttpServletResponse response) throws Exception {
//		try {
//			if(hasLoginRole(user, LOGIN_ROLE_ADMIN_PROJECTS)) {
//				return sampleBasicDAO.getAll();
//			} else {
//				return sampleBasicDAO.getAllByProjectMember(user);
//			}
//		}
//		catch(ModelAccessException e) {
//			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//			return Collections.emptyList();
//		}
//	}

	@ResponseBody
	@RequestMapping(value = SAMPLE_MODEL_PATH + "*", method = RequestMethod.GET, params = "project")
	public List<Sample> getAllByProjectGid(@RequestParam String user, @RequestParam("project") String projectGid, HttpServletResponse response) throws Exception {
		
		Authorities authorities;
		try {
			authorities = projectAuthorityAccessor.getAuthorities(user, projectGid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyList();
		}
		
		if(!authorities.containsProjectAuthority() && authorities.containsNone(FACILITY_ADMIN_PROJECTS)) {
			return Collections.emptyList();
		}
		
		try {
			return sampleBasicDAO.getAllByProjectGid(projectGid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyList();
		}
	}
	
	@Override
	public String getModelPath() {
		return SAMPLE_MODEL_PATH;
	}
	
	@Override
	public ModelBasicDAO<Sample> getModelBasicDAO() {
		return getSampleBasicDAO();
	}
	
	@Override
	public ModelValidator<Sample> getModelValidator() {
		return sampleValidator;
	}

	public SampleBasicDAO getSampleBasicDAO() {
		return sampleBasicDAO;
	}
	public void setSampleBasicDAO(SampleBasicDAO sampleBasicDAO) {
		this.sampleBasicDAO = sampleBasicDAO;
	}

	public SampleValidator getSampleValidator() {
		return sampleValidator;
	}
	public void setSampleValidator(SampleValidator sampleValidator) {
		this.sampleValidator = sampleValidator;
	}
	
	public ProjectBasicDAO getProjectBasicDAO() {
		return projectBasicDAO;
	}
	public void setProjectBasicDAO(ProjectBasicDAO projectBasicDAO) {
		this.projectBasicDAO = projectBasicDAO;
	}
	
	public ExperimentAuthzDAO getExperimentAuthzDAO() {
		return experimentAuthzDAO;
	}
	public void setExperimentAuthzDAO(ExperimentAuthzDAO experimentAuthzDAO) {
		this.experimentAuthzDAO = experimentAuthzDAO;
	}

	public ProjectAuthorityAccessor getProjectAuthorityAccessor() {
		return projectAuthorityAccessor;
	}
	public void setProjectAuthorityAccessor(ProjectAuthorityAccessor projectAuthorityAccessor) {
		this.projectAuthorityAccessor = projectAuthorityAccessor;
	}
}
