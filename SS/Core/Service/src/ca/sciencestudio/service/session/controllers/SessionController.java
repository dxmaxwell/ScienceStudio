/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionController class.
 *     
 */
package ca.sciencestudio.service.session.controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.ExperimentDAO;
import ca.sciencestudio.model.session.dao.SessionDAO;
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.controllers.AbstractModelController;
import ca.sciencestudio.service.utilities.ModelPathUtils;
import ca.sciencestudio.util.web.BindAndValidateUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class SessionController extends AbstractModelController {

	@Autowired
	private SessionDAO sessionDAO;
	
	@Autowired
	private ExperimentDAO experimentDAO;
	
	@RequestMapping(value = "/project/{projectId}/sessions.{format}", method = RequestMethod.GET)
	public String getSessionList(@PathVariable int projectId, @PathVariable String format, ModelMap model) {
		
		List<Session> sessionList = Collections.emptyList();
		
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		Object team = AuthorityUtil.buildProjectGroupAuthority(projectId);
		if(SecurityUtil.hasAnyAuthority(admin, team)) {
			sessionList = sessionDAO.getSessionListByProjectId(projectId);
		}
		
		model.put("response", sessionList);
		return "response-" + format;
	}

	@RequestMapping(value = "/session/{sessionId}/remove.{format}")
	public String removeSession(@PathVariable int sessionId, @PathVariable String format, HttpServletRequest request, ModelMap model) {
		
		BindException errors = BindAndValidateUtils.buildBindException();
		model.put("errors", errors);
		
		String responseView = "response-" + format;
		
		Session session = sessionDAO.getSessionById(sessionId);
		if(session == null) {
			errors.reject("session.notfound", "Session not found.");
			return responseView;
		}
		
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		
		if(!SecurityUtil.hasAuthority(admin)) {
			errors.reject("permission.denied", "Not permitted to remove session.");
			return responseView;
		}
		
		List<Experiment> experimentList = experimentDAO.getExperimentListBySessionId(sessionId);
		if(!experimentList.isEmpty()) {
			errors.reject("experiments.notempty", "Session has associated experiments.");
			return responseView;
		}
		
		sessionDAO.removeSession(sessionId);
		
		Map<String,String> response = new HashMap<String,String>();
		response.put("viewUrl", getModelPath(request) + ModelPathUtils.getSessionsPath(session.getProjectId(), ".html"));
		model.put("response", response);
		return responseView;
	}
	
	public SessionDAO getSessionDAO() {
		return sessionDAO;
	}
	public void setSessionDAO(SessionDAO sessionDAO) {
		this.sessionDAO = sessionDAO;
	}

	public ExperimentDAO getExperimentDAO() {
		return experimentDAO;
	}
	public void setExperimentDAO(ExperimentDAO experimentDAO) {
		this.experimentDAO = experimentDAO;
	}
}
