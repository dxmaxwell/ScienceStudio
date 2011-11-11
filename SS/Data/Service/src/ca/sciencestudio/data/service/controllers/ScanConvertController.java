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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
import ca.sciencestudio.model.facility.dao.FacilityAuthzDAO;
import ca.sciencestudio.model.facility.dao.InstrumentAuthzDAO;
import ca.sciencestudio.model.facility.dao.InstrumentTechniqueAuthzDAO;
import ca.sciencestudio.model.facility.dao.LaboratoryAuthzDAO;
import ca.sciencestudio.model.facility.dao.TechniqueAuthzDAO;
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectAuthzDAO;
import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.sample.dao.SampleAuthzDAO;
import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.ExperimentAuthzDAO;
import ca.sciencestudio.model.session.dao.ScanAuthzDAO;
import ca.sciencestudio.model.session.dao.SessionAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.web.FormResponseMap;

/**
 * @author maxweld
 *
 */
@Controller
public class ScanConvertController implements StdConverter {

	private ScanAuthzDAO scanAuthzDAO;
	
	private SampleAuthzDAO sampleAuthzDAO;
	
	private SessionAuthzDAO sessionAuthzDAO;
	
	private ProjectAuthzDAO projectAuthzDAO;
	
	private FacilityAuthzDAO facilityAuthzDAO;
	
	private ExperimentAuthzDAO experimentAuthzDAO;
	
	private LaboratoryAuthzDAO laboratoryAuthzDAO;
	
	private TechniqueAuthzDAO techniqueAuthzDAO;
	
	private InstrumentAuthzDAO instrumentAuthzDAO;
	
	private InstrumentTechniqueAuthzDAO instrumentTechniqueAuthzDAO;
	
	private ConverterFactory converterFactory;
	
	private Map<String,ConvertThread> convertThreadMap = Collections.synchronizedMap(new HashMap<String,ConvertThread>());
	
	protected Log logger = LogFactory.getLog(getClass());
	
	@ResponseBody
	@RequestMapping(value = "/scan/{scanGid}/convert/{fromFormat}/{toFormat}*", method = RequestMethod.GET)
	public FormResponseMap convert(@PathVariable String scanGid, @PathVariable String fromFormat, @PathVariable String toFormat) {

		String convertThreadKey = buildConvertThreadKey(scanGid, fromFormat, toFormat);
		if(convertThreadMap.containsKey(convertThreadKey)) {
			return checkConvertThread(scanGid, fromFormat, toFormat);
		}
		
		String user = SecurityUtil.getPersonGid();
		
		Scan scan = scanAuthzDAO.get(user, scanGid).get();
		if(scan == null) {
			return new FormResponseMap(false, "Scan not found.");
		}
		
		Date scanStartDate = scan.getStartDate();
		Date scanEndDate = scan.getEndDate();
		if((scanStartDate == null) || (scanEndDate == null)) {
			FormResponseMap response = new FormResponseMap(false, "Scan is incomplete");
			response.put("complete", false);
			return response;
		}
		
		Experiment experiment = experimentAuthzDAO.get(user, scan.getExperimentGid()).get();
		if(experiment == null) {
			return new FormResponseMap(false, "Experiment not found.");
		}
		
		Sample sample = sampleAuthzDAO.get(user, experiment.getSourceGid()).get();
		if(sample == null) {
			return new FormResponseMap(false, "Sample not found.");
		}
		
		Session session = sessionAuthzDAO.get(user, experiment.getSessionGid()).get();
		if(session == null) {
			return new FormResponseMap(false, "Session not found.");
		}
		
		InstrumentTechnique it = instrumentTechniqueAuthzDAO.get(user, experiment.getInstrumentTechniqueGid()).get();
		if(it == null) {
			return new FormResponseMap(false, "Instrument technique not found.");
		}
		
		Technique technique = techniqueAuthzDAO.get(it.getTechniqueGid()).get();
		if(technique == null) {
			return new FormResponseMap(false, "Technique not found.");
		}
		
		Instrument instrument = instrumentAuthzDAO.get(user, it.getInstrumentGid()).get();
		if(instrument == null) {
			return new FormResponseMap(false, "Instrument not found.");
		}
		
		Laboratory laboratory = laboratoryAuthzDAO.get(session.getLaboratoryGid()).get();
		if(laboratory == null) {
			return new FormResponseMap(false, "Laboratory not found.");
		}
		
		Facility facility = facilityAuthzDAO.get(laboratory.getFacilityGid()).get();
		if(facility == null) {
			return new FormResponseMap(false, "Facility not found.");
		}
		
		Project project = projectAuthzDAO.get(user, session.getProjectGid()).get();
		if(project == null) {
			return new FormResponseMap(false, "Project not found.");
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
			FormResponseMap response = new FormResponseMap(false, message);
			logger.warn(message, e);
			return response;
		}
		
		convertThreadMap.put(convertThreadKey, new ConvertThread(converter));
		
		try {
			Thread.sleep(1000L);
		}
		catch(InterruptedException e) {
			// ignore //
		}
		
		return checkConvertThread(scanGid, fromFormat, toFormat);
	}
	
	protected FormResponseMap checkConvertThread(String scanGid, String fromFormat, String toFormat) {
		
		String convertThreadKey = buildConvertThreadKey(scanGid, fromFormat, toFormat);
		ConvertThread convertThread = convertThreadMap.get(convertThreadKey);
		
		if(convertThread.isAlive()) {
			FormResponseMap response = new FormResponseMap(true, "Conversion in progress.");
			response.put("complete", false);
			return response;
		}
		
		convertThreadMap.remove(convertThreadKey);
			
		if(convertThread.hasException()) {
			String message = "Conversion failed (" + fromFormat + " -> " + toFormat +").";
			FormResponseMap response = new FormResponseMap(false, message);
			logger.warn(message, convertThread.getException());
			return response;
		}
		
		FormResponseMap response = new FormResponseMap(true, "Conversion complete.");
		response.put("complete", true);
		return response;
	}
	
	protected String buildConvertThreadKey(String scanGid, String fromFormat, String toFormat) {
		return fromFormat + "_" + toFormat + "_" + scanGid;
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

	public ScanAuthzDAO getScanAuthzDAO() {
		return scanAuthzDAO;
	}
	public void setScanAuthzDAO(ScanAuthzDAO scanAuthzDAO) {
		this.scanAuthzDAO = scanAuthzDAO;
	}

	public SampleAuthzDAO getSampleAuthzDAO() {
		return sampleAuthzDAO;
	}
	public void setSampleAuthzDAO(SampleAuthzDAO sampleAuthzDAO) {
		this.sampleAuthzDAO = sampleAuthzDAO;
	}

	public SessionAuthzDAO getSessionAuthzDAO() {
		return sessionAuthzDAO;
	}
	public void setSessionAuthzDAO(SessionAuthzDAO sessionAuthzDAO) {
		this.sessionAuthzDAO = sessionAuthzDAO;
	}

	public ProjectAuthzDAO getProjectAuthzDAO() {
		return projectAuthzDAO;
	}
	public void setProjectAuthzDAO(ProjectAuthzDAO projectAuthzDAO) {
		this.projectAuthzDAO = projectAuthzDAO;
	}

	public FacilityAuthzDAO getFacilityAuthzDAO() {
		return facilityAuthzDAO;
	}
	public void setFacilityAuthzDAO(FacilityAuthzDAO facilityAuthzDAO) {
		this.facilityAuthzDAO = facilityAuthzDAO;
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

	public InstrumentTechniqueAuthzDAO getInstrumentTechniqueAuthzDAO() {
		return instrumentTechniqueAuthzDAO;
	}
	public void setInstrumentTechniqueAuthzDAO(InstrumentTechniqueAuthzDAO instrumentTechniqueAuthzDAO) {
		this.instrumentTechniqueAuthzDAO = instrumentTechniqueAuthzDAO;
	}

	public ConverterFactory getConverterFactory() {
		return converterFactory;
	}
	public void setConverterFactory(ConverterFactory converterFactory) {
		this.converterFactory = converterFactory;
	}
}
