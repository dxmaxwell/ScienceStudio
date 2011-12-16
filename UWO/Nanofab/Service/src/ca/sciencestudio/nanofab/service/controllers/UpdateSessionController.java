/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		UpdateSessionExperimentController class.
 *     
 */
package ca.sciencestudio.nanofab.service.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.dao.ExperimentAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.web.FormResponseMap;

/**
 * @author maxweld
 *
 */
@Controller
public class UpdateSessionController extends AbstractLaboratoryAuthzController {
		
	private ExperimentAuthzDAO experimentAuthzDAO;
	
	@ResponseBody
	@RequestMapping(value = "/session*", method = RequestMethod.POST)
	public FormResponseMap handleRequest(@RequestParam(required = false) String experimentGid) {
		
		if(!canWriteLaboratory()) {
			return new FormResponseMap(false, "Must be controller to set experiment.");
		}
		
		if((experimentGid == null) || (experimentGid.trim().length() == 0)) {
			return new FormResponseMap(false, "Invalid Experiment GID.");
		}
		
		Experiment experiment = experimentAuthzDAO.get(SecurityUtil.getPersonGid(), experimentGid).get();
		if(experiment == null) { 
			return new FormResponseMap(false, "Experiment not found.");
		}
		
		nanofabSessionStateMap.setExperiment(experiment);
		return new FormResponseMap(true);
	}

	public ExperimentAuthzDAO getExperimentAuthzDAO() {
		return experimentAuthzDAO;
	}
	public void setExperimentAuthzDAO(ExperimentAuthzDAO experimentAuthzDAO) {
		this.experimentAuthzDAO = experimentAuthzDAO;
	}
}
