/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ScanDataViewController class.
 *     
 */
package ca.sciencestudio.service.app.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
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
import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.dao.ExperimentAuthzDAO;
import ca.sciencestudio.model.session.dao.ScanAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;

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
	
	private ScanAuthzDAO scanAuthzDAO;
	
	private FacilityAuthzDAO facilityAuthzDAO;
	
	private TechniqueAuthzDAO techniqueAuthzDAO;
	
	private InstrumentAuthzDAO instrumentAuthzDAO;
	
	private ExperimentAuthzDAO experimentAuthzDAO;
	
	private LaboratoryAuthzDAO laboratoryAuthzDAO;
	
	private InstrumentTechniqueAuthzDAO instrumentTechniqueAuthzDAO;
	
	@RequestMapping(value = "/scan/data/view.html", params = "scan")
	public String viewScanData(@RequestParam("scan") String scanGid, ModelMap model) {
		
		String user = SecurityUtil.getPersonGid();
		
		Scan scan = scanAuthzDAO.get(user, scanGid).get();
		if(scan == null) {
			model.put("error", "Scan not found.");
			return ERROR_VIEW;
		}
		
		Experiment experiment = experimentAuthzDAO.get(user, scan.getExperimentGid()).get();
		if(experiment == null) {
			model.put("error", "Experiment not found.");
			return ERROR_VIEW;
		}
		
		InstrumentTechnique instrumentTechnique = instrumentTechniqueAuthzDAO.get(user, experiment.getInstrumentTechniqueGid()).get();
		if(instrumentTechnique == null) {
			model.put("error", "Instrument and technique not found.");
			return ERROR_VIEW;
		}
	
		Technique technique = techniqueAuthzDAO.get(user, instrumentTechnique.getTechniqueGid()).get();
		if(technique == null) {
			model.put("error", "Technique not found.");
			return ERROR_VIEW;
		}
		
		Instrument instrument = instrumentAuthzDAO.get(user, instrumentTechnique.getInstrumentGid()).get();
		if(instrument == null) {
			model.put("error", "Instrument not found.");
			return ERROR_VIEW;
		}
		
		Laboratory laboratory = laboratoryAuthzDAO.get(user, instrument.getLaboratoryGid()).get();
		if(laboratory == null) {
			model.put("error", "Laboratory not found.");
			return ERROR_VIEW;
		}
		
		Facility facility = facilityAuthzDAO.get(user, laboratory.getFacilityGid()).get();
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
			redirectUrl.append(REDIRECT_URL_SUFFIX).append("?scan=").append(scanGid);
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
}
