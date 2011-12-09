/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionGridController class.
 *     
 */
package ca.sciencestudio.service.session.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectAuthzDAO;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.SessionAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.controllers.AbstractModelController;
import ca.sciencestudio.service.session.backers.SessionGridBacker;
import ca.sciencestudio.service.utilities.ModelPathUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class SessionGridController extends AbstractModelController {

	private ProjectAuthzDAO projectAuthzDAO;
	
	private SessionAuthzDAO sessionAuthzDAO;

	@ResponseBody
	@RequestMapping(value = ModelPathUtils.SESSION_PATH + "/grid*")
	public List<SessionGridBacker> sessions() {
		
		String user = SecurityUtil.getPersonGid();
		
		List<Session> sessionList = sessionAuthzDAO.getAll(user).get();

		Map<String,Data<Project>> dataProjectMap = new HashMap<String,Data<Project>>();
		
		for(Session session : sessionList) {
			if(!dataProjectMap.containsKey(session.getProjectGid())) {
				dataProjectMap.put(session.getProjectGid(), projectAuthzDAO.get(user, session.getProjectGid()));
			}
		}
		
		List<SessionGridBacker> sessionGridBackerList = new ArrayList<SessionGridBacker>();
		
		for(Session session : sessionList) {
			Data<Project> dataProject = dataProjectMap.get(session.getProjectGid());
			if(dataProject != null) {
				Project project = dataProject.get();
				if(project != null) {
					sessionGridBackerList.add(new SessionGridBacker(project, session));
				}
			}
		}
		
		return sessionGridBackerList;
	}

	public ProjectAuthzDAO getProjectAuthzDAO() {
		return projectAuthzDAO;
	}
	public void setProjectAuthzDAO(ProjectAuthzDAO projectAuthzDAO) {
		this.projectAuthzDAO = projectAuthzDAO;
	}

	public SessionAuthzDAO getSessionAuthzDAO() {
		return sessionAuthzDAO;
	}
	public void setSessionAuthzDAO(SessionAuthzDAO sessionAuthzDAO) {
		this.sessionAuthzDAO = sessionAuthzDAO;
	}
}
