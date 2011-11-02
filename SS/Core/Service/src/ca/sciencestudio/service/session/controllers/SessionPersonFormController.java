/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionFormController class.
 *     
 */
package ca.sciencestudio.service.session.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.model.person.dao.PersonAuthzDAO;
import ca.sciencestudio.model.session.SessionPerson.Role;
import ca.sciencestudio.model.session.dao.SessionPersonAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.controllers.AbstractModelController;
import ca.sciencestudio.service.session.backers.SessionPersonFormBacker;
import ca.sciencestudio.service.utilities.ModelPathUtils;
import ca.sciencestudio.util.exceptions.AuthorizationException;
import ca.sciencestudio.util.rest.AddResult;
import ca.sciencestudio.util.rest.EditResult;
import ca.sciencestudio.util.rest.RemoveResult;
import ca.sciencestudio.util.web.FormResponseMap;

/**
 * @author maxweld
 *
 */
@Controller
public class SessionPersonFormController extends AbstractModelController {
	
	private PersonAuthzDAO personAuthzDAO;
	
	private SessionPersonAuthzDAO sessionPersonAuthzDAO;
	
	@ResponseBody
	@RequestMapping(value = ModelPathUtils.SESSION_PERSON_PATH + "/form/search*", method = RequestMethod.POST)
	public List<SessionPersonFormBacker> sessionPersonFormSearch(@RequestParam String name, @RequestParam String session) {
		
		String user = SecurityUtil.getPersonGid();

		List<SessionPersonFormBacker> sessionPersons = new ArrayList<SessionPersonFormBacker>();
		
		List<Person> persons = personAuthzDAO.getAllByName(user, name).get();
		for(Person person : persons) {
			sessionPersons.add(new SessionPersonFormBacker(session, Role.OBSERVER, person));
		}
		
		return sessionPersons;
	}
	
	@ResponseBody
	@RequestMapping(value = ModelPathUtils.SESSION_PERSON_PATH + "/form/add*", method = RequestMethod.POST)
	public FormResponseMap sessionPersonFormAdd(SessionPersonFormBacker sessionPerson, Errors errors) {
		// Bind errors are intentionally ignored, however the argument must be 
		// present to avoid automatic delegation to the exception handler.
				
		String user = SecurityUtil.getPersonGid();
		
		AddResult result = sessionPersonAuthzDAO.add(user, sessionPerson).get();
		
		FormResponseMap response = new FormResponseMap(SessionPersonFormBacker.transformResult(result));
		
		if(response.isSuccess()) {				
			response.put("viewUrl", ModelPathUtils.getModelSessionPersonPath("/", sessionPerson.getGid(), ".html"));
		}
		
		return response;
	}

	@ResponseBody
	@RequestMapping(value = ModelPathUtils.SESSION_PERSON_PATH + "/form/edit*", method = RequestMethod.POST)
	public FormResponseMap sessionPersonFormEdit(SessionPersonFormBacker sessionPerson, Errors errors) {
		// Bind errors are intentionally ignored, however the argument must be 
		// present to avoid automatic delegation to the exception handler.
		
		String user = SecurityUtil.getPersonGid();
		
		EditResult result = sessionPersonAuthzDAO.edit(user, sessionPerson).get();
		
		FormResponseMap response = new FormResponseMap(SessionPersonFormBacker.transformResult(result));
		
		if(response.isSuccess()) {
			response.setMessage("Person Saved");
		}
		
		return response;
	}
	
	@ResponseBody
	@RequestMapping(value = ModelPathUtils.SESSION_PERSON_PATH + "/form/remove*", method = RequestMethod.POST)
	public FormResponseMap sessionPersonFormRemove(@RequestParam String gid) {
		
		String user = SecurityUtil.getPersonGid();
		
		RemoveResult result;
		try {
			result = sessionPersonAuthzDAO.remove(user, gid).get();
		}
		catch(AuthorizationException e) {
			return new FormResponseMap(false, "Not Permitted");
		}
		
		return new FormResponseMap(result);
	}

	public PersonAuthzDAO getPersonAuthzDAO() {
		return personAuthzDAO;
	}
	public void setPersonAuthzDAO(PersonAuthzDAO personAuthzDAO) {
		this.personAuthzDAO = personAuthzDAO;
	}

	public SessionPersonAuthzDAO getSessionPersonAuthzDAO() {
		return sessionPersonAuthzDAO;
	}
	public void setSessionPersonAuthzDAO(SessionPersonAuthzDAO sessionPersonAuthzDAO) {
		this.sessionPersonAuthzDAO = sessionPersonAuthzDAO;
	}
}
