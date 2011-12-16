/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      MainPageController class.
 */
package ca.sciencestudio.nanofab.service.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.model.person.dao.PersonAuthzDAO;

/**
 * @author maxweld
 *
 */
@Controller
public class MainPageController extends AbstractLaboratoryControlController {

	private static final String MODEL_KEY_PERSON_GID = "personGid";
	
	private static final String SUCCESS_VIEW = "page/main";
	private static final String FAILURE_VIEW = "page/error";

	private PersonAuthzDAO personAuthzDAO;
	
	@RequestMapping(value = "/main.html", method = RequestMethod.GET)
	public String getMainPage(@RequestParam("session") String sessionGid, ModelMap model) {
		
		String runningSessionGid = nanofabSessionStateMap.getRunningSessionGid();
		
		if((runningSessionGid == null) || (!runningSessionGid.equalsIgnoreCase(sessionGid))) {
			model.put("error", "Session has not been started.");
			model.put("errorMessage", "<a href=\"\">Try Again</a>");
			return FAILURE_VIEW;
		}
		
		if(!canObserveLaboratory()) {
			model.put("error", "Not permitted to view session.");
			model.put("errorMessage", "<a href=\"\">Try Again</a>");
			return FAILURE_VIEW;
		}
		
		String user = SecurityUtil.getPersonGid();
		
		if(canControlLaboratory()) {
			Person person = personAuthzDAO.get(user, user).get();
			if(person != null) {
				nanofabSessionStateMap.setControllerIfNotSet(person);
			}
		}
		
		model.put(MODEL_KEY_PERSON_GID, user);
		return SUCCESS_VIEW;
	}

	public PersonAuthzDAO getPersonAuthzDAO() {
		return personAuthzDAO;
	}
	public void setPersonAuthzDAO(PersonAuthzDAO personAuthzDAO) {
		this.personAuthzDAO = personAuthzDAO;
	}
}
