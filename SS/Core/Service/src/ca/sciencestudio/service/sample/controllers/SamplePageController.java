/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SamplePageController class.
 *     
 */
package ca.sciencestudio.service.sample.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectAuthzDAO;
import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.sample.dao.SampleAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.controllers.AbstractModelController;
import ca.sciencestudio.service.sample.backers.SampleFormBacker;
import ca.sciencestudio.service.utilities.ModelPathUtils;
import ca.sciencestudio.util.authz.Authorities;
import ca.sciencestudio.util.web.EnumToOptionUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class SamplePageController extends AbstractModelController {

	private SampleAuthzDAO sampleAuthzDAO;
	
	private ProjectAuthzDAO projectAuthzDAO;
	
	@RequestMapping(value = ModelPathUtils.SAMPLE_PATH + ".html")
	public String getSamplesPage(@RequestParam("project") String projectGid, ModelMap model) {
		
		String user = SecurityUtil.getPersonGid();
		
		Data<Authorities> authoritiesData = projectAuthzDAO.getAuthorities(user, projectGid);
		
		Project project = projectAuthzDAO.get(user, projectGid).get();
		if(project == null) {
			model.put("error", "Project not found.");	
			return ERROR_VIEW;
		}
		
		model.put("sampleStateOptions", EnumToOptionUtils.toList(Sample.State.values()));
		model.put("authorities", authoritiesData.get());
		model.put("project", project);
		return "frag/samples";
	}
	
	@RequestMapping(value = ModelPathUtils.SAMPLE_PATH + "/{sampleGid}.html")
	public String getSamplePage(@PathVariable String sampleGid, ModelMap model) {
		
		String user = SecurityUtil.getPersonGid();
		
		Sample sample = sampleAuthzDAO.get(user, sampleGid).get();
		if(sample == null) {
			model.put("error", "Sample not found.");
			return ERROR_VIEW;
		}
		
		Data<Project> projectData = projectAuthzDAO.get(user, sample.getProjectGid());
		
		Data<Authorities> authoritiesData = projectAuthzDAO.getAuthorities(user, sample.getProjectGid());
		
		Project project = projectData.get();
		if(project == null) {
			model.put("error", "Project not found.");
			return ERROR_VIEW;
		}
			
		model.put("sampleStateOptions", EnumToOptionUtils.toList(Sample.State.values()));
		model.put("sample", new SampleFormBacker(sample));
		model.put("authorities", authoritiesData.get());
		model.put("project", project);
		return "frag/sample";
	}

	public ProjectAuthzDAO getProjectAuthzDAO() {
		return projectAuthzDAO;
	}
	public void setProjectAuthzDAO(ProjectAuthzDAO projectAuthzDAO) {
		this.projectAuthzDAO = projectAuthzDAO;
	}

	public SampleAuthzDAO getSampleAuthzDAO() {
		return sampleAuthzDAO;
	}
	public void setSampleAuthzDAO(SampleAuthzDAO sampleAuthzDAO) {
		this.sampleAuthzDAO = sampleAuthzDAO;
	}
}
