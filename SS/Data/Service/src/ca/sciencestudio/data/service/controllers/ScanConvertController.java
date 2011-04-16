/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     GenericConvertController class.
 *     
 */
package ca.sciencestudio.data.service.controllers;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.sciencestudio.data.converter.Converter;
import ca.sciencestudio.data.converter.ConverterMap;
import ca.sciencestudio.data.converter.LinkedHashConverterMap;
import ca.sciencestudio.data.converter.factory.ConverterFactory;
import ca.sciencestudio.data.standard.StdConverter;
import ca.sciencestudio.data.support.ConverterFactoryException;
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
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectDAO;
import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.sample.dao.SampleDAO;
import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.ExperimentDAO;
import ca.sciencestudio.model.session.dao.ScanDAO;
import ca.sciencestudio.model.session.dao.SessionDAO;
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.web.BindAndValidateUtils;

/**
 * @author maxweld
 *
 */
@Controller
@RequestMapping("/scan/{scanId}/convert")
public class ScanConvertController implements StdConverter {

	@Autowired
	private ScanDAO scanDAO;
	
	@Autowired
	private SampleDAO sampleDAO;
	
	@Autowired
	private SessionDAO sessionDAO;
	
	@Autowired
	private ProjectDAO projectDAO;
	
	@Autowired
	private FacilityDAO facilityDAO;
	
	@Autowired
	private ExperimentDAO experimentDAO;
	
	@Autowired
	private LaboratoryDAO laboratoryDAO;
	
	@Autowired
	private TechniqueDAO techniqueDAO;
	
	@Autowired
	private InstrumentDAO instrumentDAO;
	
	@Autowired
	private InstrumentTechniqueDAO instrumentTechniqueDAO;
	
	private ConverterFactory converterFactory;
	
	private Map<String,ConvertThread> convertThreadMap = Collections.synchronizedMap(new HashMap<String,ConvertThread>());
	
	protected Log logger = LogFactory.getLog(getClass());
	
	@RequestMapping(value = "/{fromFormat}/{toFormat}.{format}", method = RequestMethod.GET)
	public String convert(@PathVariable int scanId, @PathVariable String fromFormat,
							@PathVariable String toFormat, @PathVariable String format, ModelMap model) {
		
		BindException errors = BindAndValidateUtils.buildBindException();
		
		String responseView = "response-" + format;
		
		Project project = projectDAO.getProjectByScanId(scanId);
		if(project == null) {
			errors.reject("project.invalid", "Project not found.");
			model.put("errors", errors);
			return responseView;
		}
		
		int projectId = project.getId();
		
		Object admin = AuthorityUtil.ROLE_ADMIN_DATA;
		Object group = AuthorityUtil.buildProjectGroupAuthority(projectId);
		
		if(!SecurityUtil.hasAnyAuthority(group, admin)) {
			errors.reject("permission.denied", "Not permitted to convert data.");
			model.put("errors", errors);
			return responseView;
		}

		String convertThreadKey = buildConvertThreadKey(scanId, fromFormat, toFormat);
		if(convertThreadMap.containsKey(convertThreadKey)) {
			checkConvertThread(scanId, fromFormat, toFormat, errors, model);
			return responseView;
		}
		
		Scan scan = scanDAO.getScanById(scanId);
		if(scan == null) {
			errors.reject("scan.invalid", "Scan not found.");
			model.put("errors", errors);
			return responseView;
		}
		
		Date scanStartDate = scan.getStartDate();
		Date scanEndDate = scan.getEndDate();
		if((scanStartDate == null) || (scanEndDate == null)) {
			Map<String,Object> response = new HashMap<String,Object>();
			response.put("message", "Scan is incomplete.");
			response.put("complete", false);
			model.put("response", response);
			return responseView;
		}
		
		Experiment experiment = experimentDAO.getExperimentById(scan.getExperimentId());
		if(experiment == null) {
			errors.reject("experiment.invalid", "Experiment not found.");
			model.put("errors", errors);
			return responseView;
		}
		
		Session session = sessionDAO.getSessionById(experiment.getSessionId());
		if(session == null) {
			errors.reject("session.invalid", "Session not found");
			model.put("errors", errors);
			return responseView;
		}
		
		InstrumentTechnique it = instrumentTechniqueDAO.getInstrumentTechniqueById(experiment.getInstrumentTechniqueId());
		if(it == null) {
			errors.reject("instrumentTechnique.invalid", "Instrument technique not found.");
			model.put("errors", errors);
			return responseView;
		}
		
		Technique technique = techniqueDAO.getTechniqueById(it.getTechniqueId());
		if(technique == null) {
			errors.reject("technique.invalid", "Technique not found.");
			model.put("errors", errors);
			return responseView;
		}
		
		Instrument instrument = instrumentDAO.getInstrumentById(it.getInstrumentId());
		if(instrument == null) {
			errors.reject("instrument.invalid", "Instrument not found.");
			model.put("errors", errors);
			return responseView;
		}
		
		Sample sample = sampleDAO.getSampleById(experiment.getSampleId());
		if(sample == null) {
			errors.reject("sample.invalid", "Sample not found.");
			model.put("errors", errors);
			return responseView;
		}
		
		Laboratory laboratory = laboratoryDAO.getLaboratoryById(instrument.getLaboratoryId());
		if(laboratory == null) {
			errors.reject("laboratory.invalid", "Laboratory not found.");
			model.put("errors", errors);
			return responseView;
		}
		
		Facility facility = facilityDAO.getFacilityById(laboratory.getFacilityId());
		if(facility == null) {
			errors.reject("facility.invalid", "Facility not found.");
			model.put("errors", errors);
			return responseView;
		}
		
		ConverterMap convertRequest = new LinkedHashConverterMap(fromFormat, toFormat);
		convertRequest.put(REQUEST_KEY_SCAN, scan);
		convertRequest.put(REQUEST_KEY_SAMPLE, sample);
		convertRequest.put(REQUEST_KEY_SESSION, session);
		convertRequest.put(REQUEST_KEY_PROJECT, project);
		convertRequest.put(REQUEST_KEY_FACILITY, facility);
		convertRequest.put(REQUEST_KEY_EXPERIMENT, experiment);
		convertRequest.put(REQUEST_KEY_LABORATORY, laboratory);
		convertRequest.put(REQUEST_KEY_INSTRUMENT, instrument);
		convertRequest.put(REQUEST_KEY_TECHNIQUE, technique);
		
		Converter converter;  
		try {
			converter = converterFactory.getConverter(convertRequest);
		}
		catch(ConverterFactoryException e) {
			String message = "Conversion formats not supported (" + fromFormat + " -> " + toFormat + ").";
			errors.reject("facility.invalid", message);
			model.put("errors", errors);
			logger.warn(message, e);
			return responseView;
		}
		
		convertThreadMap.put(convertThreadKey, new ConvertThread(converter));
		
		try {
			Thread.sleep(1000L);
		}
		catch(InterruptedException e) {
			// ignore //
		}
		
		checkConvertThread(scanId, fromFormat, toFormat, errors, model);
		return responseView;
	}
	
	protected void checkConvertThread(int scanId, String fromFormat, String toFormat, BindException errors, ModelMap model) {
		
		String convertThreadKey = buildConvertThreadKey(scanId, fromFormat, toFormat);
		ConvertThread convertThread = convertThreadMap.get(convertThreadKey);
		
		if(convertThread.isAlive()) {
			Map<String,Object> response = new HashMap<String,Object>();
			response.put("message", "Conversion in progress.");
			response.put("complete", false);
			model.put("response", response);
			return;
		}
		
		convertThreadMap.remove(convertThreadKey);
			
		if(convertThread.hasException()) {
			String message = "Conversion failed (" + fromFormat + " -> " + toFormat +").";
			Exception e = convertThread.getException();
			errors.reject("coverter.failed", message);
			model.put("errors", errors);
			logger.warn(message, e);		
			return;
		}
		
		Map<String,Object> response = new HashMap<String,Object>();
		response.put("message", "Conversion complete.");
		response.put("complete", true);
		model.put("response", response);
		return;
	}
	
	protected String buildConvertThreadKey(int scanId, String fromFormat, String toFormat) {
		return fromFormat + "_" + toFormat + "_" + scanId;
	}
	
	protected static class ConvertThread extends Thread {
		
		private Converter converter;
		private Exception exception;
		
		public ConvertThread(Converter converter) {
			this.converter = converter;
			this.exception = null;
			setDaemon(true);
			start();
		}

		@Override
		public void run() {
			try {
				converter.convert();
			}
			catch(Exception e) {
				exception = e;
			}
		}

		public boolean hasException() {
			return exception != null;
		}
		
		public Converter getConverter() {
			return converter;
		}

		public Exception getException() {
			return exception;
		}
	}

	public ScanDAO getScanDAO() {
		return scanDAO;
	}
	public void setScanDAO(ScanDAO scanDAO) {
		this.scanDAO = scanDAO;
	}

	public SampleDAO getSampleDAO() {
		return sampleDAO;
	}
	public void setSampleDAO(SampleDAO sampleDAO) {
		this.sampleDAO = sampleDAO;
	}

	public SessionDAO getSessionDAO() {
		return sessionDAO;
	}
	public void setSessionDAO(SessionDAO sessionDAO) {
		this.sessionDAO = sessionDAO;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}
	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public FacilityDAO getFacilityDAO() {
		return facilityDAO;
	}
	public void setFacilityDAO(FacilityDAO facilityDAO) {
		this.facilityDAO = facilityDAO;
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

	public ConverterFactory getConverterFactory() {
		return converterFactory;
	}
	public void setConverterFactory(ConverterFactory converterFactory) {
		this.converterFactory = converterFactory;
	}
}
