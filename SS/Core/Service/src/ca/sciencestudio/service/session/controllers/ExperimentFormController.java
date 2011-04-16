/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ExperimentFormController class.
 *     
 */
package ca.sciencestudio.service.session.controllers;

import java.util.Map;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.sciencestudio.model.facility.InstrumentTechnique;
import ca.sciencestudio.model.facility.dao.InstrumentTechniqueDAO;
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectDAO;
import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.dao.ExperimentDAO;
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.controllers.AbstractModelController;
import ca.sciencestudio.service.session.backers.ExperimentFormBacker;
import ca.sciencestudio.service.session.validators.ExperimentFormBackerValidator;
import ca.sciencestudio.service.utilities.ModelPathUtils;
import ca.sciencestudio.util.web.BindAndValidateUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class ExperimentFormController extends AbstractModelController {

	@Autowired
	private ProjectDAO projectDAO;
	
	@Autowired
	private ExperimentDAO experimentDAO;
	
	@Autowired
	private InstrumentTechniqueDAO instrumentTechniqueDAO;
	
	@Autowired
	private ExperimentFormBackerValidator ExperimentFormBackerValidator;
	
	@RequestMapping(value = "/session/{sessionId}/experiments/form/add.{format}", method = RequestMethod.POST)
	public String postExperimentFormAdd(@PathVariable int sessionId, @PathVariable String format, 
															HttpServletRequest request, ModelMap model) {
		
		ExperimentFormBacker experimentFormBacker = new ExperimentFormBacker(sessionId);
		BindException errors = BindAndValidateUtils.buildBindException(experimentFormBacker);
		model.put("errors", errors);
		
		String responseView = "response-" + format;
		
		Project project = projectDAO.getProjectBySessionId(sessionId);
		if(project == null) {
			errors.reject("project.notfound", "Project not found.");
			return responseView;
		}
		
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		Object exptr = AuthorityUtil.buildProjectExperimenterAuthority(project.getId());
		
		if(!SecurityUtil.hasAnyAuthority(exptr, admin)) {
			errors.reject("", "Permission denied");
			return responseView;
		}
		
		errors = BindAndValidateUtils.bindAndValidate(experimentFormBacker, request, ExperimentFormBackerValidator);
		if(errors.hasErrors()) {
			model.put("errors", errors);
			return responseView;
		}
		
		Experiment experiment = experimentFormBacker.createExperiment(experimentDAO);
		experimentDAO.addExperiment(experiment);
		
		Map<String,String> response = new HashMap<String, String>();
		response.put("viewUrl", getModelPath(request) + ModelPathUtils.getExperimentPath(experiment.getId(), ".html"));

		model.put("response", response);
		return responseView;
	}

	@RequestMapping(value = "/experiment/{experimentId}/form/edit.{format}", method = RequestMethod.POST)
	public String postExperimentFormEdit(@PathVariable int experimentId, @PathVariable String format,
																HttpServletRequest request, ModelMap model) {
	
		BindException errors = BindAndValidateUtils.buildBindException();
		model.put("errors", errors);
		
		String responseView = "response-" + format;
		
		Project project = projectDAO.getProjectByExperimentId(experimentId);
		if(project == null) {
			errors.reject("project.notfound", "Project not found.");
			return responseView;
		}
		
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		Object exptr = AuthorityUtil.buildProjectExperimenterAuthority(project.getId());
		
		if(!SecurityUtil.hasAnyAuthority(exptr, admin)) {
			errors.reject("permission.denied", "Permission denied.");
			return responseView;
		}
		
		Experiment experiment = experimentDAO.getExperimentById(experimentId);
		if(experiment == null) {
			errors.reject("experiment.notfound", "Experiment not found.");
			return responseView;
		}
		
		InstrumentTechnique instrumentTechnique = instrumentTechniqueDAO.getInstrumentTechniqueById(experiment.getInstrumentTechniqueId()); 
		if(instrumentTechnique == null) {
			errors.reject("instrumenttechnique.notfound", "Instrument technique not found.");
			return responseView;
		}
		
		ExperimentFormBacker experimentFormBacker = new ExperimentFormBacker(experiment, instrumentTechnique);
		errors = BindAndValidateUtils.bindAndValidate(experimentFormBacker, request, ExperimentFormBackerValidator);
		if(errors.hasErrors()) {
			model.put("errors", errors);
			return responseView;
		}
		
		experimentDAO.editExperiment(experimentFormBacker.createExperiment(experimentDAO));
		
		Map<String,String> response = new HashMap<String, String>();
		response.put("message", "Experiment saved.");

		model.put("response", response);
		return responseView;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}
	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public ExperimentDAO getExperimentDAO() {
		return experimentDAO;
	}
	public void setExperimentDAO(ExperimentDAO experimentDAO) {
		this.experimentDAO = experimentDAO;
	}

	public InstrumentTechniqueDAO getInstrumentTechniqueDAO() {
		return instrumentTechniqueDAO;
	}
	public void setInstrumentTechniqueDAO(InstrumentTechniqueDAO instrumentTechniqueDAO) {
		this.instrumentTechniqueDAO = instrumentTechniqueDAO;
	}

	public ExperimentFormBackerValidator getExperimentFormBackerValidator() {
		return ExperimentFormBackerValidator;
	}
	public void setExperimentFormBackerValidator(ExperimentFormBackerValidator experimentFormBackerValidator) {
		ExperimentFormBackerValidator = experimentFormBackerValidator;
	}
}
