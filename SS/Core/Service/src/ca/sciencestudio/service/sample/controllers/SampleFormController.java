/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SampleFormController class.
 *     
 */
package ca.sciencestudio.service.sample.controllers;

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
import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.sample.dao.SampleDAO;
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.controllers.AbstractModelController;
import ca.sciencestudio.service.sample.backers.SampleFormBacker;
import ca.sciencestudio.service.sample.validators.SampleFormBackerValidator;
import ca.sciencestudio.service.utilities.ModelPathUtils;
import ca.sciencestudio.util.web.BindAndValidateUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class SampleFormController extends AbstractModelController {

	@Autowired
	private ProjectDAO projectDAO;
	
	@Autowired
	private SampleDAO sampleDAO;
	
	@Autowired
	private SampleFormBackerValidator sampleFormBackerValidator;
	
	@RequestMapping(value = "/project/{projectId}/samples/form/add.{format}", method = RequestMethod.POST)
	public String postSampleFormAdd(@PathVariable int projectId, @PathVariable String format, HttpServletRequest request, ModelMap model) {
		
		SampleFormBacker sampleFormBacker = new SampleFormBacker(projectId);
		BindException errors = BindAndValidateUtils.buildBindException(sampleFormBacker);
		model.put("errors", errors);
		
		String responseView = "response-" + format;
		
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		Object exptr = AuthorityUtil.buildProjectExperimenterAuthority(projectId);
		
		if(!SecurityUtil.hasAnyAuthority(admin, exptr)) {
			errors.reject("permission.denied", "Permission denied.");
			return responseView;
		}
		
		Project project = projectDAO.getProjectById(projectId);
		if(project == null) {
			errors.reject("project.notfound", "Project not found.");
			return responseView;
		}
		
		errors = BindAndValidateUtils.bindAndValidate(sampleFormBacker, request, sampleFormBackerValidator);
		if(errors.hasErrors()) {
			model.put("errors",  errors);
			return responseView;
		}
		
		int sampleId = sampleDAO.addSample(sampleFormBacker.createSample(sampleDAO));
		
		Map<String,String> response = new HashMap<String,String>();
		response.put("viewUrl", getModelPath(request) + ModelPathUtils.getSamplePath(sampleId, ".html"));
		
		model.put("response", response);
		return responseView;
	}
	
	@RequestMapping(value = "/sample/{sampleId}/form/edit.{format}", method = RequestMethod.POST)
	public String postSampleFormEdit(@PathVariable int sampleId, @PathVariable String format, HttpServletRequest request, ModelMap model) {

		BindException errors = BindAndValidateUtils.buildBindException();
		model.put("errors", errors);

		String responseView = "response-" + format;
		
		Sample sample = sampleDAO.getSampleById(sampleId);
		if(sample == null) {
			errors.reject("sample.notfound", "Sample not found.");
			return responseView;
		}
		
		Project project = projectDAO.getProjectById(sample.getProjectId());
		if(project == null) {
			errors.reject("project.notfound", "Project not found.");
			return responseView;
		}
		
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		Object exptr = AuthorityUtil.buildProjectExperimenterAuthority(project.getId());
		
		if(!SecurityUtil.hasAnyAuthority(admin, exptr)) {
			errors.reject("permission.denied", "Permission denied.");
			return responseView;
		}
	
		SampleFormBacker sampleFormBacker = new SampleFormBacker(sample);
		sampleFormBacker.clearHazards();
		
		errors = BindAndValidateUtils.bindAndValidate(sampleFormBacker, request, sampleFormBackerValidator);
		if(errors.hasErrors()) {
			model.put("errors", errors);
			return responseView;
		}

		sampleDAO.editSample(sampleFormBacker.createSample(sampleDAO));
		
		Map<String,String> response = new HashMap<String,String>();
		response.put("message", "Sample saved.");
		
		model.put("response", response);
		return responseView;
	}
	
	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}
	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public SampleDAO getSampleDAO() {
		return sampleDAO;
	}
	public void setSampleDAO(SampleDAO sampleDAO) {
		this.sampleDAO = sampleDAO;
	}

	public SampleFormBackerValidator getSampleFormBackerValidator() {
		return sampleFormBackerValidator;
	}
	public void setSampleFormBackerValidator(SampleFormBackerValidator sampleFormBackerValidator) {
		this.sampleFormBackerValidator = sampleFormBackerValidator;
	}
}
