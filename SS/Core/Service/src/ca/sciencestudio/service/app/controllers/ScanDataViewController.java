/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     MainPageController class.
 *     
 */
package ca.sciencestudio.service.app.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
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
import ca.sciencestudio.model.facility.dao.FacilityDAO;
import ca.sciencestudio.model.facility.dao.InstrumentDAO;
import ca.sciencestudio.model.facility.dao.InstrumentTechniqueDAO;
import ca.sciencestudio.model.facility.dao.LaboratoryDAO;
import ca.sciencestudio.model.facility.dao.TechniqueDAO;
import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.dao.ExperimentDAO;
import ca.sciencestudio.model.session.dao.ScanDAO;

/**
 * @author maxweld
 *
 */
@Controller
public class ScanDataViewController {
	
	private static final String REDIRECT_URL_PREFIX = "/ssdata";
	private static final String REDIRECT_URL_SUFFIX = "/main.html";
	private static final String REDIRECT_URL_SEPARATOR = "/";
	
	private static final String SANITIZE_REPLACE_ALL = "";
	private static final String SANITIZE_URL_ENCODING = "UTF-8";
	private static final Pattern SANITIZE_DIRTY_PATTERN = Pattern.compile("[^a-zA-Z0-9]+");
	
	private static final String ERROR_VIEW = "page/error";
	
	@Autowired
	private ScanDAO scanDAO;
	
	@Autowired
	private FacilityDAO facilityDAO;
	
	@Autowired
	private TechniqueDAO techniqueDAO;
	
	@Autowired
	private InstrumentDAO instrumentDAO;
	
	@Autowired
	private ExperimentDAO experimentDAO;
	
	@Autowired
	private LaboratoryDAO laboratoryDAO;
	
	@Autowired
	private InstrumentTechniqueDAO instrumentTechniqueDAO;
	
	@RequestMapping(value = "/scan/data/view.html", method = RequestMethod.GET)
	public String viewScanData(@RequestParam int scanId, ModelMap model) {
		
		Scan scan = scanDAO.getScanById(scanId);
		if(scan == null) {
			model.put("error", "Scan not found.");
			return ERROR_VIEW;
		}
		
		Experiment experiment = experimentDAO.getExperimentById(scan.getExperimentId());
		if(experiment == null) {
			model.put("error", "Experiment not found.");
			return ERROR_VIEW;
		}
		
		InstrumentTechnique instrumentTechnique = instrumentTechniqueDAO.getInstrumentTechniqueById(experiment.getInstrumentTechniqueId());
		if(instrumentTechnique == null) {
			model.put("error", "Instrument and technique not found.");
			return ERROR_VIEW;
		}
	
		Technique technique = techniqueDAO.getTechniqueById(instrumentTechnique.getTechniqueId());
		if(technique == null) {
			model.put("error", "Technique not found.");
			return ERROR_VIEW;
		}
		
		Instrument instrument = instrumentDAO.getInstrumentById(instrumentTechnique.getInstrumentId());
		if(instrument == null) {
			model.put("error", "Instrument not found.");
			return ERROR_VIEW;
		}
		
		Laboratory laboratory = laboratoryDAO.getLaboratoryById(instrument.getLaboratoryId());
		if(laboratory == null) {
			model.put("error", "Laboratory not found.");
			return ERROR_VIEW;
		}
		
		Facility facility = facilityDAO.getFacilityById(laboratory.getFacilityId());
		if(facility == null) {
			model.put("error", "Facility not found.");
			return ERROR_VIEW;
		}
		
		StringBuffer redirectUrl = new StringBuffer();
		try {
			redirectUrl.append(REDIRECT_URL_PREFIX);
			redirectUrl.append(REDIRECT_URL_SEPARATOR).append(sanitize(facility.getName()));
			redirectUrl.append(REDIRECT_URL_SEPARATOR).append(sanitize(laboratory.getName()));
			redirectUrl.append(REDIRECT_URL_SEPARATOR).append(sanitize(instrument.getName()));
			redirectUrl.append(REDIRECT_URL_SEPARATOR).append(sanitize(technique.getName()));
			redirectUrl.append(REDIRECT_URL_SUFFIX).append("?scanId=").append(scanId);
		}
		catch (Exception e) {
			model.put("error", "Error while constructing redirect URL.");
			return ERROR_VIEW;
		}
		
		return "redirect:" + redirectUrl;
	}
	
	protected String sanitize(String name) throws UnsupportedEncodingException {
		Matcher matcher = SANITIZE_DIRTY_PATTERN.matcher(name.toLowerCase());
		return URLEncoder.encode(matcher.replaceAll(SANITIZE_REPLACE_ALL), SANITIZE_URL_ENCODING);
	}
	
	public ScanDAO getScanDAO() {
		return scanDAO;
	}
	public void setScanDAO(ScanDAO scanDAO) {
		this.scanDAO = scanDAO;
	}

	public TechniqueDAO getTechniqueDAO() {
		return techniqueDAO;
	}
	public void setTechniqueDAO(TechniqueDAO techniqueDAO) {
		this.techniqueDAO = techniqueDAO;
	}

	public InstrumentDAO getInstrumentDAO() {
		return instrumentDAO;
	}
	public void setInstrumentDAO(InstrumentDAO instrumentDAO) {
		this.instrumentDAO = instrumentDAO;
	}

	public ExperimentDAO getExperimentDAO() {
		return experimentDAO;
	}
	public void setExperimentDAO(ExperimentDAO experimentDAO) {
		this.experimentDAO = experimentDAO;
	}

	public LaboratoryDAO getLaboratoryDAO() {
		return laboratoryDAO;
	}
	public void setLaboratoryDAO(LaboratoryDAO laboratoryDAO) {
		this.laboratoryDAO = laboratoryDAO;
	}

	public InstrumentTechniqueDAO getInstrumentTechniqueDAO() {
		return instrumentTechniqueDAO;
	}
	public void setInstrumentTechniqueDAO(InstrumentTechniqueDAO instrumentTechniqueDAO) {
		this.instrumentTechniqueDAO = instrumentTechniqueDAO;
	}
}
