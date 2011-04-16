/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectFormController class.
 *     
 */
package ca.sciencestudio.service.project.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectDAO;
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.controllers.AbstractModelController;
import ca.sciencestudio.service.project.backers.ProjectFormBacker;
import ca.sciencestudio.service.project.validators.ProjectFormBackerValidator;
import ca.sciencestudio.service.utilities.ModelPathUtils;
import ca.sciencestudio.util.web.BindAndValidateUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class ProjectFormController extends AbstractModelController {

	@Autowired
	private ProjectDAO projectDAO;
	
	@Autowired
	private ProjectFormBackerValidator projectFormBackerValidator;
	
	@RequestMapping(value = "/projects/form/add.{format}", method = RequestMethod.POST)
	public String postProjectFormAdd(@PathVariable String format, HttpServletRequest request, ModelMap model) {
		
		ProjectFormBacker projectFormBacker = new ProjectFormBacker();
		BindException errors = BindAndValidateUtils.buildBindException(projectFormBacker);
		model.put("errors", errors);
		
		String responseView = "response-" + format;
		
		if(!SecurityUtil.hasAuthority(AuthorityUtil.ROLE_ADMIN_PROJECTS)) {
			errors.reject("permission.denied", "Permission denied.");
			return responseView;
		}
		
		errors = BindAndValidateUtils.bindAndValidate(projectFormBacker, request, projectFormBackerValidator);	
		if(errors.hasErrors()) {
			model.put("errors", errors);
			return responseView;
		}
		
		Project project = projectFormBacker.createProject(projectDAO);
		
		projectDAO.addProject(project);
		
		Map<String,Object> response = new HashMap<String,Object>();
		response.put("viewUrl", getModelPath(request) + ModelPathUtils.getProjectPath(project.getId(), ".html"));
		
		model.put("response", response);
		return responseView;
	}
	
	@RequestMapping(value = "/project/{projectId}/form/edit.{format}", method = RequestMethod.POST)
	public String postProjectFormEdit(@PathVariable int projectId, @PathVariable String format, HttpServletRequest request, ModelMap model) {
		
		BindException errors = BindAndValidateUtils.buildBindException();
		model.put("errors", errors);
		
		String responseView = "response-" + format;
		
		Project project = projectDAO.getProjectById(projectId);
		if(project == null) {
			errors.reject("project.notfound", "Project not found.");
			return responseView;
		}
		
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		
		if(!SecurityUtil.hasAuthority(admin)) {
			errors.reject("", "Permission denied.");
			return responseView;
		}
		
		ProjectFormBacker projectFormBacker = new ProjectFormBacker(project);
		
		errors = BindAndValidateUtils.bindAndValidate(projectFormBacker, request, projectFormBackerValidator);
		if(errors.hasErrors()) {
			model.put("errors", errors);
			return responseView;
		}
		
		projectDAO.editProject(projectFormBacker.createProject(projectDAO));
		
		Map<String,Object> response = new HashMap<String,Object>();
		response.put("message", "Project saved.");
		
		model.put("response", response);
		return responseView;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}
	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public ProjectFormBackerValidator getProjectFormBackerValidator() {
		return projectFormBackerValidator;
	}
	public void setProjectFormBackerValidator(ProjectFormBackerValidator projectFormBackerValidator) {
		this.projectFormBackerValidator = projectFormBackerValidator;
	}
}
