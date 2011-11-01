/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionController class.
 *     
 */
package ca.sciencestudio.service.session.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.SessionAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.controllers.AbstractModelController;
import ca.sciencestudio.service.utilities.ModelPathUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class SessionController extends AbstractModelController {

	private SessionAuthzDAO sessionAuthzDAO;
	
	@ResponseBody
	@RequestMapping(value = ModelPathUtils.SESSION_PATH + "*")
	public List<Session> getSessionList(@RequestParam("project") String projectGid) {
		String user = SecurityUtil.getPersonGid();
		return sessionAuthzDAO.getAllByProjectGid(user, projectGid).get();
	}
	
	public SessionAuthzDAO getSessionAuthzDAO() {
		return sessionAuthzDAO;
	}
	public void setSessionAuthzDAO(SessionAuthzDAO sessionAuthzDAO) {
		this.sessionAuthzDAO = sessionAuthzDAO;
	}
}
