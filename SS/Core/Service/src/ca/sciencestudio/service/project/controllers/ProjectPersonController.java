/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectPersonController class.
 *     
 */
package ca.sciencestudio.service.project.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ca.sciencestudio.model.project.ProjectPerson;
import ca.sciencestudio.model.project.ProjectRole;
import ca.sciencestudio.model.project.dao.ProjectPersonDAO;
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
public class ProjectPersonController extends AbstractModelController {

	@Autowired
	private ProjectPersonDAO projectPersonDAO;
	
	@RequestMapping(value = "/project/person/{projectPersonId}/remove.{format}")
	public String removeProjectPerson(@PathVariable int projectPersonId, @PathVariable String format, HttpServletRequest request, ModelMap model) {
		
		BindException errors = BindAndValidateUtils.buildBindException();
		model.put("errors", errors);
		
		String responseView = "response-" + format;
		
		ProjectPerson projectPerson = projectPersonDAO.getProjectPersonById(projectPersonId);
		if(projectPerson == null) {
			errors.reject("projectPerson.notfound", "Project person not found.");
			return responseView;
		}
		
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		Object exptr = AuthorityUtil.buildProjectExperimenterAuthority(projectPerson.getProjectId());
		
		if((projectPerson.getProjectRole() == ProjectRole.OBSERVER) && !SecurityUtil.hasAnyAuthority(exptr,admin)) {
			errors.reject("permission.denied", "Not permitted to remove Observer.");
			return responseView;
		}
		else if((projectPerson.getProjectRole() == ProjectRole.EXPERIMENTER) && !SecurityUtil.hasAnyAuthority(admin)) {
			errors.reject("permission.denied", "Not permitted to remove Experimenter.");
			return responseView;
		}
		
		projectPersonDAO.removeProjectPerson(projectPerson.getId());
		
		Map<String,String> response = new HashMap<String,String>();
		response.put("viewUrl", getModelPath(request) + ModelPathUtils.getProjectPersonsPath(String.valueOf(projectPerson.getProjectId()), ".html"));
		model.put("response", response);
		return responseView;
	}

	public ProjectPersonDAO getProjectPersonDAO() {
		return projectPersonDAO;
	}
	public void setProjectPersonDAO(ProjectPersonDAO projectPersonDAO) {
		this.projectPersonDAO = projectPersonDAO;
	}
}
