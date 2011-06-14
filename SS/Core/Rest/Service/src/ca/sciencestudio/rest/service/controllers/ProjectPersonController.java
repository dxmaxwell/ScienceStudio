/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectPersonController class.
 *     
 */
package ca.sciencestudio.rest.service.controllers;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.ProjectPerson;
import ca.sciencestudio.model.dao.ProjectPersonDAO;
import ca.sciencestudio.model.dao.support.ModelAccessException;
import ca.sciencestudio.model.validators.ProjectPersonValidator;
import ca.sciencestudio.rest.service.controllers.support.AbstractModelController;

/**
 * @author maxweld
 *
 *
 */
@Controller
public class ProjectPersonController extends AbstractModelController<ProjectPerson, ProjectPersonDAO, ProjectPersonValidator> {

	private static final String PROJECT_PERSON_MODEL_URL = "/project/persons";
	
	public ProjectPersonController() {
		setValidator(new ProjectPersonValidator());
	}
	
	@Override
	@ResponseBody
	@RequestMapping(value = PROJECT_PERSON_MODEL_URL + "*", method = RequestMethod.POST)
	public List<String> add(@RequestBody ProjectPerson projectPerson, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return super.add(projectPerson, request, response);
	}
	
	@Override
	@ResponseBody
	@RequestMapping(value = PROJECT_PERSON_MODEL_URL + "/{facility}*", method = RequestMethod.POST)
	public List<String> add(@RequestBody ProjectPerson projectPerson, @PathVariable String facility, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return super.add(projectPerson, facility, request, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = PROJECT_PERSON_MODEL_URL + "/{gid}*", method = RequestMethod.PUT)
	public void edit(@RequestBody ProjectPerson projectPerson, @PathVariable String gid, HttpServletResponse response) throws Exception {
		super.edit(projectPerson, gid, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = PROJECT_PERSON_MODEL_URL + "/{gid}*", method = RequestMethod.DELETE)
	public void remove(@PathVariable String gid, HttpServletResponse response) throws Exception {
		super.remove(gid, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = PROJECT_PERSON_MODEL_URL + "/{gid}*", method = RequestMethod.GET)
	public Object get(@PathVariable String gid, HttpServletResponse response) throws Exception {
		return super.get(gid, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = PROJECT_PERSON_MODEL_URL + "*", method = RequestMethod.GET)
	public List<ProjectPerson> getAll(HttpServletResponse response) {
		return super.getAll(response);
	}

	@ResponseBody
	@RequestMapping(value = PROJECT_PERSON_MODEL_URL + "*", method = RequestMethod.GET, params = { "personGid" })
	public List<ProjectPerson> getAllByPersonGid(@RequestParam String personGid, HttpServletResponse response) throws Exception {
		try {
			return getProjectPersonDAO().getAllByPersonGid(personGid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return Collections.emptyList();
		}
	}
	
	@ResponseBody
	@RequestMapping(value = PROJECT_PERSON_MODEL_URL + "*", method = RequestMethod.GET, params = { "projectGid" })
	public List<ProjectPerson> getAllByProjectGid(@RequestParam String projectGid, HttpServletResponse response) throws Exception {
		try {
			return getProjectPersonDAO().getAllByProjectGid(projectGid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return Collections.emptyList();
		}
	}
	
	@ResponseBody
	@RequestMapping(value = PROJECT_PERSON_MODEL_URL + "*", method = RequestMethod.GET, params = { "projectGid", "personGid" })
	public List<ProjectPerson> getAllByProjectGidAndPersonGid(@RequestParam String projectGid, @RequestParam String personGid, HttpServletResponse response) throws Exception {
		try {
			return getProjectPersonDAO().getAllByProjectGidAndPersonGid(projectGid, personGid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return Collections.emptyList();
		}
	}
	
	@Override
	protected String getModelUrl() {
		return PROJECT_PERSON_MODEL_URL;
	}

	public ProjectPersonDAO getProjectPersonDAO() {
		return getModelDAO();
	}
	public void setProjectPersonDAO(ProjectPersonDAO projectPersonDAO) {
		setModelDAO(projectPersonDAO);
	}
}
