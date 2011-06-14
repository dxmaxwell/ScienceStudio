/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectPageController class.
 *     
 */
package ca.sciencestudio.service.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.sciencestudio.model.Project;
import ca.sciencestudio.model.dao.ProjectDAO;
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.security.util.SecurityUtil.ROLE;
import ca.sciencestudio.service.controllers.AbstractModelController;
import ca.sciencestudio.util.web.EnumToOptionUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class ProjectPageController extends AbstractModelController {
	
	private static final String ERROR_VIEW = "frag/error";
	
	@Autowired
	private ProjectDAO projectDAO;
	
	@RequestMapping(value = "/projects.html", method = RequestMethod.GET)
	public String getProjectsForm(ModelMap model) {
		
		List<Project> projectList;
		if(SecurityUtil.hasAuthority(AuthorityUtil.ROLE_ADMIN_PROJECTS)) {
			projectList = projectDAO.getAllByStatus(Project.Status.ACTIVE);
		}
		else {
			String personUid = SecurityUtil.getPerson().getGid();
			projectList = projectDAO.getAllByPersonGidAndStatus(personUid, Project.Status.ACTIVE);
		}
		
		model.put("projectStatusList", EnumToOptionUtils.toList(Project.Status.values()));
		model.put("projectList", projectList);
		return "frag/projects";
	}
	
	@RequestMapping(value = "/projects/{projectGid}.html",  method = RequestMethod.GET)
	public String getProjectForm(@PathVariable String projectGid, ModelMap model) {
		
		
		
		//Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		//Object group = AuthorityUtil.buildProjectGroupAuthority(projectId);
		
		boolean admin = SecurityUtil.hasAuthority(ROLE.ADMIN_PROJECTS);
		boolean group = SecurityUtil.hasAnyProjectRole(projectGid);
		
		//if(!SecurityUtil.hasAnyAuthority(admin/*, group*/)) {
		if(!group && !admin) {
			model.put("error", "Permission denied.");
			return ERROR_VIEW;
		}
		
		Project project = projectDAO.get(projectGid);
		if(project == null) {
			model.put("error", "Project not found.");
			return ERROR_VIEW;
		}
		
		model.put("projectStatusList", EnumToOptionUtils.toList(Project.Status.values()));
		model.put("project", project);
		return "frag/project";
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}
	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}
}
