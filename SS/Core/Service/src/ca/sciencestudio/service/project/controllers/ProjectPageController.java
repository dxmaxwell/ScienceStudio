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

import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.ProjectStatus;
import ca.sciencestudio.model.project.dao.ProjectDAO;
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.controllers.AbstractModelController;

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
			projectList = projectDAO.getProjectListByStatus(ProjectStatus.ACTIVE);
		}
		else {
			String personUid = SecurityUtil.getPerson().getUid();
			projectList = projectDAO.getProjectListByPersonUidAndStatus(personUid, ProjectStatus.ACTIVE);
		}
		
		model.put("projectStatusList", ProjectStatus.getMarshallableValues(ProjectStatus.UNKNOWN));
		model.put("projectList", projectList);
		return "frag/projects";
	}
	
	@RequestMapping(value = "/project/{projectId}.html",  method = RequestMethod.GET)
	public String getProjectForm(@PathVariable int projectId, ModelMap model) {
		
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		Object group = AuthorityUtil.buildProjectGroupAuthority(projectId);
		
		if(!SecurityUtil.hasAnyAuthority(admin, group)) {
			model.put("error", "Permission denied.");
			return ERROR_VIEW;
		}
		
		Project project = projectDAO.getProjectById(projectId);
		if(project == null) {
			model.put("error", "Project not found.");
			return ERROR_VIEW;
		}
		
		model.put("projectStatusList", ProjectStatus.getMarshallableValues(ProjectStatus.UNKNOWN));
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
