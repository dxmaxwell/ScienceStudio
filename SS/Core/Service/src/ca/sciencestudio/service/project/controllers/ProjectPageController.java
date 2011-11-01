/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectPageController class.
 *     
 */
package ca.sciencestudio.service.project.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ca.sciencestudio.model.Permissions;
import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.controllers.AbstractModelController;
import ca.sciencestudio.service.project.backers.ProjectFormBacker;
import ca.sciencestudio.service.utilities.ModelPathUtils;
import ca.sciencestudio.util.web.EnumToOptionUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class ProjectPageController extends AbstractModelController {
	
	private ProjectAuthzDAO projectAuthzDAO;
	
	@RequestMapping(value = ModelPathUtils.PROJECT_PATH + ".html")
	public String getProjectsForm(ModelMap model) {
		
		String user = SecurityUtil.getPersonGid();
		
		Permissions permissions = projectAuthzDAO.permissions(user).get();
		if(permissions == null) {
			model.put("error", "Permissions not found.");
			return ERROR_VIEW;
		}
		
		model.put("projectStatusOptions", EnumToOptionUtils.toList(Project.Status.values()));
		model.put("permissions", permissions);
		return "frag/projects";
	}
	
	@RequestMapping(value = ModelPathUtils.PROJECT_PATH + "/{projectGid}.html")
	public String getProjectForm(@PathVariable String projectGid, ModelMap model) {	
		
		String user = SecurityUtil.getPersonGid();
		
		Data<Permissions> dataPermissions = projectAuthzDAO.permissions(user, projectGid);
		
		Project project = projectAuthzDAO.get(user, projectGid).get();
		if(project == null) {
			model.put("error", "Project not found.");
			return ERROR_VIEW;
		}
		
		Permissions permissions = dataPermissions.get();
		if(permissions == null) {
			model.put("error", "Permissions not found.");
			return ERROR_VIEW;
		}
		
		model.put("projectStatusOptions", EnumToOptionUtils.toList(Project.Status.values()));
		model.put("project", new ProjectFormBacker(project));
		model.put("permissions", permissions);
		return "frag/project";
	}

	public ProjectAuthzDAO getProjectAuthzDAO() {
		return projectAuthzDAO;
	}
	public void setProjectAuthzDAO(ProjectAuthzDAO projectAuthzDAO) {
		this.projectAuthzDAO = projectAuthzDAO;
	}
}
