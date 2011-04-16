/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     MainPageController class.
 *     
 */
package ca.sciencestudio.vespers.service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.model.session.dao.SessionDAO;
import ca.sciencestudio.model.project.dao.ProjectDAO;
import ca.sciencestudio.vespers.device.proxy.event.BeamlineSessionProxyEventListener;

/**
 * @author maxweld
 *
 */
@Controller
public class MainPageController {
	
	private static final String MODEL_KEY_PERSON_UID = "personUid";
	
	private static final String VALUE_KEY_PROJECT_ID = "projectId";
	private static final String VALUE_KEY_SESSION_ID = "sessionId";
	
	private static final String SUCCESS_VIEW = "page/main";
	private static final String FAILURE_VIEW = "page/error";
	
	@Autowired
	private ProjectDAO projectDAO;
	
	@Autowired
	private SessionDAO sessionDAO;
	
	private String successView = SUCCESS_VIEW;
	
	private BeamlineSessionProxyEventListener beamlineSessionStateMap;
	
	@RequestMapping(value = "/main.html", method = RequestMethod.GET)
	public String handleRequest(@RequestParam int sessionId, ModelMap model) {
				
		Integer currentSessionId = (Integer) beamlineSessionStateMap.get(VALUE_KEY_SESSION_ID);
		
		if((currentSessionId == null) || (sessionId != currentSessionId)) {
			model.put("error", "Session has not been started.");
			model.put("errorMessage", "<a href=\"\">Try Again</a>");
			return FAILURE_VIEW;
		}
		
		Object value = beamlineSessionStateMap.get(VALUE_KEY_PROJECT_ID);
		
		int projectId = 0;
		if(value instanceof Number) {
			projectId = ((Number)value).intValue();
		}

		Object admin = AuthorityUtil.buildRoleAuthority("ADMIN_VESPERS");
		Object group = AuthorityUtil.buildProjectGroupAuthority(projectId);
		Object exptr = AuthorityUtil.buildProjectExperimenterAuthority(projectId);
		
		if(!SecurityUtil.hasAnyAuthority(group,admin)) {
			model.put("error", "Not permitted to view session.");
			model.put("errorMessage", "<a href=\"\">Try Again</a>");
			return FAILURE_VIEW;
		}
			
		String controllerUid = beamlineSessionStateMap.getControllerUid();
		if((controllerUid.length() == 0) && SecurityUtil.hasAnyAuthority(exptr,admin)) {
			beamlineSessionStateMap.setController(SecurityUtil.getPerson());
		}
		
		model.put(MODEL_KEY_PERSON_UID, SecurityUtil.getPerson().getUid());
		return successView;
	}
	
	public String getSuccessView() {
		return successView;
	}
	public void setSuccessView(String successView) {
		this.successView = successView;
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

	public BeamlineSessionProxyEventListener getBeamlineSessionStateMap() {
		return beamlineSessionStateMap;
	}
	public void setBeamlineSessionStateMap(BeamlineSessionProxyEventListener beamlineSessionStateMap) {
		this.beamlineSessionStateMap = beamlineSessionStateMap;
	}
}
