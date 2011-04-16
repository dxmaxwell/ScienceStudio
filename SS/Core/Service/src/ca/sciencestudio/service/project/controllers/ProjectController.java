/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectController class.
 *     
 */
package ca.sciencestudio.service.project.controllers;

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
import org.springframework.web.bind.annotation.RequestParam;

import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.ProjectPerson;
import ca.sciencestudio.model.project.ProjectStatus;
import ca.sciencestudio.model.project.dao.ProjectDAO;
import ca.sciencestudio.model.project.dao.ProjectPersonDAO;
import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.sample.dao.SampleDAO;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.SessionDAO;
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
public class ProjectController extends AbstractModelController {
	
	private static final String PARAM_VALUE_ROLE_USER = "USER";
	private static final String PARAM_VALUE_ROLE_ADMIN = "ADMIN";
	
	private static final String PARAM_VALUE_STATUS_ALL = "ALL";
	
	@Autowired
	private SampleDAO sampleDAO;
	
	@Autowired
	private ProjectDAO projectDAO;
	
	@Autowired
	private ProjectPersonDAO projectPersonDAO;
	
	@Autowired
	private SessionDAO sessionDAO;
	
	@RequestMapping(value = "/projects.{format}")
	public String getProjectList(@RequestParam(required = false) String role,
									@RequestParam(required = false) String status,
										@PathVariable String format, ModelMap model) {
		
		if((role == null) || (role.length() == 0)) {
			role = PARAM_VALUE_ROLE_USER;
		}
		
		if((status == null) || (status.length() == 0)) {
			status = ProjectStatus.ACTIVE.name();
		}
		
		ProjectStatus projectStatus = null;
		if(!status.equalsIgnoreCase(PARAM_VALUE_STATUS_ALL)) {
			try {
				projectStatus = ProjectStatus.valueOf(status);
			}
			catch(IllegalArgumentException e) {
				projectStatus = ProjectStatus.ACTIVE;
			}
		}
		
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		
		List<Project> projectList;
		if(role.equalsIgnoreCase(PARAM_VALUE_ROLE_ADMIN) && SecurityUtil.hasAuthority(admin)) {
			if(projectStatus == null) {
				projectList = projectDAO.getProjectList();
			}
			else {
				projectList = projectDAO.getProjectListByStatus(projectStatus);
			}
		}
		else {
			String personUid = SecurityUtil.getPerson().getUid();
			if(projectStatus == null) {
				projectList = projectDAO.getProjectListByPersonUid(personUid);
			}
			else {
				projectList = projectDAO.getProjectListByPersonUidAndStatus(personUid, projectStatus);
			}
		}
		
		model.put("response", projectList);
		return "response-" + format;
	}
	
	@RequestMapping(value = "/project/{projectId}/remove.{format}")
	public String removeProject(@PathVariable int projectId, @PathVariable String format, HttpServletRequest request, ModelMap model) {
		
		BindException errors = BindAndValidateUtils.buildBindException();
		model.put("errors", errors);
		
		String responseView = "response-" + format;
		
		Project project = projectDAO.getProjectById(projectId);
		if(project == null) {
			errors.reject("project.notfound", "Project not found.");
			return responseView;
		}
		
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		
		if(!SecurityUtil.hasAuthority(admin)) {
			errors.reject("permission.denied", "Not permitted to remove project.");
			return responseView;
		}
		
		List<Session> sessionList = sessionDAO.getSessionListByProjectId(projectId);
		if(!sessionList.isEmpty()) {
			errors.reject("sessions.notempty", "Project has associated sessions.");
			return responseView;
		}
		
		List<Sample> sampleList = sampleDAO.getSampleListByProjectId(projectId);
		for(Sample sample : sampleList) {
			sampleDAO.removeSample(sample.getId());
		}
		
		List<ProjectPerson> projectPersonList = projectPersonDAO.getProjectPersonListByProjectId(projectId);
		for(ProjectPerson projectPerson : projectPersonList) {
			projectPersonDAO.removeProjectPerson(projectPerson.getId());
		}
		
		projectDAO.removeProject(projectId);
		
		Map<String,String> response = new HashMap<String,String>();
		response.put("viewUrl", getModelPath(request) + ModelPathUtils.getProjectsPath(".html"));
		model.put("response", response);
		return responseView;
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

	public SampleDAO getSampleDAO() {
		return sampleDAO;
	}
	public void setSampleDAO(SampleDAO sampleDAO) {
		this.sampleDAO = sampleDAO;
	}

	public ProjectPersonDAO getProjectPersonDAO() {
		return projectPersonDAO;
	}
	public void setProjectPersonDAO(ProjectPersonDAO projectPersonDAO) {
		this.projectPersonDAO = projectPersonDAO;
	}
}
