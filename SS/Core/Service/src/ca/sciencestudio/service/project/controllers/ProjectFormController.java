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
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.Project;
import ca.sciencestudio.model.dao.ProjectDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.security.util.SecurityUtil.ROLE;
import ca.sciencestudio.service.controllers.AbstractModelController;
import ca.sciencestudio.service.project.backers.ProjectFormBacker;
import ca.sciencestudio.service.project.validators.ProjectFormBackerValidator;
import ca.sciencestudio.service.utilities.ModelPathUtils;
import ca.sciencestudio.util.web.BindAndValidateUtils;
import ca.sciencestudio.util.web.GeneralResponse;
import ca.sciencestudio.util.web.GenericResponse;

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
	
	@ResponseBody
	@RequestMapping(value = "/projects/form/add*", method = RequestMethod.POST)
	public GenericResponse<?> postProjectFormAdd(HttpServletRequest request) {
		
		ProjectFormBacker projectFormBacker = new ProjectFormBacker();
		BindException errors = BindAndValidateUtils.buildBindException(projectFormBacker);
		
		if(!SecurityUtil.hasAuthority(ROLE.ADMIN_PROJECTS)) {
			errors.reject("permission.denied", "Permission denied.");
			return new GeneralResponse(errors);
		}
		
		errors = BindAndValidateUtils.bindAndValidate(projectFormBacker, request, projectFormBackerValidator);	
		if(errors.hasErrors()) {
			return new GeneralResponse(errors);
		}
		
		Project project = projectFormBacker.toProject();
		
		projectDAO.add(project);
		
		
		GenericResponse<Map<String,String>> response = new GenericResponse<Map<String,String>>(new HashMap<String,String>());	
		response.getResponse().put("viewUrl", getModelPath(request) + ModelPathUtils.getProjectsPath(project.getGid(), ".html"));
		return response;
	}
	
	@ResponseBody
	@RequestMapping(value = "/projects/{projectGid}/form/edit*", method = RequestMethod.POST)
	public GenericResponse<?> postProjectFormEdit(@PathVariable String projectGid, HttpServletRequest request) {
		
		BindException errors = BindAndValidateUtils.buildBindException();
		
		Project project = projectDAO.get(projectGid);
		if(project == null) {
			errors.reject("project.notfound", "Project not found.");
			return new GeneralResponse(errors);
		}
		
		ROLE admin = ROLE.ADMIN_PROJECTS;
		
		if(!SecurityUtil.hasAuthority(admin)) {
			errors.reject("permission.denied", "Permission denied.");
			return new GeneralResponse(errors);
		}
		
		ProjectFormBacker projectFormBacker = new ProjectFormBacker(project);
		
		errors = BindAndValidateUtils.bindAndValidate(projectFormBacker, request, projectFormBackerValidator);
		if(errors.hasErrors()) {
			return new GeneralResponse(errors);
		}
		
		projectDAO.edit(projectFormBacker.toProject());
		
		
		GenericResponse<Map<String,String>> response = new GenericResponse<Map<String,String>>(new HashMap<String,String>());
		response.getResponse().put("message", "Project saved.");		
		return response;
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
