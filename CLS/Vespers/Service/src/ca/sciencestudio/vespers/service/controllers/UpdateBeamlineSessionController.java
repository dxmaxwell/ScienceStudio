/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		UpdateBeamlineSessionController class.
 *     
 */
package ca.sciencestudio.vespers.service.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.web.FormResponseMap;
import ca.sciencestudio.model.facility.dao.InstrumentTechniqueAuthzDAO;
import ca.sciencestudio.model.facility.dao.TechniqueAuthzDAO;
import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.dao.ExperimentAuthzDAO;

/**
 * @author maxweld
 *
 */
@Controller
public class UpdateBeamlineSessionController extends AbstractBeamlineAuthzController {
	
	private static final String VALUE_KEY_EXPERIMENT_GID = "experimentGid";
	private static final String VALUE_KEY_EXPERIMENT_NAME = "experimentName";
	
	private static final String VALUE_KEY_TECHNIQUE = "technique";
	private static final String VALUE_KEY_TECHNIQUE_CHANGE = "techniqueChanged";
	
	private TechniqueAuthzDAO techniqueAuthzDAO;
	private ExperimentAuthzDAO experimentAuthzDAO;
	private InstrumentTechniqueAuthzDAO instrumentTechniqueAuthzDAO;
	
	@ResponseBody
	@RequestMapping(value = "/session*", method = RequestMethod.POST)
	public FormResponseMap handleRequest(@RequestParam(required = false) String experimentGid) {
		
		if(!canWriteBeamline()) {
			return new FormResponseMap(false, "Not permitted to set session experiment.");
		}
		
		if((experimentGid == null) || (experimentGid.trim().length() == 0)) {
			return new FormResponseMap(false, "Invalid Experiment GID.");
		}
		
		Experiment experiment = experimentAuthzDAO.get(SecurityUtil.getPersonGid(), experimentGid).get();
		if(experiment == null) {
			return new FormResponseMap(false, "Experiment not found.");
		}
		
		beamlineSessionProxy.put(VALUE_KEY_EXPERIMENT_GID, experiment.getGid());
		beamlineSessionProxy.put(VALUE_KEY_EXPERIMENT_NAME, experiment.getName());
		beamlineSessionProxy.put(VALUE_KEY_TECHNIQUE, techniqueAuthzDAO.get(instrumentTechniqueAuthzDAO.get(experiment.getInstrumentTechniqueGid()).get().getTechniqueGid()).get().getName());
		beamlineSessionProxy.put(VALUE_KEY_TECHNIQUE_CHANGE, "Yes");
		
		return new FormResponseMap(true);
	}

	public TechniqueAuthzDAO getTechniqueAuthzDAO() {
		return techniqueAuthzDAO;
	}
	public void setTechniqueAuthzDAO(TechniqueAuthzDAO techniqueAuthzDAO) {
		this.techniqueAuthzDAO = techniqueAuthzDAO;
	}

	public ExperimentAuthzDAO getExperimentAuthzDAO() {
		return experimentAuthzDAO;
	}
	public void setExperimentAuthzDAO(ExperimentAuthzDAO experimentAuthzDAO) {
		this.experimentAuthzDAO = experimentAuthzDAO;
	}

	public InstrumentTechniqueAuthzDAO getInstrumentTechniqueAuthzDAO() {
		return instrumentTechniqueAuthzDAO;
	}
	public void setInstrumentTechniqueAuthzDAO(InstrumentTechniqueAuthzDAO instrumentTechniqueAuthzDAO) {
		this.instrumentTechniqueAuthzDAO = instrumentTechniqueAuthzDAO;
	}
}
