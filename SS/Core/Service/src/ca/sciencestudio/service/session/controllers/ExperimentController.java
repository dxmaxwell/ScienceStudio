/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ExperimentController class.
 *     
 */
package ca.sciencestudio.service.session.controllers;

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

import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectDAO;
import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.dao.ExperimentDAO;
import ca.sciencestudio.model.session.dao.ScanDAO;
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
public class ExperimentController extends AbstractModelController {

	@Autowired
	private ScanDAO scanDAO;
	
	@Autowired
	private ProjectDAO projectDAO;
	
	@Autowired
	private ExperimentDAO experimentDAO;
	
	@RequestMapping(value = "/experiment/{experimentId}/remove.{format}")
	public String removeExperiment(@PathVariable int experimentId, @PathVariable String format, HttpServletRequest request, ModelMap model) {
		
		BindException errors = BindAndValidateUtils.buildBindException();
		model.put("errors", errors);
		
		String responseView = "response-" + format;

		Experiment experiment = experimentDAO.getExperimentById(experimentId);
		if(experiment == null) {
			errors.reject("experiment.notfound", "Experiment not found.");
			return responseView;
		}
		
		Project project = projectDAO.getProjectByExperimentId(experimentId);
		if(project == null) {
			errors.reject("project.notfound", "Project not found.");
			return responseView;
		}
		
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		Object exptr = AuthorityUtil.buildProjectExperimenterAuthority(project.getId());
		
		if(!SecurityUtil.hasAnyAuthority(exptr, admin)) {
			errors.reject("permission.denied", "Not permitted to remove experiment.");
			return responseView;
		}
		
		List<Scan> scanList = scanDAO.getScanListByExperimentId(experimentId);
		if(!scanList.isEmpty()) {
			errors.reject("scans.notempty", "Experiment has associated scans.");
			return responseView;
		}
		
		experimentDAO.removeExperiment(experimentId);
		
		Map<String,String> response = new HashMap<String,String>();
		response.put("viewUrl", getModelPath(request) + ModelPathUtils.getExperimentsPath(experiment.getSessionId(), ".html"));
		model.put("response", response);
		return responseView;
	}

	public ScanDAO getScanDAO() {
		return scanDAO;
	}
	public void setScanDAO(ScanDAO scanDAO) {
		this.scanDAO = scanDAO;
	}

	public ExperimentDAO getExperimentDAO() {
		return experimentDAO;
	}
	public void setExperimentDAO(ExperimentDAO experimentDAO) {
		this.experimentDAO = experimentDAO;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}
	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}
}
