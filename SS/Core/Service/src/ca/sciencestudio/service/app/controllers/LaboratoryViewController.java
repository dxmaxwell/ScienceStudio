/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		LaboratoryViewController class.
 *     
 */
package ca.sciencestudio.service.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sciencestudio.model.facility.Laboratory;
import ca.sciencestudio.model.facility.dao.LaboratoryDAO;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.SessionDAO;

/**
 * @author maxweld
 *
 */
@Controller
public class LaboratoryViewController {

	@Autowired
	private SessionDAO sessionDAO;
	
	@Autowired
	private LaboratoryDAO laboratoryDAO;
	
	@RequestMapping(value = "/laboratory/view.html", method = RequestMethod.GET)
	public String joinSession(@RequestParam int sessionId, ModelMap model) {
				
		Session session = sessionDAO.getSessionById(sessionId);
		if(session == null) {
			model.put("error", "Session not found.");
			return "page/error";
		}
		
		int laboratoryId = session.getLaboratoryId();
		Laboratory laboratory = laboratoryDAO.getLaboratoryById(laboratoryId);
		if(laboratory == null) {
			model.put("error", "Laboratory not found.");
			return "page/error";
		}
		
		String viewUrl = laboratory.getViewUrl();
		if((viewUrl == null) || (viewUrl.length() == 0)) {
			model.put("error", "Laboratory view URL not found.");
			return "page/error";
		}
		
		return "redirect:" + viewUrl + "?sessionId=" + sessionId;
	}

	public SessionDAO getSessionDAO() {
		return sessionDAO;
	}
	public void setSessionDAO(SessionDAO sessionDAO) {
		this.sessionDAO = sessionDAO;
	}
	
	public LaboratoryDAO getLaboratoryDAO() {
		return laboratoryDAO;
	}
	public void setLaboratoryDAO(LaboratoryDAO laboratoryDAO) {
		this.laboratoryDAO = laboratoryDAO;
	}
}
