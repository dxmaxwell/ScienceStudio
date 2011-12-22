/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		LaboratoryViewController class.
 *     
 */
package ca.sciencestudio.service.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sciencestudio.model.facility.Laboratory;
import ca.sciencestudio.model.facility.dao.LaboratoryAuthzDAO;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.SessionAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;

/**
 * 
 * Not sure the authz work should be done here or at the controller for the viewurl
 * 
 * @author maxweld
 *
 */
@Controller
public class LaboratoryViewController {

	private static final String ERROR_VIEW = "page/error";
	
	private SessionAuthzDAO sessionAuthzDAO;	

	private LaboratoryAuthzDAO laboratoryAuthzDAO;
	
	@RequestMapping(value = "/laboratory/view.html", params = "session")
	public String joinSession(@RequestParam("session") String sessionGid, ModelMap model) {
		
		String user = SecurityUtil.getPersonGid();
		
		Session session = sessionAuthzDAO.get(user, sessionGid).get();
		if(session == null) {
			model.put("error", "Session not found.");
			return ERROR_VIEW;
		}
		
		Laboratory laboratory = laboratoryAuthzDAO.get(user, session.getLaboratoryGid()).get();
		if(laboratory == null) {
			model.put("error", "Laboratory not found.");
			return ERROR_VIEW;
		}
		
		String viewUrl = laboratory.getViewUrl();
		if((viewUrl == null) || (viewUrl.length() == 0)) {
			model.put("error", "Laboratory view URL not found.");
			return ERROR_VIEW;
		}
		
		String redirectUrl = "redirect:" + viewUrl + "?session=" + sessionGid;
		
		String authc = SecurityUtil.getAuthenticator();
		if((authc != null) && (authc.length() > 0)) {
			redirectUrl += "&authc=" + authc;
		}
		
		return redirectUrl;
	}

	public SessionAuthzDAO getSessionAuthzDAO() {
		return sessionAuthzDAO;
	}
	public void setSessionAuthzDAO(SessionAuthzDAO sessionAuthzDAO) {
		this.sessionAuthzDAO = sessionAuthzDAO;
	}

	public LaboratoryAuthzDAO getLaboratoryAuthzDAO() {
		return laboratoryAuthzDAO;
	}
	public void setLaboratoryAuthzDAO(LaboratoryAuthzDAO laboratoryAuthzDAO) {
		this.laboratoryAuthzDAO = laboratoryAuthzDAO;
	}
}
