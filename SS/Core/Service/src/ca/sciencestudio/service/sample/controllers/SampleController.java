/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SampleController class.
 *     
 */
package ca.sciencestudio.service.sample.controllers;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.sample.dao.SampleDAO;
import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.dao.ExperimentDAO;
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
public class SampleController extends AbstractModelController {

	@Autowired
	private SampleDAO sampleDAO;

	@Autowired
	private ExperimentDAO experimentDAO;
	
	@RequestMapping(value = "/project/{projectId}/samples.{format}", method = RequestMethod.GET)
	public String getSampleList(@PathVariable int projectId, @PathVariable String format, ModelMap model) {
		
		List<Sample> sampleList = Collections.emptyList();
		
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		Object team = AuthorityUtil.buildProjectGroupAuthority(projectId);
		
		if(SecurityUtil.hasAnyAuthority(admin, team)) { 
			sampleList = sampleDAO.getSampleListByProjectId(projectId);
		}
		
		model.put("response", sampleList);
		return "response-" + format;
	}
	
	@RequestMapping(value = "/sample/{sampleId}/remove.{format}")
	public String removeSample(@PathVariable int sampleId, @PathVariable String format, HttpServletRequest request, ModelMap model) {
		
		BindException errors = BindAndValidateUtils.buildBindException();
		model.put("errors", errors);
		
		String responseView = "response-" + format;

		Sample sample = sampleDAO.getSampleById(sampleId);
		if(sample == null) {
			errors.reject("sample.notfound", "Sample not found.");
			return responseView;
		}
		
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		Object exptr = AuthorityUtil.buildProjectExperimenterAuthority(sample.getProjectId());
		
		if(!SecurityUtil.hasAnyAuthority(admin, exptr)) {
			errors.reject("permission.denied", "Not permitted to remove sample.");
			return responseView;
		}
		
		List<Experiment> experimentList = experimentDAO.getExperimentListBySampleId(sampleId);
		if(!experimentList.isEmpty()) {
			errors.reject("experiments.notempty", "Sample has associated experiments.");
			return responseView;
		}
		
		sampleDAO.removeSample(sampleId);
		
		Map<String,String> response = new HashMap<String,String>();
		response.put("viewUrl", getModelPath(request) + ModelPathUtils.getSamplesPath(sample.getProjectId(), ".html"));
		model.put("response", response);
		return responseView;
	}
	
	public SampleDAO getSampleDAO() {
		return sampleDAO;
	}
	public void setSampleDAO(SampleDAO sampleDAO) {
		this.sampleDAO = sampleDAO;
	}

	public ExperimentDAO getExperimentDAO() {
		return experimentDAO;
	}
	public void setExperimentDAO(ExperimentDAO experimentDAO) {
		this.experimentDAO = experimentDAO;
	}
}
