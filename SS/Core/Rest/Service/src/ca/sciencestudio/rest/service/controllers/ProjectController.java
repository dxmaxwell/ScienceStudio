/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectController class.
 *     
 */
package ca.sciencestudio.rest.service.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.Project;
import ca.sciencestudio.model.dao.ProjectDAO;
import ca.sciencestudio.rest.service.controllers.support.AbstractModelController;

/**
 * @author maxweld
 *
 */
@Controller
public class ProjectController extends AbstractModelController<Project, ProjectDAO> {

	private static final String PROJECT_MODEL_URL = "/projects";
	
	@Override
	@ResponseBody 
	@RequestMapping(value = PROJECT_MODEL_URL + "*", method = RequestMethod.POST)
	public List<String> add(@RequestBody Project project, HttpServletRequest request, HttpServletResponse response) throws Exception{
		return super.add(project, request, response);
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
	@RequestMapping(value = "/projects/{gid}*", method = RequestMethod.GET)
	public Object get(@PathVariable String gid, HttpServletResponse response) throws Exception {
		return super.get(gid, response);
	}
	
	@Override
	@RequestMapping(value = "/projects*", method = RequestMethod.GET)
	@ResponseBody public List<Project> getAll() {
		return super.getAll();
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
