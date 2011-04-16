/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ScanFormController class.
 *     
 */
package ca.sciencestudio.service.session.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectDAO;
import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.dao.ScanDAO;
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.session.backers.ScanFormBacker;
import ca.sciencestudio.service.session.validators.ScanFormValidator;
import ca.sciencestudio.util.web.BindAndValidateUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class ScanFormController {
	
	@Autowired
	private ScanDAO scanDAO;
	
	@Autowired
	private ProjectDAO projectDAO;
	
	@Autowired
	private ScanFormValidator scanFormValidator;
	
	@RequestMapping(value = "/scan/{scanId}/form.{format}", method = RequestMethod.GET)
	public String getScanForm(@PathVariable int scanId, @PathVariable String format, HttpServletResponse response, ModelMap model) {
		
		BindException errors = BindAndValidateUtils.buildBindException();
		model.put("errors", errors);
		
		String responseView = "response-" + format;
		
		Project project = projectDAO.getProjectByScanId(scanId);
		if(project == null) {
			errors.reject("project.notfound", "Project not found.");
			return responseView;
		}
		
		Object data = AuthorityUtil.ROLE_ADMIN_DATA;
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		Object group = AuthorityUtil.buildProjectGroupAuthority(project.getId());
		
		if(!SecurityUtil.hasAnyAuthority(group, data, admin)) {
			errors.reject("permission.denied",  "Not permitted to read scan.");
			return responseView;
		}
		
		Scan scan = scanDAO.getScanById(scanId);
		if(scan == null) {
			errors.reject("scan.notfound", "Scan not found.");
			return responseView;
		}
		
		model.put("response", new ScanFormBacker(scan));
		return responseView;
	}
	
	@RequestMapping(value = "/scan/{scanId}/form/edit.{format}", method = RequestMethod.POST)
	public String postScanEdit(@PathVariable int scanId, @PathVariable String format, HttpServletRequest request, ModelMap model) {

		BindException errors = BindAndValidateUtils.buildBindException();
		model.put("errors", errors);
		
		String responseView = "response-" + format;
		
		Project project = projectDAO.getProjectByScanId(scanId);
		if(project == null) {
			errors.reject("project.notfound", "Project not found.");
			return responseView;
		}
		
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		Object group = AuthorityUtil.buildProjectGroupAuthority(project.getId());
		
		if(!SecurityUtil.hasAnyAuthority(group, admin)) {
			errors.reject("permission.denied", "Not permitted to edit scan.");
			return responseView;
		}
		
		Scan scan = scanDAO.getScanById(scanId);
		
		if(scan == null) {
			errors.reject("permission.denied", "Scan not found.");
			return responseView;
		}
		
		ScanFormBacker scanFormBacker = new ScanFormBacker(scan);
		errors = BindAndValidateUtils.bindAndValidate(scanFormBacker, request, scanFormValidator);
		
		if(errors.hasErrors()) {
			model.put("errors", errors);
			return responseView;
		}
		
		// Ensure only scan name is modified //
		scan.setName(scanFormBacker.getName());
		///////////////////////////////////////
		
		scanDAO.editScan(scan);
		
		Map<String,String> response = new HashMap<String,String>();
		response.put("message", "Scan saved.");
		
		model.put("response", response);
		return responseView;
	}

	public ScanDAO getScanDAO() {
		return scanDAO;
	}
	public void setScanDAO(ScanDAO scanDAO) {
		this.scanDAO = scanDAO;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}
	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public ScanFormValidator getScanFormValidator() {
		return scanFormValidator;
	}
	public void setScanFormValidator(ScanFormValidator scanFormValidator) {
		this.scanFormValidator = scanFormValidator;
	}
}
