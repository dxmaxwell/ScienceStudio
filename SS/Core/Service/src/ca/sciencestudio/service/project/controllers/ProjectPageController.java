/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectPageController class.
 *     
 */
package ca.sciencestudio.service.project.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.facility.Facility;
import ca.sciencestudio.model.facility.dao.FacilityAuthzDAO;
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.controllers.AbstractModelController;
import ca.sciencestudio.service.project.backers.ProjectFormBacker;
import ca.sciencestudio.service.utilities.ModelPathUtils;
import ca.sciencestudio.util.authz.Authorities;
import ca.sciencestudio.util.web.EnumToOptionUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class ProjectPageController extends AbstractModelController {
	
	private String facility; 
	
	private ProjectAuthzDAO projectAuthzDAO;

	private FacilityAuthzDAO facilityAuthzDAO;
	
	@RequestMapping(value = ModelPathUtils.PROJECT_PATH + ".html")
	public String projectsPage(ModelMap model) {
		
		String user = SecurityUtil.getPersonGid();
		
		Data<Authorities> authoritiesData = facilityAuthzDAO.getAuthorities(user, facility);
		
		Facility facility = facilityAuthzDAO.get(this.facility).get();
		if(facility == null) {
			model.put("error", "Facility not found.");
			return ERROR_VIEW;
		}
			
		model.put("projectStatusOptions", EnumToOptionUtils.toList(Project.Status.values()));
		model.put("authorities", authoritiesData.get());
		model.put("facility", facility);
		return "frag/projects";
	}
	
	@RequestMapping(value = ModelPathUtils.PROJECT_PATH + "/{projectGid}.html")
	public String projectPage(@PathVariable String projectGid, ModelMap model) {	
		
		String user = SecurityUtil.getPersonGid();
		
		Data<Authorities> authoritiesData = projectAuthzDAO.getAuthorities(user, projectGid);
		
		Project project = projectAuthzDAO.get(user, projectGid).get();
		if(project == null) {
			model.put("error", "Project not found.");
			return ERROR_VIEW;
		}
		
		model.put("projectStatusOptions", EnumToOptionUtils.toList(Project.Status.values()));
		model.put("project", new ProjectFormBacker(project));	
		model.put("authorities", authoritiesData.get());
		return "frag/project";
	}

	public String getFacility() {
		return facility;
	}
	public void setFacility(String facility) {
		this.facility = facility;
	}
	
	public ProjectAuthzDAO getProjectAuthzDAO() {
		return projectAuthzDAO;
	}
	public void setProjectAuthzDAO(ProjectAuthzDAO projectAuthzDAO) {
		this.projectAuthzDAO = projectAuthzDAO;
	}

	public FacilityAuthzDAO getFacilityAuthzDAO() {
		return facilityAuthzDAO;
	}
	public void setFacilityAuthzDAO(FacilityAuthzDAO facilityAuthzDAO) {
		this.facilityAuthzDAO = facilityAuthzDAO;
	}
}
