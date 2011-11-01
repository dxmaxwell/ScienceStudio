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

import ca.sciencestudio.model.AddResult;
import ca.sciencestudio.model.EditResult;
import ca.sciencestudio.model.Permissions;
import ca.sciencestudio.model.dao.ModelBasicDAO;
import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.sample.dao.SampleBasicDAO;
import ca.sciencestudio.model.sample.validators.SampleValidator;
import ca.sciencestudio.model.validators.ModelValidator;
import ca.sciencestudio.rest.service.controllers.AbstractProjectAuthzController;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 *
 */
@Controller
public class SampleAuthzController extends AbstractProjectAuthzController<Sample> {

	private static final String SAMPLE_MODEL_PATH = "/samples";
	
	private SampleBasicDAO sampleBasicDAO;

	private SampleValidator sampleValidator;
	
	
	@ResponseBody
	@RequestMapping(value = SAMPLE_MODEL_PATH + "/perms*", method = RequestMethod.GET)
	public Permissions permissions(@RequestParam String user) {
		return new Permissions(true);
	}
	
	@ResponseBody
	@RequestMapping(value = SAMPLE_MODEL_PATH + "/{gid}/perms*", method = RequestMethod.GET)
	public Permissions permissions(@RequestParam String user, @PathVariable String gid) {
		return new Permissions(true);
	}

	@ResponseBody
	@RequestMapping(value = SAMPLE_MODEL_PATH + "/{facility}*", method = RequestMethod.POST)
	public AddResult add(@RequestBody Sample sample, @PathVariable String facility, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(!facility.equals(sampleBasicDAO.getGidFacility())) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return new AddResult();
		}
		
		// Check permissions. //		
		return doAdd(sample, request, response);
	}

	@ResponseBody
	@RequestMapping(value = SAMPLE_MODEL_PATH + "/{gid}*", method = RequestMethod.PUT)
	public EditResult edit(@RequestBody Sample sample, @PathVariable String gid, HttpServletResponse response) throws Exception {
		sample.setGid(gid);
		
		// Check permissions. //
		return doEdit(sample, response);
	}

	@ResponseBody
	@RequestMapping(value = SAMPLE_MODEL_PATH + "/{gid}*", method = RequestMethod.DELETE)
	public void remove(@PathVariable String gid, HttpServletResponse response) throws Exception {
		// Check permissions. //
		
		response.setStatus(HttpStatus.FORBIDDEN.value());
		//doRemove(gid, response);
	}

	@ResponseBody
	@RequestMapping(value = SAMPLE_MODEL_PATH + "/{gid}*", method = RequestMethod.GET)
	public Object get(@RequestParam String user, @PathVariable String gid, HttpServletResponse response) throws Exception {
		Sample sample = sampleBasicDAO.get(gid);
		if(sample == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.emptyMap();
		}
		
		if(!isProjectMember(user, sample.getProjectGid()) && !hasLoginRole(user, LOGIN_ROLE_ADMIN_PROJECTS)) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return Collections.emptyMap();
		}
		
		return sample;
	}
	
	@ResponseBody
	@RequestMapping(value = SAMPLE_MODEL_PATH + "*", method = RequestMethod.GET)
	public List<Sample> getAll(@RequestParam String user, HttpServletResponse response) {
		try {
			if(hasLoginRole(user, LOGIN_ROLE_ADMIN_PROJECTS)) {
				return sampleBasicDAO.getAll();
			} else {
				return sampleBasicDAO.getAllByProjectMember(user);
			}
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return Collections.emptyList();
		}
	}

	@ResponseBody
	@RequestMapping(value = SAMPLE_MODEL_PATH + "*", method = RequestMethod.GET, params = "project")
	public List<Sample> getAllByProjectGid(@RequestParam String user, @RequestParam("project") String projectGid, HttpServletResponse response) {
		if(!isProjectMember(user, projectGid) && !hasLoginRole(user, LOGIN_ROLE_ADMIN_PROJECTS)) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return Collections.emptyList();
		}
		
		try {
			return sampleBasicDAO.getAllByProjectGid(projectGid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
}
