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
		
	private static final String MODEL_KEY_PROJECT_NAME = "projectName";
	private static final String MODEL_KEY_SESSION_NAME = "sessionName";
	private static final String MODEL_KEY_EXPERIMENT_NAME = "experimentName";
	private static final String MODEL_KEY_SAMPLE_NAME = "sampleName";
	private static final String MODEL_KEY_SCAN_NAME = "scanName";
	private static final String MODEL_KEY_ERROR_MESSAGE = "errorMessage";
	
	private static final String TEMP_FILE_NAME_PREFIX = "ssTempFile";
	private static final String DATA_FILE_NAME_SUFFIX_REGEX = "\\.[\\d\\w]*$";
	
	private static final String FORMAT_CDF = "CDF";
	private static final String FORMAT_CDFML = "CDFML";
	
	private static final String FORM_VIEW = "convertXRFDAFtoCDFML";
	
	private String facilityName = DEFAULT_FACILITY_NAME;
	private String facilityLongName = DEFAULT_FACILITY_LONG_NAME;
	
	private String laboratoryName = DEFAULT_LABORATORY_NAME;
	private String laboratoryLongName = DEFAULT_LABORATORY_LONG_NAME;
	
	private String instrumentName = DEFAULT_INSTRUMENT_NAME;
	private String techniqueName = DEFAULT_TECHNIQUE_NAME;
		
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
		
		File dataTempFile;
		try {
			dataTempFile = File.createTempFile(TEMP_FILE_NAME_PREFIX, FILE_NAME_SUFFIX_DAF_DATA);
		}
		catch(IOException e) {
			model.put(MODEL_KEY_ERROR_MESSAGE, "A system error has occurred. (001)");
			log.warn(model.get(MODEL_KEY_ERROR_MESSAGE));
			return FORM_VIEW;
		}
		
		String dataUrl = dataTempFile.getParentFile().getAbsolutePath();
		String dataFileBase = dataTempFile.getName().replaceAll(DATA_FILE_NAME_SUFFIX_REGEX, "");
		File specTempFile = new File(dataUrl, dataFileBase + FILE_NAME_SUFFIX_DAF_SPEC);
		
		ConverterMap converterRequestMap = new LinkedHashConverterMap();
		
		converterRequestMap.put(REQUEST_KEY_FACILITY_NAME, facilityName);
		converterRequestMap.put(REQUEST_KEY_FACILITY_LONG_NAME, facilityLongName);
		
		converterRequestMap.put(REQUEST_KEY_LABORATORY_NAME, laboratoryName);
		converterRequestMap.put(REQUEST_KEY_LABORATORY_LONG_NAME, laboratoryLongName);
		
		converterRequestMap.put(REQUEST_KEY_INSTRUMENT_NAME, instrumentName);
		converterRequestMap.put(REQUEST_KEY_TECHNIQUE_NAME, techniqueName);
		
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
			dafDataFile.transferTo(dataTempFile);
		}
		catch(IOException e) {
			dataTempFile.delete();
			specTempFile.delete();
			model.put(MODEL_KEY_ERROR_MESSAGE, "A system error has occurred. (105)");
			log.warn(model.get(MODEL_KEY_ERROR_MESSAGE), e);
			return FORM_VIEW;
		}
		
		try {
			dafSpecFile.transferTo(specTempFile);
		}
		catch(IOException e) {
			dataTempFile.delete();
			specTempFile.delete();
			model.put(MODEL_KEY_ERROR_MESSAGE, "A system error has occurred. (106)");
			log.warn(model.get(MODEL_KEY_ERROR_MESSAGE), e);
			return FORM_VIEW;
		}
		
		// Convert UNKNOWN to CDF //
		try {
			long start = System.currentTimeMillis();
			converterRequestMap.setToFormat(FORMAT_CDF);
			converterFactory.getConverter(converterRequestMap).convert();
			log.info("Complete UNKNOWN to CDF conversion: " + dafDataFile.getName() + ": " + (System.currentTimeMillis()-start)/1000.0 + " sec.");
		}
		catch(ConverterException e) {
			dataTempFile.delete();
			specTempFile.delete();
			model.put(MODEL_KEY_ERROR_MESSAGE, "Error while converting XRF data to CDF.<br/>Please ensure that valid data and spectra files have been selected.");
			log.warn(model.get(MODEL_KEY_ERROR_MESSAGE), e);
			return FORM_VIEW;
		}
		
		File cdfTempFile = new File(dataUrl, dataFileBase + FILE_NAME_SUFFIX_CDF);
		if(cdfTempFile.length() == 0L) {
			dataTempFile.delete();
			specTempFile.delete();
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
			dataTempFile.delete();
			specTempFile.delete();
			cdfTempFile.delete();
			model.put(MODEL_KEY_ERROR_MESSAGE, "Error while converting XRF data to CDFML.<br/>Please ensure that valid data and spectra files have been selected.");
			log.warn(model.get(MODEL_KEY_ERROR_MESSAGE), e);
			return FORM_VIEW;
		}
		
		File cdfmlTempFile = new File(dataUrl, dataFileBase + FILE_NAME_SUFFIX_CDFML);
		long cdfmlFileLength = cdfmlTempFile.length();
		if(cdfmlFileLength == 0L) {
			dataTempFile.delete();
			specTempFile.delete();
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
			dataTempFile.delete();
			specTempFile.delete();
			cdfTempFile.delete();
			cdfmlTempFile.delete();
			model.put(MODEL_KEY_ERROR_MESSAGE, "A system error has occurred. (203)");
			log.warn(model.get(MODEL_KEY_ERROR_MESSAGE));
			return FORM_VIEW;
		}
		
		String cdfmlFileName = dafDataFile.getOriginalFilename();
		if(cdfmlFileName.endsWith(FILE_NAME_SUFFIX_DAF_DATA)) {
			cdfmlFileName = cdfmlFileName.replaceAll(DATA_FILE_NAME_SUFFIX_REGEX, FILE_NAME_SUFFIX_CDFML);
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
		dataTempFile.delete();
		specTempFile.delete();
		cdfTempFile.delete();
		cdfmlTempFile.delete();
		return null;
	}
	
	public String getFacilityName() {
		return facilityName;
	}
	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

	public String getFacilityLongName() {
		return facilityLongName;
	}
	public void setFacilityLongName(String facilityLongName) {
		this.facilityLongName = facilityLongName;
	}

	public String getLaboratoryName() {
		return laboratoryName;
	}
	public void setLaboratoryName(String laboratoryName) {
		this.laboratoryName = laboratoryName;
	}

	public String getLaboratoryLongName() {
		return laboratoryLongName;
	}
	public void setLaboratoryLongName(String laboratoryLongName) {
		this.laboratoryLongName = laboratoryLongName;
	}

	public String getInstrumentName() {
		return instrumentName;
	}
	public void setInstrumentName(String instrumentName) {
		this.instrumentName = instrumentName;
	}

	public String getTechniqueName() {
		return techniqueName;
	}
	public void setTechniqueName(String techniqueName) {
		this.techniqueName = techniqueName;
	}

	public ConverterFactory getConverterFactory() {
		return converterFactory;
	}
	public void setConverterFactory(ConverterFactory converterFactory) {
		this.converterFactory = converterFactory;
	}
}
