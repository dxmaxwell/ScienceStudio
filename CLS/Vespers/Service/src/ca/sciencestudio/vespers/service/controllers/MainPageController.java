/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     MainPageController class.
 *     
 */
package ca.sciencestudio.vespers.service.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.model.person.dao.PersonAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;

/**
 * @author maxweld
 *
 */
@Controller
public class MainPageController extends AbstractBeamlineControlController {
	
	private static final String MODEL_KEY_PERSON_GID = "personGid";
	
	private static final String VALUE_KEY_SESSION_GID = "sessionGid";
	
	private static final String SUCCESS_VIEW = "page/main";
	private static final String FAILURE_VIEW = "page/error";

	private PersonAuthzDAO personAuthzDAO;
	
	private String successView = SUCCESS_VIEW;
	
	@RequestMapping(value = "/main.html", method = RequestMethod.GET)
	public String handleRequest(@RequestParam("session") String sessionGid, ModelMap model) {
				
		String currentSessionGid = (String) beamlineSessionProxy.get(VALUE_KEY_SESSION_GID);
		
		if((currentSessionGid == null) || (!currentSessionGid.equalsIgnoreCase(sessionGid))) {
			model.put("error", "Session has not been started.");
			model.put("errorMessage", "<a href=\"\">Try Again</a>");
			return FAILURE_VIEW;
		}
		
		if(!canObserveBeamline()) {
			model.put("error", "Not permitted to view session.");
			model.put("errorMessage", "<a href=\"\">Try Again</a>");
			return FAILURE_VIEW;
		}
			
		if(canControlBeamline()) {
			String user = SecurityUtil.getPersonGid();
			Person person = personAuthzDAO.get(user, user).get();
			if(person != null) {
				beamlineSessionProxy.setControllerIfNotSet(person);
			}
		}
		
		model.put(MODEL_KEY_PERSON_GID, SecurityUtil.getPersonGid());
		return successView;
	}
	
	public String getSuccessView() {
		return successView;
	}
	public void setSuccessView(String successView) {
		this.successView = successView;
	}

	public PersonAuthzDAO getPersonAuthzDAO() {
		return personAuthzDAO;
	}
	public void setPersonAuthzDAO(PersonAuthzDAO personAuthzDAO) {
		this.personAuthzDAO = personAuthzDAO;
	}
}
