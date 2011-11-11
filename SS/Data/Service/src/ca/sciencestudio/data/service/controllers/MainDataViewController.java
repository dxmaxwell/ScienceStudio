/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     MainDataViewController class.
 *     
 */
package ca.sciencestudio.data.service.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sciencestudio.model.facility.Facility;
import ca.sciencestudio.model.facility.Instrument;
import ca.sciencestudio.model.facility.InstrumentTechnique;
import ca.sciencestudio.model.facility.Laboratory;
import ca.sciencestudio.model.facility.Technique;
import ca.sciencestudio.model.facility.dao.FacilityAuthzDAO;
import ca.sciencestudio.model.facility.dao.InstrumentAuthzDAO;
import ca.sciencestudio.model.facility.dao.InstrumentTechniqueAuthzDAO;
import ca.sciencestudio.model.facility.dao.LaboratoryAuthzDAO;
import ca.sciencestudio.model.facility.dao.TechniqueAuthzDAO;
import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.dao.ExperimentAuthzDAO;
import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.dao.ScanAuthzDAO;
import ca.sciencestudio.service.session.backers.ScanFormBacker;
import ca.sciencestudio.security.util.SecurityUtil;

/**
 * @author maxweld
 *
 */
@Controller
public class MainDataViewController {
	
	private String errorView = "page/error";
	
	private String defaultDataView = "page/generic";
	
	private ScanAuthzDAO scanAuthzDAO;
	
	private FacilityAuthzDAO facilityAuthzDAO;
	
	private TechniqueAuthzDAO techniqueAuthzDAO;
	
	private InstrumentAuthzDAO instrumentAuthzDAO;
	
	private ExperimentAuthzDAO experimentAuthzDAO;
	
	private LaboratoryAuthzDAO laboratoryAuthzDAO;
	
	private InstrumentTechniqueAuthzDAO instrumentTechniqueAuthzDAO;
	
	private Map<String,String> dataViewMap = new HashMap<String,String>();
	
	@RequestMapping(value = "/main.html", method = RequestMethod.GET)
	public String main(@RequestParam("scan") String scanGid, ModelMap model) {
	
		String user = SecurityUtil.getPersonGid();
		
		Scan scan = scanAuthzDAO.get(user, scanGid).get();
		if(scan == null) {
			model.put("error", "Scan not found.");
			return errorView;
		}
		
		Experiment experiment = experimentAuthzDAO.get(user, scan.getExperimentGid()).get();
		if(experiment == null) {
			model.put("error", "Experiment not found.");
			return errorView;
		}
		
		InstrumentTechnique instrumentTechnique = instrumentTechniqueAuthzDAO.get(user, experiment.getInstrumentTechniqueGid()).get();
		if(instrumentTechnique == null) {
			model.put("error", "Instrument and technique not found.");
			return errorView;
		}
	
		Technique technique = techniqueAuthzDAO.get(user, instrumentTechnique.getTechniqueGid()).get();
		if(technique == null) {
			model.put("error", "Technique not found.");
			return errorView;
		}
		
		Instrument instrument = instrumentAuthzDAO.get(user, instrumentTechnique.getInstrumentGid()).get();
		if(instrument == null) {
			model.put("error", "Instrument not found.");
			return errorView;
		}
		
		Laboratory laboratory = laboratoryAuthzDAO.get(user, instrument.getLaboratoryGid()).get();
		if(laboratory == null) {
			model.put("error", "Laboratory not found.");
			return errorView;
		}
		
		Facility facility = facilityAuthzDAO.get(user, laboratory.getFacilityGid()).get();
		if(facility == null) {
			model.put("error", "Facility not found.");
			return errorView;
		}
		
		String dataViewKey = buildDataViewKey(technique, instrument, laboratory, facility);
		
		String dataView = defaultDataView;
		for(Map.Entry<String,String> entry : dataViewMap.entrySet()) {
			if(entry.getKey().equalsIgnoreCase(dataViewKey)) {
				dataView = entry.getValue();
				break;
			}
		}
				
		model.put("scan", new ScanFormBacker(scan));
		return dataView;
	}
	
	protected String buildDataViewKey(Technique technique, Instrument instrument, Laboratory laboratory, Facility facility) {
		return facility.getName() + "/" + laboratory.getName() + "/" + instrument.getName() + "/" + technique.getName();
	}

	public String getErrorView() {
		return errorView;
	}
	public void setErrorView(String errorView) {
		this.errorView = errorView;
	}
	
	public String getDefaultDataView() {
		return defaultDataView;
	}
	public void setDefaultDataView(String defaultDataView) {
		this.defaultDataView = defaultDataView;
	}

	public ScanAuthzDAO getScanAuthzDAO() {
		return scanAuthzDAO;
	}
	public void setScanAuthzDAO(ScanAuthzDAO scanAuthzDAO) {
		this.scanAuthzDAO = scanAuthzDAO;
	}

	public FacilityAuthzDAO getFacilityAuthzDAO() {
		return facilityAuthzDAO;
	}
	public void setFacilityAuthzDAO(FacilityAuthzDAO facilityAuthzDAO) {
		this.facilityAuthzDAO = facilityAuthzDAO;
	}

	public TechniqueAuthzDAO getTechniqueAuthzDAO() {
		return techniqueAuthzDAO;
	}
	public void setTechniqueAuthzDAO(TechniqueAuthzDAO techniqueAuthzDAO) {
		this.techniqueAuthzDAO = techniqueAuthzDAO;
	}

	public InstrumentAuthzDAO getInstrumentAuthzDAO() {
		return instrumentAuthzDAO;
	}
	public void setInstrumentAuthzDAO(InstrumentAuthzDAO instrumentAuthzDAO) {
		this.instrumentAuthzDAO = instrumentAuthzDAO;
	}

	public ExperimentAuthzDAO getExperimentAuthzDAO() {
		return experimentAuthzDAO;
	}
	public void setExperimentAuthzDAO(ExperimentAuthzDAO experimentAuthzDAO) {
		this.experimentAuthzDAO = experimentAuthzDAO;
	}

	public LaboratoryAuthzDAO getLaboratoryAuthzDAO() {
		return laboratoryAuthzDAO;
	}
	public void setLaboratoryAuthzDAO(LaboratoryAuthzDAO laboratoryAuthzDAO) {
		this.laboratoryAuthzDAO = laboratoryAuthzDAO;
	}

	public InstrumentTechniqueAuthzDAO getInstrumentTechniqueAuthzDAO() {
		return instrumentTechniqueAuthzDAO;
	}
	public void setInstrumentTechniqueAuthzDAO(InstrumentTechniqueAuthzDAO instrumentTechniqueAuthzDAO) {
		this.instrumentTechniqueAuthzDAO = instrumentTechniqueAuthzDAO;
	}

	public Map<String, String> getDataViewMap() {
		return dataViewMap;
	}
	public void setDataViewMap(Map<String, String> dataViewMap) {
		this.dataViewMap = dataViewMap;
	}
}
