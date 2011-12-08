/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ControlBeamlineController class.
 *     
 */
package ca.sciencestudio.vespers.service.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.model.person.dao.PersonAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.web.FormResponseMap;

/**
 * @author maxweld
 *
 */
@Controller
public class ControlBeamlineController extends AbstractBeamlineControlController {
	
	private PersonAuthzDAO personAuthzDAO;
		
	@ResponseBody
	@RequestMapping(value = "/session/control*", method = RequestMethod.GET)
	public FormResponseMap handleRequest() {
		
		if(!canControlBeamline()) {
			return new FormResponseMap(false, "Not permitted to control session.");
		}
		
		String user = SecurityUtil.getPersonGid();
		Person person = personAuthzDAO.get(user, user).get();
		if(person == null) {
			return new FormResponseMap(false, "Controller person not found.");
		}
		
		beamlineSessionProxy.setController(person);
		return new FormResponseMap(true);
	}

	public PersonAuthzDAO getPersonAuthzDAO() {
		return personAuthzDAO;
	}
	public void setPersonAuthzDAO(PersonAuthzDAO personAuthzDAO) {
		this.personAuthzDAO = personAuthzDAO;
	}
}
