/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     PersonAuthzController class.
 *     
 */
package ca.sciencestudio.rest.service.person.controllers;

import java.util.Collections;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.dao.ModelBasicDAO;
import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.model.person.dao.PersonBasicDAO;
import ca.sciencestudio.model.person.validators.PersonValidator;
import ca.sciencestudio.model.validators.ModelValidator;
import ca.sciencestudio.rest.service.controllers.AbstractModelAuthzController;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 * 
 *
 */
@Controller
public class PersonAuthzController extends AbstractModelAuthzController<Person> {

	private static final String PERSON_MODEL_PATH = "/persons";
		
	private PersonBasicDAO personBasicDAO;
	
	private PersonValidator personValidator;
		
	//
	//	Adding, Editing and Removing Persons currently only done by administrator. No REST API implemented. 
	//

	@ResponseBody
	@RequestMapping(value = PERSON_MODEL_PATH + "/{gid}*", method = RequestMethod.GET)
	public Object get(@PathVariable String gid, HttpServletResponse response) throws Exception {
		return doGet(gid, response);
	}
	
	@ResponseBody
	@RequestMapping(value = PERSON_MODEL_PATH + "/whois*", method = RequestMethod.GET, params = { "username", "facility" })
	public Object getByUsername(@RequestParam String username, @RequestParam String facility, HttpServletResponse response) throws Exception {
		Person person;
		try {
			person = personBasicDAO.getByUsername(username, facility);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return Collections.emptyMap();
		}
		
		if(person == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.emptyMap();
		}
		return person;
	}
		
	@ResponseBody
	@RequestMapping(value = PERSON_MODEL_PATH + "*", method = RequestMethod.GET, params = "name")
	public Object getAllByName(@RequestParam String name, HttpServletResponse response) {
		if((name == null) || (name.trim().length() == 0)) {
			return Collections.emptyList();
		}
		
		try {
			return personBasicDAO.getAllByName(name);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return Collections.emptyList();
		}
	}
	
	@Override
	public String getModelPath() {
		return PERSON_MODEL_PATH;
	}
	
	@Override
	public ModelBasicDAO<Person> getModelBasicDAO() {
		return getPersonBasicDAO();
	}
	
	@Override
	public ModelValidator<Person> getModelValidator() {
		return personValidator;
	}

	public PersonBasicDAO getPersonBasicDAO() {
		return personBasicDAO;
	}
	public void setPersonBasicDAO(PersonBasicDAO personBasicDAO) {
		this.personBasicDAO = personBasicDAO;
	}

	public PersonValidator getPersonValidator() {
		return personValidator;
	}
	public void setPersonValidator(PersonValidator personValidator) {
		this.personValidator = personValidator;
	}
}
