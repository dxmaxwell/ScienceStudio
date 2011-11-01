/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectController class.
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

import ca.sciencestudio.model.Project;
import ca.sciencestudio.model.dao.ProjectDAO;
import ca.sciencestudio.util.exceptions.ModelAccessException;
import ca.sciencestudio.model.validators.ProjectValidator;
import ca.sciencestudio.rest.service.controllers.support.AbstractModelController;

/**
 * @author maxweld
 *
 */
@Controller
public class ProjectController extends AbstractModelController<Project, ProjectDAO, ProjectValidator> {

	private static final String PROJECT_MODEL_URL = "/projects";

	public ProjectController() {
		setValidator(new ProjectValidator());
	}
	
	@Override
	@ResponseBody 
	@RequestMapping(value = PROJECT_MODEL_URL + "*", method = RequestMethod.POST)
	public List<String> add(@RequestBody Project project, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return super.add(project, request, response);
	}

	@Override
	@ResponseBody 
	@RequestMapping(value = PROJECT_MODEL_URL + "/{facility}*", method = RequestMethod.POST)
	public List<String> add(@RequestBody Project project, @PathVariable String facility, HttpServletRequest request, HttpServletResponse response) throws Exception{
		return super.add(project, facility, request, response);
	}
	
	@Override
	@RequestMapping(value = PROJECT_MODEL_URL + "/{gid}*", method = RequestMethod.PUT)
	public void edit(@RequestBody Project project, @PathVariable String gid, HttpServletResponse response) throws Exception{
		super.edit(project, gid, response);
	}
	
	@Override
	@RequestMapping(value = PROJECT_MODEL_URL + "/{gid}*", method = RequestMethod.DELETE)
	public void remove(@PathVariable String gid, HttpServletResponse response) throws Exception{
		super.remove(gid, response);
	}
	
	@Override
	@ResponseBody 
	@RequestMapping(value = PROJECT_MODEL_URL + "/{gid}*", method = RequestMethod.GET)
	public Object get(@PathVariable String gid, HttpServletResponse response) throws Exception {
		return super.get(gid, response);
	}
	
	@Override
	@RequestMapping(value = PROJECT_MODEL_URL + "*", method = RequestMethod.GET)
	@ResponseBody public List<Project> getAll(HttpServletResponse response) {
		return super.getAll(response);
	}
	
	@RequestMapping(value = PROJECT_MODEL_URL + "*", method = RequestMethod.GET, params="status")
	@ResponseBody public List<Project> getAllByStatus(@RequestParam String status, HttpServletResponse response) {
		try {
			return getModelDAO().getAllByStatus(status);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return Collections.emptyList();
		}
	}
	
	@RequestMapping(value = PROJECT_MODEL_URL + "*", method = RequestMethod.GET, params="personGid")
	@ResponseBody public List<Project> getAllByPersonGid(@RequestParam String personGid, HttpServletResponse response) {
		try {
			return getModelDAO().getAllByPersonGid(personGid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return Collections.emptyList();
		}
	}
	
	@RequestMapping(value = PROJECT_MODEL_URL + "*", method = RequestMethod.GET, params={ "personGid", "status" })
	@ResponseBody public List<Project> getAllPersonGidAndStatus(@RequestParam String personGid, @RequestParam String status, HttpServletResponse response) {
		try {
			return getModelDAO().getAllByPersonGidAndStatus(personGid, status);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return Collections.emptyList();
		}
	}
	
	@Override
	protected String getModelUrl() {
		return PROJECT_MODEL_URL;
	}

	public ProjectDAO getProjectDAO() {
		return getModelDAO();
	}
	public void setProjectDAO(ProjectDAO projectDAO) {
		setModelDAO(projectDAO);
	}
}
