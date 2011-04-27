/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectController class.
 *     
 */
package ca.sciencestudio.rest.service.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.rest.model.project.Project;
import ca.sciencestudio.rest.model.project.SimpleProject;
import ca.sciencestudio.rest.model.project.dao.MemoryProjectDAO;

/**
 * @author maxweld
 *
 */
@Controller
public class ProjectController {

	private MemoryProjectDAO projectDAO;
	
	@RequestMapping(value = "/projects*", method = RequestMethod.GET)
	@ResponseBody public Collection<Project> getProjectList() {
		return projectDAO.getProjectList();
	}

	@RequestMapping(value = "/projects/{uid}*", method = RequestMethod.GET)
	@ResponseBody public Project getProjectById(@PathVariable String uid, HttpServletResponse response) throws Exception {
		Project project = projectDAO.getProjectByUid(uid);
		if(project == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return project;
	}
	
	@RequestMapping(value = "/projects*", method = RequestMethod.POST)
	@ResponseBody public Map<String,String> addProject(@RequestBody SimpleProject project, HttpServletResponse response, HttpServletRequest request) throws Exception{
		String uid = projectDAO.addProject(project);
		response.setStatus(HttpServletResponse.SC_CREATED);
		response.setHeader("Location", buildLocationPath(request, "project", uid));
		Map<String,String> responseBody = new HashMap<String,String>();
		responseBody.put("uid", uid);
		return responseBody;
	}

	protected String buildLocationPath(HttpServletRequest request, Object... objects) throws UnsupportedEncodingException {
		 StringBuilder path = new StringBuilder(request.getContextPath() + request.getServletPath());
		 for(Object o : objects) {
			 path.append("/").append(URLEncoder.encode(o.toString(), "UTF-8"));
		 }
		 return path.toString();
	}
	
	public MemoryProjectDAO getProjectDAO() {
		return projectDAO;
	}

	public void setProjectDAO(MemoryProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}
}
