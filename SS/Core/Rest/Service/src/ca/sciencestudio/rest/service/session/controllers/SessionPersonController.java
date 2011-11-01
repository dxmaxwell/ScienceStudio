/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionPersonController class.
 *     
 */
package ca.sciencestudio.rest.service.session.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.session.SessionPerson;
import ca.sciencestudio.model.session.dao.SessionPersonBasicDAO;
import ca.sciencestudio.model.session.validators.SessionPersonValidator;
import ca.sciencestudio.rest.service.controllers.AbstractModelController;

/**
 * @author maxweld
 * 
 *
 */
@Controller
public class SessionPersonController extends AbstractModelController<SessionPerson, SessionPersonBasicDAO, SessionPersonValidator> {

	private static final String SESSION_PERSON_MODEL_URL = "/session/persons";

	@Override
	@ResponseBody
	@RequestMapping(value = SESSION_PERSON_MODEL_URL + "*", method = RequestMethod.POST)
	public List<String> add(@RequestBody SessionPerson sp, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return super.add(sp, request, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = SESSION_PERSON_MODEL_URL + "/{facility}*", method = RequestMethod.POST)
	public List<String> add(@RequestBody SessionPerson sp, @PathVariable String facility, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return super.add(sp, facility, request, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = SESSION_PERSON_MODEL_URL + "/{gid}*", method = RequestMethod.PUT)
	public void edit(@RequestBody SessionPerson sp, @PathVariable String gid, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		super.edit(sp, gid, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = SESSION_PERSON_MODEL_URL + "/{gid}*", method = RequestMethod.DELETE)
	public void remove(@PathVariable String gid, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		super.remove(gid, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = SESSION_PERSON_MODEL_URL + "/{gid}*", method = RequestMethod.GET)
	public Object get(@PathVariable String gid, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return super.get(gid, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = SESSION_PERSON_MODEL_URL + "*", method = RequestMethod.GET)
	public List<SessionPerson> getAll(HttpServletResponse response) {
		// TODO Auto-generated method stub
		return super.getAll(response);
	}

	@Override
	protected String getModelUrl() {
		return SESSION_PERSON_MODEL_URL;
	}

	public SessionPersonBasicDAO getSessionPersonBasicDAO() {
		return getModelBasicDAO();
	}
	public void setSessionPersonBasicDAO(SessionPersonBasicDAO sessionPersonBasicDAO) {
		setModelBasicDAO(sessionPersonBasicDAO);
	}
}
