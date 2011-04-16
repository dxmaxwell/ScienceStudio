/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     BeamlineViewportController class.
 *     
 */
package ca.sciencestudio.service.app.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectDAO;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.SessionDAO;
import ca.sciencestudio.util.state.StateMap;

/**
 * @author medrand
 *
 */
public class BeamlineViewportController implements Controller {

	public static final String SESSION_ID_PARAMETER_KEY = "sessionId";
	public static final String SUCCESS_VIEW = "beamlineViewport";
	public static final String FAILURE_VIEW = "errorPage";
	
	private ProjectDAO projectDAO;
	private SessionDAO sessionDAO;
	private StateMap stateMap;
	
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String,Object> model = new HashMap<String,Object>();
		
		String sessionIdParamter = request.getParameter(SESSION_ID_PARAMETER_KEY);
		
		int sessionId = 0;
		try {
			sessionId = Integer.parseInt(sessionIdParamter);
		}
		catch (NumberFormatException e) {}
		
		Session session = sessionDAO.getSessionById(sessionId);
		if(session == null) {
			model.put("error", "Invalid Session Id");
			model.put("errorMessage", "Session could not be found.");
			return new ModelAndView(FAILURE_VIEW, model);
		}
		
		Project project = projectDAO.getProjectById(session.getProjectId());
		if(project == null) {
			model.put("error", "Invalid Project Id");
			model.put("errorMessage", "Project could not be found.");
			return new ModelAndView(FAILURE_VIEW, model);
		}
		
		stateMap.put("projectId", project.getId());
		stateMap.put("projectName", project.getName());
		stateMap.put("sessionId", session.getId());
		stateMap.put("sessionName", session.getName());
		stateMap.put("experimentId", 0);
		stateMap.put("experimentName", "(PLEASE SELECT)");
		stateMap.put("scanId", 0);
		stateMap.put("scanName", "(NOT AVAILABLE)");
		
		return new ModelAndView(SUCCESS_VIEW);
	}
	
	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}
	
	public void setSessionDAO(SessionDAO sessionDAO) {
		this.sessionDAO = sessionDAO;
	}
	
	public void setStateMap(StateMap stateMap) {
		this.stateMap = stateMap;
	}
}
