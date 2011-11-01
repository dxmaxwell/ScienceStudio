/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ScanFormController class.
 *     
 */
package ca.sciencestudio.service.session.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.EditResult;
import ca.sciencestudio.model.session.dao.ScanAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.session.backers.ScanFormBacker;
import ca.sciencestudio.service.utilities.ModelPathUtils;
import ca.sciencestudio.util.web.FormResponseMap;

/**
 * @author maxweld
 *
 */
@Controller
public class ScanFormController {
	
	private String facility;
	
	private ScanAuthzDAO scanAuthzDAO;
	
//
//	User added Scans are not currently supported.	
//	
//	@ResponseBody
//	@RequestMapping(value = ModelPathUtils.SCAN_PATH + "/form/add*}", method = RequestMethod.GET)
//	public FormResponseMap scanFormAdd(ScanFormBacker scan, Errors errors) {
//		// Bind errors are intentionally ignored, however the argument must be 
//		// present to avoid automatic delegation to the exception handler.
//				
//		String user = SecurityUtil.getPersonGid();
//		
//		AddResult result = scanAuthzDAO.add(user, scan, facility).get();
//		
//		FormResponseMap response = new FormResponseMap(ScanFormBacker.transformResult(result));
//		
//		if(response.isSuccess()) {				
//			response.put("viewUrl", ModelPathUtils.getModelScanPath("/", scan.getGid(), ".html"));
//		}
//		
//		return response;
//	}

	@ResponseBody
	@RequestMapping(value = ModelPathUtils.SCAN_PATH + "/form/edit*", method = RequestMethod.POST)
	public FormResponseMap scanFormEdit(ScanFormBacker scan, Errors errors) {
		// Bind errors are intentionally ignored, however the argument must be 
		// present to avoid automatic delegation to the exception handler.
		
		String user = SecurityUtil.getPersonGid();
		
		EditResult result = scanAuthzDAO.edit(user, scan).get();
		
		FormResponseMap response = new FormResponseMap(ScanFormBacker.transformResult(result));
		
		if(response.isSuccess()) {
			response.setMessage("Scan Saved");
		}
		
		return response;
	}
	
//
//	User removal of Scans is not currently supported. 
//	
//	@ResponseBody
//	@RequestMapping(value = ModelPathUtils.SCAN_PATH + "/form/remove*", method = RequestMethod.POST)
//	public FormResponseMap scanFormRemove(@RequestParam String gid) {
//		
//		String user = SecurityUtil.getPersonGid();
//		
//		boolean success;
//		try {
//			success = scanAuthzDAO.remove(user, gid).get();
//		}
//		catch(AuthorizationException e) {
//			return new FormResponseMap(false, "Not Permitted");
//		}
//		
//		FormResponseMap response = new FormResponseMap(success);
//		
//		if(response.isSuccess()) {				
//			response.put("viewUrl", ModelPathUtils.getModelScanPath(".html"));
//		}
//		
//		return response;
//	}
	
	public String getFacility() {
		return facility;
	}
	public void setFacility(String facility) {
		this.facility = facility;
	}
	
	public ScanAuthzDAO getScanAuthzDAO() {
		return scanAuthzDAO;
	}
	public void setScanAuthzDAO(ScanAuthzDAO scanAuthzDAO) {
		this.scanAuthzDAO = scanAuthzDAO;
	}
}
