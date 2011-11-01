/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionFormController class.
 *     
 */
package ca.sciencestudio.service.session.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.AddResult;
import ca.sciencestudio.model.EditResult;
import ca.sciencestudio.model.session.dao.SessionAuthzDAO;

import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.controllers.AbstractModelController;
import ca.sciencestudio.service.session.backers.SessionFormBacker;
import ca.sciencestudio.service.utilities.ModelPathUtils;
import ca.sciencestudio.util.exceptions.AuthorizationException;
import ca.sciencestudio.util.web.FormResponseMap;

/**
 * @author maxweld
 *
 */
@Controller
public class SessionFormController extends AbstractModelController {

	private String facility;

	private SessionAuthzDAO sessionAuthzDAO;	

	@ResponseBody
	@RequestMapping(value = ModelPathUtils.SESSION_PATH + "/form/add*", method = RequestMethod.POST)
	public FormResponseMap sessionsFormAdd(SessionFormBacker session, Errors errors) {
		// Bind errors are intentionally ignored, however the argument must be 
		// present to avoid automatic delegation to the exception handler.
				
		String user = SecurityUtil.getPersonGid();
		
		AddResult result = sessionAuthzDAO.add(user, session, facility).get();
		
		FormResponseMap response = new FormResponseMap(SessionFormBacker.transformResult(result));
		
		if(response.isSuccess()) {				
			response.put("viewUrl", ModelPathUtils.getModelSessionPath("/", session.getGid(), ".html"));
		}
		
		return response;
	}

	@ResponseBody
	@RequestMapping(value = ModelPathUtils.SESSION_PATH + "/form/edit*", method = RequestMethod.POST)
	public FormResponseMap sessionFormEdit(SessionFormBacker session, Errors errors) {
		// Bind errors are intentionally ignored, however the argument must be 
		// present to avoid automatic delegation to the exception handler.
		
		String user = SecurityUtil.getPersonGid();
		
		EditResult result = sessionAuthzDAO.edit(user, session).get();
		
		FormResponseMap response = new FormResponseMap(SessionFormBacker.transformResult(result));

		if(response.isSuccess()) {
			response.setMessage("Session Saved");
		}
		
		return response;
	}
	
	@ResponseBody
	@RequestMapping(value = ModelPathUtils.SESSION_PATH + "/form/remove*", method = RequestMethod.POST)
	public FormResponseMap sessionFormRemove(@RequestParam String gid) {
		
		String user = SecurityUtil.getPersonGid();
		
		boolean success;
		try {
			success = sessionAuthzDAO.remove(user, gid).get();
		}
		catch(AuthorizationException e) {
			return new FormResponseMap(false, "Not Permitted");
		}
		
		FormResponseMap response = new FormResponseMap(success);
		
		if(response.isSuccess()) {				
			response.put("viewUrl", ModelPathUtils.getModelSessionPath(".html"));
		}
		
		return response;
	}

	public String getFacility() {
		return facility;
	}
	public void setFacility(String facility) {
		this.facility = facility;
	}

	public SessionAuthzDAO getSessionAuthzDAO() {
		return sessionAuthzDAO;
	}
	public void setSessionAuthzDAO(SessionAuthzDAO sessionAuthzDAO) {
		this.sessionAuthzDAO = sessionAuthzDAO;
	}
}
