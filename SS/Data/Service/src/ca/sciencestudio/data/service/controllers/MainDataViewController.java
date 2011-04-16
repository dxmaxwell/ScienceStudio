/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     MainDataViewController class.
 *     
 */
package ca.sciencestudio.data.service.controllers;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectDAO;
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;

/**
 * @author maxweld
 *
 */
@Controller
public class MainDataViewController {

	private static final String ERROR_VIEW = "page/error";
	private static final String GENERIC_VIEW = "page/generic";
	
	@Autowired
	private ProjectDAO projectDAO;
	
	private Map<String,String> dataViews = Collections.emptyMap();
	
	@RequestMapping(value = "/main.html", method = RequestMethod.GET)
	public String getGenericPage(@RequestParam int scanId, ModelMap model) {
		
		Project project = projectDAO.getProjectByScanId(scanId);
		if(project == null) {
			model.put("error", "Project not found.");
			return ERROR_VIEW;
		}
		
		Object admin = AuthorityUtil.ROLE_ADMIN_DATA;
		Object group = AuthorityUtil.buildProjectGroupAuthority(project.getId());
		
		if(!SecurityUtil.hasAnyAuthority(group, admin)) {
			model.put("error", "Not permitted to view data.");
			return ERROR_VIEW;
		}
		
		model.put("scanId", scanId);
		return GENERIC_VIEW;
	}
	
	@RequestMapping(value = "/{instrumentName}/{techniqueName}/main.html", method = RequestMethod.GET)
	public String getMainPage(@PathVariable String instrumentName, @PathVariable String techniqueName, @RequestParam int scanId, ModelMap model) {
		
		Project project = projectDAO.getProjectByScanId(scanId);
		if(project == null) {
			model.put("error", "Project not found.");
			return ERROR_VIEW;
		}
		
		Object admin = AuthorityUtil.ROLE_ADMIN_DATA;
		Object group = AuthorityUtil.buildProjectGroupAuthority(project.getId());
		
		if(!SecurityUtil.hasAnyAuthority(group, admin)) {
			model.put("error", "Not permitted to view data.");
			return ERROR_VIEW;
		}
		
		String dataViewKey = instrumentName + "/" + techniqueName;
		
		String dataView = dataViews.get(dataViewKey);
		if(dataView == null) {
			model.put("error", "No data view found for request: " + dataViewKey);
			return ERROR_VIEW;
		}
		
		model.put("scanId", scanId);
		return dataView;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}
	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public Map<String, String> getDataViews() {
		return dataViews;
	}
	public void setDataViews(Map<String, String> dataViews) {
		this.dataViews = dataViews;
	}
}
