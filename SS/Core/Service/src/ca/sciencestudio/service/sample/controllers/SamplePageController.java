/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SamplePageController class.
 *     
 */
package ca.sciencestudio.service.sample.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectDAO;
import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.sample.SampleState;
import ca.sciencestudio.model.sample.dao.SampleDAO;
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.controllers.AbstractModelController;
import ca.sciencestudio.service.sample.backers.SampleFormBacker;

/**
 * @author maxweld
 *
 */
@Controller
public class SamplePageController extends AbstractModelController {

	private static final String ERROR_VIEW = "frag/error";
	
	@Autowired
	private ProjectDAO projectDAO;
	
	@Autowired
	private SampleDAO sampleDAO;
	
	@RequestMapping(value = "/project/{projectId}/samples.html", method = RequestMethod.GET)
	public String getSamplesPage(@PathVariable int projectId, ModelMap model) {
		
		Project project = projectDAO.getProjectById(projectId);
		if(project == null) {
			model.put("error", "Project not found.");	
			return ERROR_VIEW;
		}
		
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		Object team = AuthorityUtil.buildProjectGroupAuthority(projectId);
		
		if(!SecurityUtil.hasAnyAuthority(admin, team)) { 
			model.put("error", "Permission denied.");	
			return ERROR_VIEW;
		}
		
		model.put("sampleStateList", SampleState.getMarshallableValues(SampleState.UNKNOWN));
		model.put("project", project);
		return "frag/samples";
	}
	
	@RequestMapping(value = "/sample/{sampleId}.html", method = RequestMethod.GET)
	public String getSamplePage(@PathVariable int sampleId, ModelMap model) {
		
		Sample sample = sampleDAO.getSampleById(sampleId);
		if(sample == null) {
			model.put("error", "Sample not found.");
			return ERROR_VIEW;
		}

		Project project = projectDAO.getProjectById(sample.getProjectId());
		if(project == null) {
			model.put("error", "Project not found.");
			return ERROR_VIEW;
		}
		
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		Object team = AuthorityUtil.buildProjectGroupAuthority(project.getId());
		
		if(!SecurityUtil.hasAnyAuthority(admin, team)) {
			model.put("error", "Permission denied.");	
			return ERROR_VIEW;
		}
			
		model.put("sampleStateList", SampleState.getMarshallableValues(SampleState.UNKNOWN));
		model.put("sampleFormBacker", new SampleFormBacker(sample));
		model.put("project", project);
		model.put("sample", sample);
		return "frag/sample";
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}
	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public SampleDAO getSampleDAO() {
		return sampleDAO;
	}
	public void setSampleDAO(SampleDAO sampleDAO) {
		this.sampleDAO = sampleDAO;
	}
}
