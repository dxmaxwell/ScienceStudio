/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ConvertXRFDAFtoCDFMLController class.
 *     
 */
package ca.sciencestudio.data.service.vespers.controllers.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import ca.sciencestudio.model.facility.Facility;
import ca.sciencestudio.model.facility.Instrument;
import ca.sciencestudio.model.facility.Laboratory;
import ca.sciencestudio.model.facility.Technique;
import ca.sciencestudio.model.facility.dao.FacilityAuthzDAO;
import ca.sciencestudio.model.facility.dao.InstrumentAuthzDAO;
import ca.sciencestudio.model.facility.dao.LaboratoryAuthzDAO;
import ca.sciencestudio.model.facility.dao.TechniqueAuthzDAO;

import ca.sciencestudio.data.converter.ConverterMap;
import ca.sciencestudio.data.converter.LinkedHashConverterMap;
import ca.sciencestudio.data.converter.factory.ConverterFactory;
import ca.sciencestudio.data.standard.StdConverter;
import ca.sciencestudio.data.standard.StdScanParams;
import ca.sciencestudio.data.support.ConverterException;

import ca.sciencestudio.util.Parameters;


/**
 * @author maxweld
 *
 */
@Controller
public class ConvertXRFDAFtoCDFMLController implements StdConverter, StdScanParams {
		
	static private final String MODEL_KEY_PROJECT_NAME = "projectName";
	static private final String MODEL_KEY_SESSION_NAME = "sessionName";
	static private final String MODEL_KEY_EXPERIMENT_NAME = "experimentName";
	static private final String MODEL_KEY_SAMPLE_NAME = "sampleName";
	static private final String MODEL_KEY_SCAN_NAME = "scanName";
	static private final String MODEL_KEY_ERROR_MESSAGE = "errorMessage";
	
	static private final String DEFAULT_PROJECT_NAME = "Unspecified Project";
	static private final String DEFAULT_SESSION_NAME = "Unspecified Session";
	static private final String DEFAULT_EXPERIMENT_NAME = "Unspecified Experiment";
	static private final String DEFAULT_SAMPLE_NAME = "Unspecified Sample";
	static private final String DEFAULT_SCAN_NAME = "Unspecified Scan";
	
	static private final String FILE_NAME_PREFIX_TEMP = "dafDataFile";
	public static final String FILE_NAME_SUFFIX_REGEX_DAF_DATA = Pattern.quote(FILE_NAME_SUFFIX_DAF_DATA) + "\\Z";
	
	private static final String FORMAT_DAF = "DAF";
	private static final String FORMAT_CDF = "CDF";
	private static final String FORMAT_CDFML = "CDFML";
	
	static private final String FORM_VIEW = "convertXRFDAFtoCDFML";
	
	private FacilityAuthzDAO facilityAuthzDAO;
	
	private InstrumentAuthzDAO instrumentAuthzDAO;
	
	private LaboratoryAuthzDAO laboratoryAuthzDAO;
	
	private TechniqueAuthzDAO techniqueAuthzDAO;
	
	private ConverterFactory converterFactory;
	
	protected Log log = LogFactory.getLog(getClass());

	@RequestMapping(value = "/util/convertXRF.html", method = RequestMethod.GET)
	public String getFormView() {
		return FORM_VIEW;
	}

	@RequestMapping(value = "/util/convertXRF.html", method = RequestMethod.POST)
	public String postFormData(@RequestParam(required = false) String projectName, @RequestParam(required = false) String sessionName,
									@RequestParam(required = false) String experimentName, @RequestParam(required = false) String sampleName,
										@RequestParam(required = false) String scanName, @RequestParam(required = false) MultipartFile dafDataFile,
											@RequestParam(required = false) MultipartFile dafSpecFile, HttpServletResponse response, ModelMap model) {
		
		if((projectName == null) || (projectName.length() == 0)) {
			projectName = DEFAULT_PROJECT_NAME;
		} else {
			model.put(MODEL_KEY_PROJECT_NAME, projectName);
		}
		
		if((sessionName == null) || (sessionName.length() == 0)) {
			sessionName = DEFAULT_SESSION_NAME;
		} else {
			model.put(MODEL_KEY_SESSION_NAME, sessionName);
		}
		
		if((experimentName == null) || (experimentName.length() == 0)) {
			experimentName = DEFAULT_EXPERIMENT_NAME;
		} else {
			model.put(MODEL_KEY_EXPERIMENT_NAME, experimentName);
		}
		
		if((sampleName == null) || (sampleName.length() == 0)) {
			sampleName = DEFAULT_SAMPLE_NAME;
		} else {
			model.put(MODEL_KEY_SAMPLE_NAME, sampleName);
		}
		
		if((scanName == null) || (scanName.length() == 0)) {
			scanName = DEFAULT_SCAN_NAME;
		} else {
			model.put(MODEL_KEY_SCAN_NAME, scanName);
		}
		
		if((dafDataFile == null) || (dafDataFile.getSize() == 0L)) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "A valid XRF data file (*.dat) is required.");
			return FORM_VIEW;
		}
		
		if((dafSpecFile == null) || (dafSpecFile.getSize() == 0L)) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "A valid XRF spectra file (*_spectra.dat) is required.");
			return FORM_VIEW;
		}
		
		File dafDataTempFile;
		try {
			dafDataTempFile = File.createTempFile(FILE_NAME_PREFIX_TEMP, FILE_NAME_SUFFIX_DAF_DATA);
		}
		catch(IOException e) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "A system error has occurred. (001)");
			log.warn(model.get(MODEL_KEY_ERROR_MESSAGE));
			return FORM_VIEW;
		}
		
		String dataUrl = dafDataTempFile.getParentFile().getAbsolutePath();
		String dataFileBase = dafDataTempFile.getName().replaceAll(FILE_NAME_SUFFIX_REGEX_DAF_DATA, "");
		File dafSpecTempFile = new File(dataUrl, dataFileBase + FILE_NAME_SUFFIX_DAF_SPEC);
		
		ConverterMap converterRequestMap = new LinkedHashConverterMap();
		
		Facility facility = facilityAuthzDAO.get("CLS").get();
		if(facility == null) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "A system error has occurred. (101)");
			log.warn(model.get(MODEL_KEY_ERROR_MESSAGE));
			return FORM_VIEW;
		}
		converterRequestMap.put(REQUEST_KEY_FACILITY, facility);
		
		List<Laboratory> laboratories = laboratoryAuthzDAO.getAllByFacilityGid(facility.getGid()).get();
		
		Laboratory laboratory = null;
		for(Laboratory l : laboratories) {
			if(l.getName().equals("VESPERS")) {
				laboratory = l;
				break;
			}
		}
		if(laboratory == null) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "A system error has occurred. (102)");
			log.warn(model.get(MODEL_KEY_ERROR_MESSAGE));
			return FORM_VIEW;
		}
		converterRequestMap.put(REQUEST_KEY_LABORATORY, laboratory);
		
		List<Instrument> instruments = instrumentAuthzDAO.getAllByLaboratoryGid(laboratory.getGid()).get();
			
		Instrument instrument = null;
		for(Instrument i : instruments) {
			if(i.getName().equals("Microprobe")) {
				instrument = i;
				break;
			}
		}
		if(instrument == null) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "A system error has occurred. (103)");
			log.warn(model.get(MODEL_KEY_ERROR_MESSAGE));
			return FORM_VIEW;
		}
		converterRequestMap.put(REQUEST_KEY_INSTRUMENT, instrument);
		
		List<Technique> techniques = techniqueAuthzDAO.getAllByInstrumentGid(instrument.getGid()).get();
		
		Technique technique = null;
		for(Technique t : techniques) {
			if(t.getName().equals("XRF")) {
				technique = t;
				break;
			}
		}
		if(technique == null) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "A system error has occurred. (104)");
			log.warn(model.get(MODEL_KEY_ERROR_MESSAGE));
			return FORM_VIEW;
		}
		converterRequestMap.put(REQUEST_KEY_TECHNIQUE, technique);
		converterRequestMap.put(REQUEST_KEY_PROJECT_NAME, projectName);
		converterRequestMap.put(REQUEST_KEY_SESSION_NAME, sessionName);
		converterRequestMap.put(REQUEST_KEY_EXPERIMENT_NAME, experimentName);
		converterRequestMap.put(REQUEST_KEY_SAMPLE_NAME, sampleName);
		converterRequestMap.put(REQUEST_KEY_SCAN_NAME, scanName);
		
		// ???????????
		//Date epoch = new Date(0L);
		//scan.setStartTime(epoch);
		//scan.setEndTime(epoch);
		
		converterRequestMap.put(REQUEST_KEY_SCAN_DATA_URL, dataUrl);
		
		Parameters parameters = new Parameters();
		parameters.put(StdScanParams.PARAM_KEY_DATA_FILE_BASE, dataFileBase);
		converterRequestMap.put(REQUEST_KEY_SCAN_PARAMS, parameters);
		
		try {
			dafDataFile.transferTo(dafDataTempFile);
		}
		catch(IOException e) {
			dafDataTempFile.delete();
			dafSpecTempFile.delete();
			model.put(MODEL_KEY_ERROR_MESSAGE, "A system error has occurred. (105)");
			log.warn(model.get(MODEL_KEY_ERROR_MESSAGE), e);
			return FORM_VIEW;
		}
		
		try {
			dafSpecFile.transferTo(dafSpecTempFile);
		}
		catch(IOException e) {
			dafDataTempFile.delete();
			dafSpecTempFile.delete();
			model.put(MODEL_KEY_ERROR_MESSAGE, "A system error has occurred. (106)");
			log.warn(model.get(MODEL_KEY_ERROR_MESSAGE), e);
			return FORM_VIEW;
		}
		
		// Convert DAF to CDF //
		try {
			long start = System.currentTimeMillis();
			converterRequestMap.setToFormat(FORMAT_CDF);
			converterRequestMap.setFromFormat(FORMAT_DAF);
			converterFactory.getConverter(converterRequestMap).convert();
			log.info("Complete DAF to CDF conversion: " + dafDataFile.getName() + ": " + (System.currentTimeMillis()-start)/1000.0 + " sec.");
		}
		catch(ConverterException e) {
			dafDataTempFile.delete();
			dafSpecTempFile.delete();
			model.put(MODEL_KEY_ERROR_MESSAGE, "Error while converting XRF data to CDF.<br/>Please ensure that valid data and spectra files have been selected.");
			log.warn(model.get(MODEL_KEY_ERROR_MESSAGE), e);
			return FORM_VIEW;
		}
		
		File cdfTempFile = new File(dataUrl, dataFileBase + FILE_NAME_SUFFIX_CDF);
		if(cdfTempFile.length() == 0L) {
			dafDataTempFile.delete();
			dafSpecTempFile.delete();
			cdfTempFile.delete();
			model.put(MODEL_KEY_ERROR_MESSAGE, "A system error has occurred. (201)");
			log.warn(model.get(MODEL_KEY_ERROR_MESSAGE));
			return FORM_VIEW;
		}
		
		// Convert CDF to CDFML //
		try {
			long start = System.currentTimeMillis();
			converterRequestMap.setToFormat(FORMAT_CDFML);
			converterRequestMap.setFromFormat(FORMAT_CDF);
			converterFactory.getConverter(converterRequestMap).convert();
			log.info("Complete CDF to CDFML conversion: " + cdfTempFile.getName() + ": " + (System.currentTimeMillis()-start)/1000.0 + " sec.");
		}
		catch(ConverterException e) {
			dafDataTempFile.delete();
			dafSpecTempFile.delete();
			cdfTempFile.delete();
			model.put(MODEL_KEY_ERROR_MESSAGE, "Error while converting XRF data to CDFML.<br/>Please ensure that valid data and spectra files have been selected.");
			log.warn(model.get(MODEL_KEY_ERROR_MESSAGE), e);
			return FORM_VIEW;
		}
		
		File cdfmlTempFile = new File(dataUrl, dataFileBase + FILE_NAME_SUFFIX_CDFML);
		long cdfmlFileLength = cdfmlTempFile.length();
		if(cdfmlFileLength == 0L) {
			dafDataTempFile.delete();
			dafSpecTempFile.delete();
			cdfTempFile.delete();
			cdfmlTempFile.delete();
			model.put(MODEL_KEY_ERROR_MESSAGE, "A system error has occurred. (202)");
			log.warn(model.get(MODEL_KEY_ERROR_MESSAGE));
			return FORM_VIEW;
		}
			
		// Copy CDFML File to Response //
		InputStream cdfmlInputSteam;
		OutputStream responseOutputStream;
		try {
			cdfmlInputSteam = new FileInputStream(cdfmlTempFile);
			responseOutputStream = response.getOutputStream();
		}
		catch(IOException e) {
			dafDataTempFile.delete();
			dafSpecTempFile.delete();
			cdfTempFile.delete();
			cdfmlTempFile.delete();
			model.put(MODEL_KEY_ERROR_MESSAGE, "A system error has occurred. (203)");
			log.warn(model.get(MODEL_KEY_ERROR_MESSAGE));
			return FORM_VIEW;
		}
		
		String cdfmlFileName = dafDataFile.getOriginalFilename();
		if(cdfmlFileName.endsWith(FILE_NAME_SUFFIX_DAF_DATA)) {
			cdfmlFileName = cdfmlFileName.replaceAll(FILE_NAME_SUFFIX_REGEX_DAF_DATA, FILE_NAME_SUFFIX_CDFML);
		} else {
			cdfmlFileName = cdfmlFileName + FILE_NAME_SUFFIX_CDFML;
		}
		
		response.setContentType("application/cdfml+xml");
		response.setContentLength((int)cdfmlFileLength);
		response.addHeader("Content-Disposition", "attachment;filename=" + cdfmlFileName);
		response.setStatus(HttpServletResponse.SC_OK);
		
		try {	
			IOUtils.copy(cdfmlInputSteam, responseOutputStream);
		}
		catch(IOException e) {
			log.warn("A system error has occurred. Likely the user cancelled the download. (204)");
		}
		
		// cleanup temp files //
		dafDataTempFile.delete();
		dafSpecTempFile.delete();
		cdfTempFile.delete();
		cdfmlTempFile.delete();
		return null;
	}
	
	
	public FacilityAuthzDAO getFacilityAuthzDAO() {
		return facilityAuthzDAO;
	}
	public void setFacilityAuthzDAO(FacilityAuthzDAO facilityAuthzDAO) {
		this.facilityAuthzDAO = facilityAuthzDAO;
	}

	public InstrumentAuthzDAO getInstrumentAuthzDAO() {
		return instrumentAuthzDAO;
	}
	public void setInstrumentAuthzDAO(InstrumentAuthzDAO instrumentAuthzDAO) {
		this.instrumentAuthzDAO = instrumentAuthzDAO;
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

	public ConverterFactory getConverterFactory() {
		return converterFactory;
	}
	public void setConverterFactory(ConverterFactory converterFactory) {
		this.converterFactory = converterFactory;
	}
}
