/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionPageController class.
 *     
 */
package ca.sciencestudio.service.session.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.facility.Laboratory;
import ca.sciencestudio.model.facility.dao.FacilityAuthzDAO;
import ca.sciencestudio.model.facility.dao.LaboratoryAuthzDAO;
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectAuthzDAO;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.SessionAuthzDAO;
import ca.sciencestudio.service.controllers.AbstractModelController;
import ca.sciencestudio.service.session.backers.SessionFormBacker;
import ca.sciencestudio.service.utilities.ModelPathUtils;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.authz.Authorities;

/**
 * @author maxweld
 *
 */
@Controller
public class SessionPageController extends AbstractModelController {
	
	private String facility;
	
	private ProjectAuthzDAO projectAuthzDAO;
	
	private SessionAuthzDAO sessionAuthzDAO;
	
	private FacilityAuthzDAO facilityAuthzDAO;
	
	private LaboratoryAuthzDAO laboratoryAuthzDAO;
	
	@RequestMapping(value = ModelPathUtils.SESSION_PATH + ".html")
	public String getSessionsPage(@RequestParam("project") String projectGid, ModelMap model) {
		
		String user = SecurityUtil.getPersonGid();
		
		Data<Authorities> projectAuthoritiesData = projectAuthzDAO.getAuthorities(user, projectGid);
		
		Data<Authorities> facilityAuthoritiesData = facilityAuthzDAO.getAuthorities(user, facility);
		
		Data<List<Laboratory>> laboratoryListData = laboratoryAuthzDAO.getAll(); //ByFacility??
			
		Project project = projectAuthzDAO.get(user, projectGid).get();
		if(project == null) {
			model.put("error", "Project not found.");
			return ERROR_VIEW;
		}
		
		Authorities authorities = mergeAuthorities(projectAuthoritiesData.get(), facilityAuthoritiesData.get());
		
		model.put("laboratoryList", laboratoryListData.get());
		model.put("authorities", authorities);
		model.put("project", project);
		return "frag/sessions";
	}

	@RequestMapping(value = ModelPathUtils.SESSION_PATH + "/{sessionGid}.html")
	public String getSessionPage(@PathVariable String sessionGid, ModelMap model) {

		String user = SecurityUtil.getPersonGid();

		Session session = sessionAuthzDAO.get(user, sessionGid).get();
		if(session == null) {
			model.put("error", "Session not found.");
			return ERROR_VIEW;
		}
		
		Data<Authorities> projectAuthoritiesData = projectAuthzDAO.getAuthorities(user, session.getProjectGid());
		
		Data<Authorities> sessionAuthoritiesData = sessionAuthzDAO.getAuthorities(user, session.getGid());
		
		Data<List<Laboratory>> laboratoryListData = laboratoryAuthzDAO.getAll(); //ByFacility??
		
		Project project = projectAuthzDAO.get(user, session.getProjectGid()).get();
		if(project == null) {
			model.put("error", "Project not found.");
			return ERROR_VIEW;
		}
		
		Authorities authorities = mergeAuthorities(projectAuthoritiesData.get(), sessionAuthoritiesData.get());
		
		model.put("session", new SessionFormBacker(session));
		model.put("laboratoryList", laboratoryListData.get());
		model.put("authorities", authorities);
		model.put("project", project);
		return "frag/session";
	}

	public String getFacility() {
		return facility;
	}
	public void setFacility(String facility) {
		this.facility = facility;
	}
	
	public FacilityAuthzDAO getFacilityAuthzDAO() {
		return facilityAuthzDAO;
	}
	public void setFacilityAuthzDAO(FacilityAuthzDAO facilityAuthzDAO) {
		this.facilityAuthzDAO = facilityAuthzDAO;
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

	public LaboratoryAuthzDAO getLaboratoryAuthzDAO() {
		return laboratoryAuthzDAO;
	}
	public void setLaboratoryAuthzDAO(LaboratoryAuthzDAO laboratoryAuthzDAO) {
		this.laboratoryAuthzDAO = laboratoryAuthzDAO;
	}
}
