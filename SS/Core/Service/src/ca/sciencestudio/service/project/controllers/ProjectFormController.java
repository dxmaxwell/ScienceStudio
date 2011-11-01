/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectFormController class.
 *     
 */
package ca.sciencestudio.service.project.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.AddResult;
import ca.sciencestudio.model.EditResult;
import ca.sciencestudio.model.project.dao.ProjectAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.controllers.AbstractModelController;
import ca.sciencestudio.service.project.backers.ProjectFormBacker;
import ca.sciencestudio.service.utilities.ModelPathUtils;
import ca.sciencestudio.util.exceptions.AuthorizationException;
import ca.sciencestudio.util.web.FormResponseMap;

/**
 * @author maxweld
 *
 */
@Controller
public class ProjectFormController extends AbstractModelController {

	private String facility;
	
	private ProjectAuthzDAO projectAuthzDAO;
	
	@ResponseBody
	@RequestMapping(value = ModelPathUtils.PROJECT_PATH + "/form/add*", method = RequestMethod.POST)
	public FormResponseMap projectFormAdd(ProjectFormBacker project, Errors errors) {
		// Bind errors are intentionally ignored, however the argument must be 
		// present to avoid automatic delegation to the exception handler.
				
		String user = SecurityUtil.getPersonGid();
		
		AddResult result = projectAuthzDAO.add(user, project, facility).get();
		
		FormResponseMap response = new FormResponseMap(ProjectFormBacker.transformResult(result));
		
		if(response.isSuccess()) {				
			response.put("viewUrl", ModelPathUtils.getModelProjectPath("/", project.getGid(), ".html"));
		}
		
		return response;
	}
	
	@ResponseBody
	@RequestMapping(value = ModelPathUtils.PROJECT_PATH  + "/form/edit*", method = RequestMethod.POST)
	public FormResponseMap projectFormEdit(ProjectFormBacker project, Errors errors) {
		// Bind errors are intentionally ignored, however the argument must be 
		// present to avoid automatic delegation to the exception handler.
		
		String user = SecurityUtil.getPersonGid();
		
		EditResult result = projectAuthzDAO.edit(user, project).get();
		
		FormResponseMap response = new FormResponseMap(ProjectFormBacker.transformResult(result));
		
		if(response.isSuccess()) {
			response.setMessage("Project Saved");
		}
		
		return response;
	}

	@ResponseBody
	@RequestMapping(value = ModelPathUtils.PROJECT_PATH + "/form/remove*", method = RequestMethod.POST)
	public FormResponseMap projectFormRemove(@RequestParam String gid) {
		
		String user = SecurityUtil.getPersonGid();
		
		boolean success;
		try {
			success = projectAuthzDAO.remove(user, gid).get();
		}
		catch(AuthorizationException e) {
			return new FormResponseMap(false, "Not Permitted");
		}
		
		FormResponseMap response = new FormResponseMap(success);
		
		if(response.isSuccess()) {				
			response.put("viewUrl", ModelPathUtils.getModelProjectPath(".html"));
		}
		
		return response;
	}
	
	public String getFacility() {
		return facility;
	}
	public void setFacility(String facility) {
		this.facility = facility;
	}

	public ProjectAuthzDAO getProjectAuthzDAO() {
		return projectAuthzDAO;
	}
	public void setProjectAuthzDAO(ProjectAuthzDAO projectAuthzDAO) {
		this.projectAuthzDAO = projectAuthzDAO;
	}
}
