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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.dao.ExperimentAuthzDAO;
import ca.sciencestudio.model.session.dao.ScanAuthzDAO;
import ca.sciencestudio.model.utilities.GID;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.rest.ValidationResult;
import ca.sciencestudio.util.web.FormResponseMap;

import ca.sciencestudio.nanofab.service.controllers.AbstractShareController;

/**
 * @author maxweld
 *
 */
@Controller
public class CreateScanController extends AbstractShareController {
	
	private static final String DIRECTORY_PREFIX_DATA = "scan-";
	private static final String DIRECTORY_PREFIX_TEMP = "temp-";
	
	private File scanDataDirectory;
	
	private ScanAuthzDAO scanAuthzDAO;
	private ExperimentAuthzDAO experimentAuthzDAO;
	
	protected Log logger = LogFactory.getLog(getClass());
	
	@ResponseBody
	@RequestMapping(value = "/scan/create*", method = RequestMethod.POST)
	public FormResponseMap handleRequest(@RequestParam String scanName, @RequestParam String[] scanFiles) {
		
		if(!canWriteLaboratory()) {
			return new FormResponseMap(false, "Must be controller to create scan.");
		}
		
		if((scanName == null) || (scanName.length() == 0)) {
			return new FormResponseMap(false, "Please specify a scan name.");
		}
		
		String user = SecurityUtil.getPersonGid();
		String experimentGid = nanofabSessionStateMap.getExperimentGid();
		Experiment experiment = experimentAuthzDAO.get(user, experimentGid).get();
		if(experiment == null) {
			return new FormResponseMap(false, "Please select an experiment.");
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
				return new FormResponseMap(false, "A selected data file does not exist.");
			}
		}
		
		if(scanFileSet.isEmpty()) {
			return new FormResponseMap(false, "Please select one or more data files.");
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
			logger.warn("Could not find unique temporary directory.");
			return new FormResponseMap(false, "Could not find unique temporary directory.");
		}
		
		if(!scanDataTempDirectory.mkdirs()) {
			logger.warn("Could not create unique temporary directory.");
			return new FormResponseMap(false, "Error: Cannot create scan directory.");
		}
		
		Map<File,File> scanDataFileMap = new LinkedHashMap<File, File>();
		for(File scanFile : scanFileSet) {
			File scanDataFile = getUniqueScanDataFile(scanDataTempDirectory, scanFile);
			if(scanDataFile == null) {
				// CLEAN UP //
				scanDataTempDirectory.delete();
				//////////////
				logger.warn("Could not find unique file in temporary directory: " + scanFile.getAbsolutePath());
				return new FormResponseMap(false, "Error: Cannot find unique data file name.");
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
				logger.warn("Could not copy data file to temporary directory: " + entry.getKey() + " to: " + entry.getValue());
				return new FormResponseMap(false, "Error: Cannot copy data file to data directory.");
			}
			if(logger.isDebugEnabled()) {
				logger.debug("Finished copying data file: " + entry.getKey() + " to: " + entry.getValue());
			}
		}
		
		Date now = new Date();
		Scan scan = new Scan();
		scan.setName(scanName);
		scan.setDataUrl(scanDataTempDirectory.getAbsolutePath());
		scan.setExperimentGid(experimentGid);
		scan.setStartDate(now);
		scan.setEndDate(new Date(now.getTime()+60000L));
		
		ValidationResult result = scanAuthzDAO.add(user, scan).get();
		if(result.hasErrors()) {
			logger.warn("Could not add Scan to experiment: " + experimentGid);
			return new FormResponseMap(false, "Error: Cannot create new Scan. (1)");
		}
		
		GID gid = GID.parse(scan.getGid());
		if(gid == null) {
			logger.warn("Could not parse Scan GID: " + scan.getGid());
			return new FormResponseMap(false, "Error: Connot create new Scan. (2)");
		}
		
		File scanDirectory = new File(scanDataDirectory, DIRECTORY_PREFIX_DATA + gid.getId());
		if(scanDataTempDirectory.renameTo(scanDirectory)) {
			scan.setDataUrl(scanDirectory.getAbsolutePath());
			result = scanAuthzDAO.edit(user, scan).get();
			if(result.hasErrors()) {
				logger.warn("Could not edit Scan with GID: " + scan.getGid());
			}
		} else {
			logger.warn("Could not rename temporary directory to scan data directory.");
		}
		
		// DELETE ORIGINAL FILES?? //
			
		return new FormResponseMap(true);
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

	public ScanAuthzDAO getScanAuthzDAO() {
		return scanAuthzDAO;
	}
	public void setScanAuthzDAO(ScanAuthzDAO scanAuthzDAO) {
		this.scanAuthzDAO = scanAuthzDAO;
	}

	public ExperimentAuthzDAO getExperimentAuthzDAO() {
		return experimentAuthzDAO;
	}
	public void setExperimentAuthzDAO(ExperimentAuthzDAO experimentAuthzDAO) {
		this.experimentAuthzDAO = experimentAuthzDAO;
	}
}
