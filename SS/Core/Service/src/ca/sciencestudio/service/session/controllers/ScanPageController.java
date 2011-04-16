/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ScanPageController class.
 *     
 */
package ca.sciencestudio.service.session.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectDAO;
import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.ExperimentDAO;
import ca.sciencestudio.model.session.dao.ScanDAO;
import ca.sciencestudio.model.session.dao.SessionDAO;
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.controllers.AbstractModelController;
import ca.sciencestudio.service.session.backers.ScanFormBacker;

/**
 * @author maxweld
 *
 */
@Controller
public class ScanPageController extends AbstractModelController {

	private static final String ERROR_VIEW = "frag/error";
	
	@Autowired
	private ScanDAO scanDAO;
	
	@Autowired
	private ProjectDAO projectDAO;
	
	@Autowired
	private SessionDAO sessionDAO;
	
	@Autowired
	private ExperimentDAO experimentDAO;
	
	@RequestMapping(value = "/experiment/{experimentId}/scans.html", method = RequestMethod.GET)
	public String getScansPage(@PathVariable int experimentId, ModelMap model) {
		
		Experiment experiment = experimentDAO.getExperimentById(experimentId);
		if(experiment == null) {
			model.put("error", "Experiment not found.");
			return ERROR_VIEW;
		}
		
		Session session = sessionDAO.getSessionById(experiment.getSessionId());
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
			return ERROR_VIEW;
		}
		
		model.put("experiment", experiment);
		model.put("project", project);
		model.put("session", session);
		return "frag/scans";
	}
	
	@RequestMapping(value = "/scan/{scanId}.html", method = RequestMethod.GET)
	public String getScanPage(@PathVariable int scanId, ModelMap model) {
		
		Scan scan = scanDAO.getScanById(scanId);
		if(scan == null) {
			model.put("error", "Scan not found!");
			return ERROR_VIEW;
		}
		
		Experiment experiment = experimentDAO.getExperimentById(scan.getExperimentId());
		if(experiment == null) {
			model.put("error", "Experiment not found.");
			return ERROR_VIEW;
		}
		
		Session session = sessionDAO.getSessionById(experiment.getSessionId());
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
			return ERROR_VIEW;
		}
		
		ScanFormBacker scanFormBacker = new ScanFormBacker(scan);
		model.put("scanFormBacker", scanFormBacker);
		model.put("experiment", experiment);
		model.put("project", project);
		model.put("session", session);
		model.put("scan", scan);
		return "frag/scan";
	}

	public ScanDAO getScanDAO() {
		return scanDAO;
	}
	public void setScanDAO(ScanDAO scanDAO) {
		this.scanDAO = scanDAO;
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

	public ExperimentDAO getExperimentDAO() {
		return experimentDAO;
	}
	public void setExperimentDAO(ExperimentDAO experimentDAO) {
		this.experimentDAO = experimentDAO;
	}
}
