/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      CreateScanController class.	     
 */
package ca.sciencestudio.nanofab.service.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.dao.ScanDAO;
import ca.sciencestudio.model.session.dao.ExperimentDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.web.BindAndValidateUtils;

import ca.sciencestudio.nanofab.service.controllers.AbstractShareController;
import ca.sciencestudio.nanofab.state.NanofabSessionStateMap;

/**
 * @author maxweld
 *
 */
@Controller
public class CreateScanController extends AbstractShareController {
	
	private static final String DIRECTORY_PREFIX_DATA = "scan-";
	private static final String DIRECTORY_PREFIX_TEMP = "temp-";
	
	private File scanDataDirectory;
	
	private NanofabSessionStateMap nanofabSessionStateMap;
	
	private ScanDAO scanDAO;
	private ExperimentDAO experimentDAO;
	
	protected Log logger = LogFactory.getLog(getClass());
	
	@RequestMapping(value = "/scan/create.{format}", method = RequestMethod.POST)
	public String handleRequest(@RequestParam String scanName, @RequestParam String[] scanFiles, @PathVariable String format, ModelMap model) {
		
		BindException errors = BindAndValidateUtils.buildBindException();
		model.put("errors", errors);
		
		String responseView = "response-" + format;
		
		String personUid = nanofabSessionStateMap.getControllerUid();
		if(!SecurityUtil.getPerson().getUid().equals(personUid)) {
			errors.reject("permission.denied", "Must be controller to create scan.");
			return responseView;
		}
		
		if((scanName == null) || (scanName.length() == 0)) {
			errors.reject("scanName.invalid", "Please specify a scan name.");
			return responseView;
		}
		
		int experimentId = nanofabSessionStateMap.getExperimentId();
		Experiment experiment = experimentDAO.getExperimentById(experimentId);
		if(experiment == null) {
			errors.reject("experiment.notfound", "Please select an experiment.");
			return responseView;
		}
		
		File shareParent = getShareDirectory();
		Set<File> scanFileSet = new LinkedHashSet<File>();
		
		for(String scanFile : scanFiles) {
			if(logger.isDebugEnabled()) {
				logger.debug("Checking selected data file: " + scanFile);
			}
			
			File file = new File(shareParent, scanFile);
			if(file.isFile()) {
				scanFileSet.add(file);
			}
			else {
				logger.warn("Selected file does not exist or is not a regular file: " + scanFile);
				errors.reject("file.invalid", "A selected data file does not exist.");
				return responseView;
			}
		}
		
		if(scanFileSet.isEmpty()) {
			errors.reject("scanFileSet.empty", "Please select one or more data files.");
			return responseView;
		}
		
		File scanDataTempDirectory = null;
		
		Random random = new Random();
		for(int idx=0; idx<1000; idx++) {
			File temp = new File(scanDataDirectory, DIRECTORY_PREFIX_TEMP + random.nextInt(Integer.MAX_VALUE));
			if(!temp.exists()) {
				scanDataTempDirectory = temp;
				break;
			}
		} 
		
		if(scanDataTempDirectory == null) {
			errors.reject("", "Error: Cannot create scan directory. (1)");
			logger.warn("Could not find unique temporary directory."); 
			return responseView;
		}
		
		if(!scanDataTempDirectory.mkdirs()) {
			errors.reject("", "Error: Cannot create scan directory. (2)");
			logger.warn("Could not create unique temporary directory.");
			return responseView;
		}
		
		Map<File,File> scanDataFileMap = new LinkedHashMap<File, File>();
		for(File scanFile : scanFileSet) {
			File scanDataFile = getUniqueScanDataFile(scanDataTempDirectory, scanFile);
			if(scanDataFile == null) {
				// CLEAN UP //
				scanDataTempDirectory.delete();
				//////////////
				errors.reject("", "Error: Cannot find unique data file name.");
				logger.warn("Could not find unique file in temporary directory: " + scanFile.getAbsolutePath());
				return responseView;
			}
			if(logger.isDebugEnabled()) {
				logger.debug("Preparing to copy file: " + scanFile  + " to: " + scanDataFile);
			}
			scanDataFileMap.put(scanFile, scanDataFile);
		}
			
		for(Map.Entry<File, File> entry : scanDataFileMap.entrySet()) {
			try {
				InputStream inputStream = new FileInputStream(entry.getKey());
				OutputStream outputStream = new FileOutputStream(entry.getValue());
				IOUtils.copy(inputStream, outputStream);
			}
			catch(IOException e) {
				// CLEAN UP //
				for(File file : scanDataTempDirectory.listFiles()) {
					file.delete();
				}
				scanDataTempDirectory.delete();
				//////////////
				errors.reject("", "Error: Cannot copy data file to data directory.");
				logger.warn("Could not copy data file to temporary directory: " + entry.getKey() + " to: " + entry.getValue());
				return responseView;
			}
			if(logger.isDebugEnabled()) {
				logger.debug("Finished copying data file: " + entry.getKey() + " to: " + entry.getValue());
			}
		}
		
		Date now = new Date();
		Scan scan = scanDAO.createScan();
		scan.setEndDate(now);
		scan.setStartDate(now);
		scan.setName(scanName);
		scan.setExperimentId(experimentId);
		int scanId = scanDAO.addScan(scan);
		
		File scanDirectory = new File(scanDataDirectory, DIRECTORY_PREFIX_DATA + scanId);
		if(scanDataTempDirectory.renameTo(scanDirectory)) {
			scan.setDataUrl(scanDirectory.getAbsolutePath());
		} else {
			scan.setDataUrl(scanDataTempDirectory.getAbsolutePath());
			logger.warn("Could not rename temporary directory to scan data directory.");
		}
		scanDAO.editScan(scan);
		
		// DELETE ORIGINAL FILES?? //
			
		return responseView;
	}
		
	protected File getUniqueScanDataFile(File parent, File file) {
		
		String name = file.getName();
		File scanDataFile = new File(parent, name);
		
		if(!scanDataFile.exists()) {	
			return scanDataFile;
		}
		
		String[] split = name.split("\\.", 2);
		
		String format;
		if(split.length == 1) {
			format = String.format("%s_%%d", split[0]);
		} else {
			format = String.format("%s_%%d.%s", split[0], split[1]);
		}
		
		for(int idx=1; idx<1000; idx++) {
			name = String.format(format, idx);
			scanDataFile = new File(parent, name);
			if(!scanDataFile.exists()) {
				return scanDataFile;
			}
		}
		return null;
	}

	public File getScanDataDirectory() {
		return scanDataDirectory;
	}
	public void setScanDataDirectory(File scanDataDirectory) {
		this.scanDataDirectory = scanDataDirectory;
	}

	public NanofabSessionStateMap getNanofabSessionStateMap() {
		return nanofabSessionStateMap;
	}
	public void setNanofabSessionStateMap(
			NanofabSessionStateMap nanofabSessionStateMap) {
		this.nanofabSessionStateMap = nanofabSessionStateMap;
	}

	public ScanDAO getScanDAO() {
		return scanDAO;
	}
	public void setScanDAO(ScanDAO scanDAO) {
		this.scanDAO = scanDAO;
	}

	public ExperimentDAO getExperimentDAO() {
		return experimentDAO;
	}
	public void setExperimentDAO(ExperimentDAO experimentDAO) {
		this.experimentDAO = experimentDAO;
	}
}
