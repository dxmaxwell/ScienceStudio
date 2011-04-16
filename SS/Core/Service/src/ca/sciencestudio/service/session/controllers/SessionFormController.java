/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionFormController class.
 *     
 */
package ca.sciencestudio.service.session.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectDAO;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.SessionDAO;
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;

import ca.sciencestudio.service.controllers.AbstractModelController;
import ca.sciencestudio.service.session.backers.SessionFormBacker;
import ca.sciencestudio.service.session.validators.SessionFormBackerValidator;
import ca.sciencestudio.service.utilities.ModelPathUtils;
import ca.sciencestudio.util.web.BindAndValidateUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class SessionFormController extends AbstractModelController {

	@Autowired
	private ProjectDAO projectDAO;
	
	@Autowired
	private SessionDAO sessionDAO;
	
	@Autowired
	private SessionFormBackerValidator sessionFormBackerValidator;
	
	@RequestMapping(value = "/project/{projectId}/sessions/form/add.{format}", method = RequestMethod.POST)
	public String postSessionsFormAdd(@PathVariable int projectId, @PathVariable String format, HttpServletRequest request, ModelMap model) {
		
		SessionFormBacker sessionFormBacker = new SessionFormBacker(projectId);
		BindException errors  = BindAndValidateUtils.buildBindException(sessionFormBacker);
		model.put("errors", errors);
		
		String responseView = "response-" + format;
		
		Project project = projectDAO.getProjectById(projectId);
		if(project == null) {
			errors.reject("project.notfound", "Project not found.");
			return responseView;
		}
		
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		
		if(!SecurityUtil.hasAuthority(admin)) {
			errors.reject("permission.denied", "Permission denied.");
			return responseView;
		}
		
		errors = BindAndValidateUtils.bindAndValidate(sessionFormBacker, request, sessionFormBackerValidator);
		if(errors.hasErrors()) {
			model.put("errors", errors);
			return responseView;
		}
		
		Session session = sessionFormBacker.createSession(sessionDAO);
		sessionDAO.addSession(session);
		
		Map<String,String> response = new HashMap<String,String>();
		response.put("viewUrl", getModelPath(request) + ModelPathUtils.getSessionPath(session.getId(), ".html"));
		
		model.put("response", response);
		return responseView;
	}

	@RequestMapping(value = "/session/{sessionId}/form/edit.{format}", method = RequestMethod.POST)
	public String postSessionFormEdit(@PathVariable int sessionId, @PathVariable String format,
														HttpServletRequest request, ModelMap model) {
		
		BindException errors  = BindAndValidateUtils.buildBindException();
		model.put("errors", errors);
		
		String responseView = "response-" + format;
		
		Session session = sessionDAO.getSessionById(sessionId);
		if(session == null) {
			errors.reject("session.notfound", "Session not found");
			return responseView;
		}
		
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		if(!SecurityUtil.hasAuthority(admin)) {
			errors.reject("permission.denied", "Permission denied.");
			return responseView;
		}
	
		SessionFormBacker sessionFormBacker = new SessionFormBacker(session);

		errors = BindAndValidateUtils.bindAndValidate(sessionFormBacker, request, sessionFormBackerValidator);
		if(errors.hasErrors()) {
			model.put("errors", errors);
			return responseView;
		}
		
		sessionDAO.editSession(sessionFormBacker.createSession(sessionDAO));
		
		Map<String,String> response = new HashMap<String,String>();
		response.put("message", "Session saved.");
		
		model.put("response", response);
		return responseView;
	}
	
	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}
	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public SessionDAO getSessionDAO() {
		return sessionDAO;
	}
	public void setSessionDAO(SessionDAO sessionDAO) {
		this.sessionDAO = sessionDAO;
	}

	public SessionFormBackerValidator getSessionFormBackerValidator() {
		return sessionFormBackerValidator;
	}
	public void setSessionFormBackerValidator(SessionFormBackerValidator sessionFormBackerValidator) {
		this.sessionFormBackerValidator = sessionFormBackerValidator;
	}
}
