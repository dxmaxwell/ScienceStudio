/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		UpdateSessionExperimentController class.
 *     
 */
package ca.sciencestudio.nanofab.service.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.dao.ExperimentDAO;
import ca.sciencestudio.nanofab.state.NanofabSessionStateMap;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.web.BindAndValidateUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class UpdateSessionController {
		
	private ExperimentDAO experimentDAO;
	private NanofabSessionStateMap nanofabSessionStateMap;
	
	@RequestMapping(value = "/session.{format}", method = RequestMethod.POST)
	public String handleRequest(@RequestParam int experimentId, @PathVariable String format, ModelMap model) {
		
		BindException errors = BindAndValidateUtils.buildBindException();
		model.put("errors", errors);
		
		String responseView = "response-" + format;
		
		String personUid = nanofabSessionStateMap.getControllerUid();
		if(!SecurityUtil.getPerson().getUid().equals(personUid)) {
			errors.reject("permission.denied", "Must be controller to set experiment.");
			return responseView;
		}
		
		Experiment experiment = experimentDAO.getExperimentById(experimentId);
		if(experiment == null) { 
			errors.reject("experiment.notfound", "Experiment not found.");
			return responseView;
		}
		
		nanofabSessionStateMap.setExperiment(experiment);
		
		return responseView;
	}

	public ExperimentDAO getExperimentDAO() {
		return experimentDAO;
	}
	public void setExperimentDAO(ExperimentDAO experimentDAO) {
		this.experimentDAO = experimentDAO;
	}
	
	public NanofabSessionStateMap getNanofabSessionStateMap() {
		return nanofabSessionStateMap;
	}
	public void setNanofabSessionStateMap(NanofabSessionStateMap nanofabSessionStateMap) {
		this.nanofabSessionStateMap = nanofabSessionStateMap;
	}
}
