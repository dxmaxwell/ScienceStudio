/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionGridController class.
 *     
 */
package ca.sciencestudio.service.session.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.ProjectStatus;
import ca.sciencestudio.model.project.dao.ProjectDAO;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.SessionDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.controllers.AbstractModelController;
import ca.sciencestudio.service.session.backers.SessionGridBacker;

/**
 * @author maxweld
 *
 */
@Controller
public class SessionGridController extends AbstractModelController {

	@Autowired
	private ProjectDAO projectDAO;
	
	@Autowired
	private SessionDAO sessionDAO;

	@RequestMapping(value = "/sessions/grid.{format}")
	public String sessions(@PathVariable String format, ModelMap model) {
		
		String personUid = SecurityUtil.getPerson().getGid();
		List<Project> projectList = projectDAO.getProjectListByPersonUidAndStatus(personUid, ProjectStatus.ACTIVE);
		List<Session> sessionList = sessionDAO.getSessionListByPersonUidAndProjectStatus(personUid, ProjectStatus.ACTIVE);
		
		List<SessionGridBacker> sessionGridBackerList = new ArrayList<SessionGridBacker>(sessionList.size());
		for(Session session : sessionList) {
			for(Project project : projectList) {
				if(project.getId() == session.getProjectId()) {
					sessionGridBackerList.add(new SessionGridBacker(project, session));
					break;
				}
			}
		}
				
		model.put("response", sessionGridBackerList);
		return "response-" + format;
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
}
