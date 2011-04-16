/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		UpdateBeamlineSessionController class.
 *     
 */
package ca.sciencestudio.vespers.service.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.state.StateMap;
import ca.sciencestudio.util.web.BindAndValidateUtils;
import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.dao.ExperimentDAO;

/**
 * @author maxweld
 *
 */
@Controller
public class UpdateBeamlineSessionController {
	
	private static final String STATE_KEY_CONTROLLER_UID = "controllerUid";
	
	private static final String VALUE_KEY_EXPERIMENT_ID = "experimentId";
	private static final String VALUE_KEY_EXPERIMENT_NAME = "experimentName";
	
	private ExperimentDAO experimentDAO;
	private StateMap beamlineSessionStateMap;
	
	@RequestMapping(value = "/session.{format}", method = RequestMethod.POST)
	public String handleRequest(@RequestParam int experimentId, @PathVariable String format, ModelMap model) {
		
		BindException errors = BindAndValidateUtils.buildBindException();
		model.put("errors", errors);
		
		String responseView = "response-" + format;
		
		String personUid = (String) beamlineSessionStateMap.get(STATE_KEY_CONTROLLER_UID);
		
		if(!SecurityUtil.getPerson().getUid().equals(personUid)) {
			errors.reject("permission.denied", "Not permitted to set session experiment.");
			return responseView;
		}
		
		Experiment experiment = experimentDAO.getExperimentById(experimentId);
		if(experiment == null) { 
			errors.reject("experiment.invalid", "Experiment not found.");
			return responseView;
		}
		
		beamlineSessionStateMap.put(VALUE_KEY_EXPERIMENT_ID, experiment.getId());
		beamlineSessionStateMap.put(VALUE_KEY_EXPERIMENT_NAME, experiment.getName());
		
		return responseView;
	}

	public ExperimentDAO getExperimentDAO() {
		return experimentDAO;
	}
	public void setExperimentDAO(ExperimentDAO experimentDAO) {
		this.experimentDAO = experimentDAO;
	}
	
	public StateMap getBeamlineSessionStateMap() {
		return beamlineSessionStateMap;
	}
	public void setBeamlineSessionStateMap(StateMap beamlineSessionStateMap) {
		this.beamlineSessionStateMap = beamlineSessionStateMap;
	}
}
