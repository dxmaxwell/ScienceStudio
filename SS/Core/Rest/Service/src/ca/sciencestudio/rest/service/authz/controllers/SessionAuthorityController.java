/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionAuthorityController class.
 *     
 */
package ca.sciencestudio.rest.service.authz.controllers;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.SessionBasicDAO;
import ca.sciencestudio.security.authz.accessors.SessionAuthorityAccessor;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 * 
 *
 */
@Controller
public class SessionAuthorityController extends AbstractAuthorityController {

	private static final String SESSION_AUTHZ_PATH = "/sessions";
	
	private SessionBasicDAO sessionBasicDAO;
	
	private SessionAuthorityAccessor sessionAuthorityAccessor;
	
	@ResponseBody
	@RequestMapping(value = SESSION_AUTHZ_PATH + "/{gid}*", method = RequestMethod.GET)
	public Object getAuthorities(@RequestParam String user, @PathVariable String gid, HttpServletResponse response) {
		
		try {
			Session session = sessionBasicDAO.get(gid);
			if(session == null) {
				response.setStatus(HttpStatus.NOT_FOUND.value());
				return EMPTY_AUTHORITIES;
			}
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return EMPTY_AUTHORITIES;
		}
		
		try {
			return sessionAuthorityAccessor.getAuthorities(user, gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return EMPTY_AUTHORITIES;
		}
	}
	
	public SessionBasicDAO getSessionBasicDAO() {
		return sessionBasicDAO;
	}
	public void setSessionBasicDAO(SessionBasicDAO sessionBasicDAO) {
		this.sessionBasicDAO = sessionBasicDAO;
	}

	public SessionAuthorityAccessor getSessionAuthorityAccessor() {
		return sessionAuthorityAccessor;
	}
	public void setSessionAuthorityAccessor(SessionAuthorityAccessor sessionAuthorityAccessor) {
		this.sessionAuthorityAccessor = sessionAuthorityAccessor;
	}
}
