/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SampleFormController class.
 *     
 */
package ca.sciencestudio.service.sample.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.AddResult;
import ca.sciencestudio.model.EditResult;
import ca.sciencestudio.model.sample.dao.SampleAuthzDAO;
import ca.sciencestudio.service.controllers.AbstractModelController;
import ca.sciencestudio.service.sample.backers.SampleFormBacker;
import ca.sciencestudio.service.utilities.ModelPathUtils;
import ca.sciencestudio.util.exceptions.AuthorizationException;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.web.FormResponseMap;

/**
 * @author maxweld
 *
 */
@Controller
public class SampleFormController extends AbstractModelController {

	private String facility;
	
	private SampleAuthzDAO sampleAuthzDAO;
		
	@ResponseBody
	@RequestMapping(value = ModelPathUtils.SAMPLE_PATH + "/form/add*", method = RequestMethod.POST)
	public FormResponseMap sampleFormAdd(SampleFormBacker sample, Errors errors) {
		// Bind errors are intentionally ignored, however the argument must be 
		// present to avoid automatic delegation to the exception handler.
				
		String user = SecurityUtil.getPersonGid();
		
		AddResult result = sampleAuthzDAO.add(user, sample, facility).get();
		
		FormResponseMap response = new FormResponseMap(SampleFormBacker.transformResult(result));
		
		if(response.isSuccess()) {				
			response.put("viewUrl", ModelPathUtils.getModelSamplePath("/", sample.getGid(), ".html"));
		}
		
		return response;
	}
	
	@ResponseBody
	@RequestMapping(value = ModelPathUtils.SAMPLE_PATH + "/form/edit*", method = RequestMethod.POST)
	public FormResponseMap postSampleFormEdit(SampleFormBacker sample) {
		// Bind errors are intentionally ignored, however the argument must be 
		// present to avoid automatic delegation to the exception handler.
		
		String user = SecurityUtil.getPersonGid();
		
		EditResult result = sampleAuthzDAO.edit(user, sample).get();
		
		FormResponseMap response = new FormResponseMap(SampleFormBacker.transformResult(result));
		
		if(response.isSuccess()) {
			response.setMessage("Sample Saved");
		}
		
		return response;
	}
	
	@ResponseBody
	@RequestMapping(value = ModelPathUtils.SAMPLE_PATH + "/form/remove*", method = RequestMethod.POST)
	public FormResponseMap sampleFormRemove(@RequestParam String gid) {
		
		String user = SecurityUtil.getPersonGid();
		
		boolean success;
		try {
			success = sampleAuthzDAO.remove(user, gid).get();
		}
		catch(AuthorizationException e) {
			return new FormResponseMap(false, "Not Permitted");
		}
		
		FormResponseMap response = new FormResponseMap(success);
		
		if(response.isSuccess()) {				
			response.put("viewUrl", ModelPathUtils.getModelSamplePath(".html"));
		}
		
		return response;
	}

	public String getFacility() {
		return facility;
	}
	public void setFacility(String facility) {
		this.facility = facility;
	}

	public SampleAuthzDAO getSampleAuthzDAO() {
		return sampleAuthzDAO;
	}
	public void setSampleAuthzDAO(SampleAuthzDAO sampleAuthzDAO) {
		this.sampleAuthzDAO = sampleAuthzDAO;
	}
}
