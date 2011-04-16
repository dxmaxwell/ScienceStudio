/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionPageController class.
 *     
 */
package ca.sciencestudio.service.session.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.sciencestudio.model.facility.dao.LaboratoryDAO;
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectDAO;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.SessionDAO;
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.session.backers.SessionFormBacker;

/**
 * @author maxweld
 *
 */
@Controller
public class SessionPageController {
	
	private static final String ERROR_VIEW = "frag/error";
	
	@Autowired
	private ProjectDAO projectDAO;
	
	@Autowired
	private SessionDAO sessionDAO;
	
	@Autowired
	private LaboratoryDAO laboratoryDAO;
	
	@RequestMapping(value = "/project/{projectId}/sessions.html", method = RequestMethod.GET)
	public String getSessionsPage(@PathVariable int projectId, ModelMap model) {
		
		Project project = projectDAO.getProjectById(projectId);
		if(project == null) {
			model.put("error", "Project not found.");
			return ERROR_VIEW;
		}
		
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		Object group = AuthorityUtil.buildProjectGroupAuthority(projectId);
		
		if(!SecurityUtil.hasAnyAuthority(admin, group)) {
			model.put("error", "Permission denied.");
			return ERROR_VIEW;
		}
		
		model.put("laboratoryList", laboratoryDAO.getLaboratoryList());
		model.put("project", project);
		return "frag/sessions";
	}

	@RequestMapping(value = "/session/{sessionId}.html", method = RequestMethod.GET)
	public String getSessionPage(@PathVariable int sessionId, ModelMap model) {
		
		Session session = sessionDAO.getSessionById(sessionId);
		if(session == null) {
			model.put("error", "Session not found.");
			return ERROR_VIEW;
		}
		
		Project project = projectDAO.getProjectById(session.getProjectId());
		if(project == null) {
			model.put("error", "Project not found.");
			return ERROR_VIEW;
		}
		
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		Object group = AuthorityUtil.buildProjectGroupAuthority(project.getId());
		
		if(!SecurityUtil.hasAnyAuthority(admin, group)) {
			model.put("error", "Permission denied.");
			return "frag/error";
		}
				
		model.put("sessionFormBacker", new SessionFormBacker(session));
		model.put("laboratoryList", laboratoryDAO.getLaboratoryList());
		model.put("session", session);
		model.put("project", project);
		return "frag/session";
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

	public LaboratoryDAO getLaboratoryDAO() {
		return laboratoryDAO;
	}
	public void setLaboratoryDAO(LaboratoryDAO laboratoryDAO) {
		this.laboratoryDAO = laboratoryDAO;
	}
}
