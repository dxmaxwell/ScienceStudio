/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ScanController class.
 *     
 */
package ca.sciencestudio.service.session.controllers;

import java.util.List;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectDAO;
import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.dao.ScanDAO;
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;

/**
 * @author maxweld
 *
 */
@Controller
public class ScanController {

	@Autowired
	private ScanDAO scanDAO;
	
	@Autowired
	private ProjectDAO projectDAO;
	
	@RequestMapping(value = "/experiment/{experimentId}/scans.{format}", method = RequestMethod.GET)
	public String getScanList(@PathVariable int experimentId, @PathVariable String format, ModelMap model) {
		
		List<Scan> scanList = Collections.emptyList();
		model.put("response", scanList);
		
		String responseView = "response-" + format;
		
		Project project = projectDAO.getProjectByExperimentId(experimentId);
		if(project == null) {
			return responseView;
		}
		
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		Object group = AuthorityUtil.buildProjectGroupAuthority(project.getId());
		
		if(!SecurityUtil.hasAnyAuthority(admin, group)) {
			return responseView;
		}
		
		scanList = scanDAO.getScanListByExperimentId(experimentId);
		model.put("response", scanList);
		return responseView;
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
}
