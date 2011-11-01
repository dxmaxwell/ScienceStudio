/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectAuthorityController class.
 *     
 */
package ca.sciencestudio.rest.service.authz.controllers;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectBasicDAO;
import ca.sciencestudio.security.authz.accessors.ProjectAuthorityAccessor;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 * 
 *
 */
@Controller
public class ProjectAuthorityController extends AbstractAuthorityController {

	private static final String PROJECT_AUTHZ_PATH = "/projects";
	
	private ProjectBasicDAO projectBasicDAO;
	
	private ProjectAuthorityAccessor projectAuthorityAccessor;
	
	@ResponseBody
	@RequestMapping(value = PROJECT_AUTHZ_PATH + "/{gid}*", method = RequestMethod.GET)
	public Object getAuthorities(@RequestParam String user, @PathVariable String gid, HttpServletResponse response) {
		
		try {
			Project project = projectBasicDAO.get(gid);
			if(project == null) {
				response.setStatus(HttpStatus.NOT_FOUND.value());
				return EMPTY_AUTHORITIES;
			}
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return EMPTY_AUTHORITIES;
		}
		
		try {
			return projectAuthorityAccessor.getAuthorities(user, gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return EMPTY_AUTHORITIES;
		}
	}
	
	public ProjectBasicDAO getProjectBasicDAO() {
		return projectBasicDAO;
	}
	public void setProjectBasicDAO(ProjectBasicDAO projectBasicDAO) {
		this.projectBasicDAO = projectBasicDAO;
	}
	
	public ProjectAuthorityAccessor getProjectAuthorityAccessor() {
		return projectAuthorityAccessor;
	}
	public void setProjectAuthorityAccessor(ProjectAuthorityAccessor projectAuthorityAccessor) {
		this.projectAuthorityAccessor = projectAuthorityAccessor;
	}
}

