/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectController class.
 *     
 */
package ca.sciencestudio.service.project.controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.Project;
import ca.sciencestudio.model.ProjectPerson;
import ca.sciencestudio.model.dao.ProjectDAO;
import ca.sciencestudio.model.dao.ProjectPersonDAO;
import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.sample.dao.SampleDAO;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.SessionDAO;
import ca.sciencestudio.model.utilities.GID;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.security.util.SecurityUtil.ROLE;
import ca.sciencestudio.service.controllers.AbstractModelController;
import ca.sciencestudio.service.utilities.ModelPathUtils;
import ca.sciencestudio.util.web.BindAndValidateUtils;
import ca.sciencestudio.util.web.GeneralResponse;
import ca.sciencestudio.util.web.GenericResponse;

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
	
	@ResponseBody
	@RequestMapping(value = "/projects*")
	public Object getProjectList(@RequestParam(required = false) String role, @RequestParam(required = false) String status) {
		
		if((role == null) || (role.length() == 0)) {
			role = PARAM_VALUE_ROLE_USER;
		}
		
		if((status == null) || (status.length() == 0)) {
			status = Project.Status.ACTIVE.name();
		}
		
		Project.Status projectStatus = null;
		if(!status.equalsIgnoreCase(PARAM_VALUE_STATUS_ALL)) {
			try {
				projectStatus = Project.Status.valueOf(status);
			}
			catch(IllegalArgumentException e) {
				projectStatus = Project.Status.ACTIVE;
			}
		}
		
		List<Project> projectList;
		if(role.equalsIgnoreCase(PARAM_VALUE_ROLE_ADMIN) && SecurityUtil.hasAuthority(ROLE.ADMIN_PROJECTS)) {
			if(projectStatus == null) {
				projectList = projectDAO.getAll();
			}
			else {
				projectList = projectDAO.getAllByStatus(projectStatus.name());
			}
		}
		else {
			String personUid = SecurityUtil.getPerson().getGid();
			if(projectStatus == null) {
				projectList = projectDAO.getAllByPersonGid(personUid);
			}
			else {
				projectList = projectDAO.getAllByPersonGidAndStatus(personUid, projectStatus);
			}
		}
		
		return projectList;
	}
	
	@ResponseBody
	@RequestMapping(value = "/projects/{projectGid}/remove*")
	public GenericResponse<?> removeProject(@PathVariable String projectGid, HttpServletRequest request) {
		
		BindException errors = BindAndValidateUtils.buildBindException();
		
		Project project = projectDAO.get(projectGid);
		if(project == null) {
			errors.reject("project.notfound", "Project not found.");
			return new GeneralResponse(errors);
		}
		
		if(!SecurityUtil.hasAuthority(ROLE.ADMIN_PROJECTS)) {
			errors.reject("permission.denied", "Not permitted to remove project.");
			return new GeneralResponse(errors);
		}
		
		GID gid = GID.parse(projectGid);
		
		List<Session> sessionList = sessionDAO.getSessionListByProjectId(gid.getId());
		if(!sessionList.isEmpty()) {
			errors.reject("sessions.notempty", "Project has associated sessions.");
			return new GeneralResponse(errors);
		}
		
		List<Sample> sampleList = sampleDAO.getSampleListByProjectId(gid.getId());
		for(Sample sample : sampleList) {
			sampleDAO.removeSample(sample.getId());
		}
		
		List<ProjectPerson> projectPersonList = projectPersonDAO.getAllByProjectGid(projectGid);
		for(ProjectPerson projectPerson : projectPersonList) {
			projectPersonDAO.remove(projectPerson.getGid());
		}
		
		projectDAO.remove(projectGid);
		
		GenericResponse<Map<String,String>> response = new GenericResponse<Map<String,String>>(new HashMap<String,String>());
		response.getResponse().put("viewUrl", getModelPath(request) + ModelPathUtils.getProjectsPath(".html"));
		return response;
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
