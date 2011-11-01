/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ScanPageController class.
 *     
 */
package ca.sciencestudio.service.session.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sciencestudio.model.Permissions;
import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectAuthzDAO;
import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.ExperimentAuthzDAO;
import ca.sciencestudio.model.session.dao.ScanAuthzDAO;
import ca.sciencestudio.model.session.dao.SessionAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.controllers.AbstractModelController;
import ca.sciencestudio.service.session.backers.ScanFormBacker;
import ca.sciencestudio.service.utilities.ModelPathUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class ScanPageController extends AbstractModelController {
	
	private ScanAuthzDAO scanAuthzDAO;
	
	private ProjectAuthzDAO projectAuthzDAO;
	
	private SessionAuthzDAO sessionAuthzDAO;
	
	private ExperimentAuthzDAO experimentAuthzDAO;
	
	@RequestMapping(value = ModelPathUtils.SCAN_PATH + ".html", params = "experiment")
	public String getScansPage(@RequestParam("experiment") String experimentGid, ModelMap model) {
		
		String user = SecurityUtil.getPersonGid();
		
		Data<Permissions> dataPermissions = scanAuthzDAO.permissions(user);
		
		Experiment experiment = experimentAuthzDAO.get(user, experimentGid).get();
		if(experiment == null) {
			model.put("error", "Experiment not found.");
			return ERROR_VIEW;
		}
		
		Session session = sessionAuthzDAO.get(user, experiment.getSessionGid()).get();
		if(session == null) {
			model.put("error", "Session not found.");
			return ERROR_VIEW;
		}
		
		Project project = projectAuthzDAO.get(user, session.getProjectGid()).get();
		if(project == null) {
			model.put("error", "Project not found.");
			return ERROR_VIEW;
		}
		
		Permissions permissions = dataPermissions.get();
		if(permissions == null) {
			model.put("error", "Permissions not found.");
			return ERROR_VIEW;
		}
		
		model.put("permissions", permissions);
		model.put("experiment", experiment);
		model.put("project", project);
		model.put("session", session);
		return "frag/scans";
	}
	
	
	@RequestMapping(value = ModelPathUtils.SCAN_PATH + "/{scanGid}.html")
	public String getScanPage(@PathVariable String scanGid, ModelMap model) {
		
		String user = SecurityUtil.getPersonGid();
		
		Data<Permissions> dataPermissions = scanAuthzDAO.permissions(user, scanGid);
		
		Scan scan = scanAuthzDAO.get(user, scanGid).get();
		if(scan == null) {
			model.put("error", "Scan not found!");
			return ERROR_VIEW;
		}
		
		Experiment experiment = experimentAuthzDAO.get(user, scan.getExperimentGid()).get();
		if(experiment == null) {
			model.put("error", "Experiment not found.");
			return ERROR_VIEW;
		}
		
		Session session = sessionAuthzDAO.get(user, experiment.getSessionGid()).get();
		if(session == null) {
			model.put("error", "Session not found.");
			return ERROR_VIEW;
		}
		
		Project project = projectAuthzDAO.get(user, session.getProjectGid()).get();
		if(project == null) {
			model.put("error", "Project not found.");
			return ERROR_VIEW;
		}
		
		Permissions permissions = dataPermissions.get();
		if(permissions == null) {
			model.put("error", "Permissions not found.");
			return ERROR_VIEW;
		}
		
		model.put("scan", new ScanFormBacker(scan));
		model.put("permissions", permissions);
		model.put("experiment", experiment);
		model.put("project", project);
		model.put("session", session);
		return "frag/scan";
	}

	public ScanAuthzDAO getScanAuthzDAO() {
		return scanAuthzDAO;
	}
	public void setScanAuthzDAO(ScanAuthzDAO scanAuthzDAO) {
		this.scanAuthzDAO = scanAuthzDAO;
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

	public ExperimentAuthzDAO getExperimentAuthzDAO() {
		return experimentAuthzDAO;
	}
	public void setExperimentAuthzDAO(ExperimentAuthzDAO experimentAuthzDAO) {
		this.experimentAuthzDAO = experimentAuthzDAO;
	}
}
