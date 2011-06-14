/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     PersonController class.
 *     
 */
package ca.sciencestudio.rest.service.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.Person;
import ca.sciencestudio.model.dao.PersonDAO;
import ca.sciencestudio.model.validators.PersonValidator;
import ca.sciencestudio.rest.service.controllers.support.AbstractModelController;

/**
 * @author maxweld
 * 
 *
 */
@Controller
public class PersonController extends AbstractModelController<Person, PersonDAO, PersonValidator> {

	private static final String PERSON_MODEL_URL = "/persons";
	
	public PersonController() {
		setValidator(new PersonValidator());
	}
	
	@Override
	@ResponseBody
	@RequestMapping(value = PERSON_MODEL_URL + "*", method = RequestMethod.POST)
	public List<String> add(@RequestBody Person person, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return super.add(person, request, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = PERSON_MODEL_URL + "/{facility}*", method = RequestMethod.POST)
	public List<String> add(@RequestBody Person person, @PathVariable String facility, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return super.add(person, facility, request, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = PERSON_MODEL_URL + "/{gid}*", method = RequestMethod.PUT)
	public void edit(@RequestBody Person person, @PathVariable String gid, HttpServletResponse response) throws Exception {
		super.edit(person, gid, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = PERSON_MODEL_URL + "/{gid}*", method = RequestMethod.GET)
	public Object get(@PathVariable String gid, HttpServletResponse response) throws Exception {
		return super.get(gid, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = PERSON_MODEL_URL + "*", method = RequestMethod.GET)
	public List<Person> getAll(HttpServletResponse response) {
		return super.getAll(response);
	}

	@Override
	protected String getModelUrl() {
		return PERSON_MODEL_URL;
	}

	public PersonDAO getPersonDAO() {
		return getModelDAO();
	}
	public void setPersonDAO(PersonDAO personDAO) {
		setModelDAO(personDAO);
	}
}
