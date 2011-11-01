/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionController class.
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

import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.SessionBasicDAO;
import ca.sciencestudio.model.session.validators.SessionValidator;
import ca.sciencestudio.rest.service.controllers.AbstractModelController;

/**
 * @author maxweld
 * 
 *
 */
@Controller
public class SessionController extends AbstractModelController<Session, SessionBasicDAO, SessionValidator> {

	private static final String SESSION_MODEL_URL = "/sessions";
	
	@Override
	@ResponseBody
	@RequestMapping(value = SESSION_MODEL_URL + "*", method = RequestMethod.POST)
	public List<String> add(@RequestBody Session s, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return super.add(s, request, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = SESSION_MODEL_URL + "/{facility}*", method = RequestMethod.POST)
	public List<String> add(@RequestBody Session s, @PathVariable String facility, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return super.add(s, facility, request, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = SESSION_MODEL_URL + "/{gid}*", method = RequestMethod.PUT)
	public void edit(@RequestBody Session s, @PathVariable String gid, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		super.edit(s, gid, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = SESSION_MODEL_URL + "/{gid}*", method = RequestMethod.DELETE)
	public void remove(@PathVariable String gid, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		super.remove(gid, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = SESSION_MODEL_URL + "/{gid}*", method = RequestMethod.GET)
	public Object get(@PathVariable String gid, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return super.get(gid, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = SESSION_MODEL_URL + "*", method = RequestMethod.GET)
	public List<Session> getAll(HttpServletResponse response) {
		// TODO Auto-generated method stub
		return super.getAll(response);
	}

	@Override
	protected String getModelUrl() {
		return SESSION_MODEL_URL;
	}
	
	public SessionBasicDAO getSessionBasicDAO() {
		return getModelBasicDAO();
	}
	public void setSessionBasicDAO(SessionBasicDAO sessionBasicDAO) {
		setModelBasicDAO(sessionBasicDAO);
	}
}
