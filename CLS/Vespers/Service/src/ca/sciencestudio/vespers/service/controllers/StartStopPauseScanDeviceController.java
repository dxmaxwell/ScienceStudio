/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		StartStopPauseScanDeviceController class.
 *
 */
package ca.sciencestudio.vespers.service.controllers;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.dao.ScanAuthzDAO;
import ca.sciencestudio.model.utilities.GID;
import ca.sciencestudio.util.rest.AddResult;
import ca.sciencestudio.util.state.StateMap;
import ca.sciencestudio.util.web.FormResponseMap;
import ca.sciencestudio.security.util.SecurityUtil;

/**
 * @author medrand
 *
 */
@Controller
public class StartStopPauseScanDeviceController extends AbstractBeamlineAuthzController {
	
	private static final String VALUE_KEY_SCAN_ID = "scanId";
	private static final String VALUE_KEY_SCAN_GID = "scanGid";
	private static final String VALUE_KEY_SCAN_NAME = "scanName";
	private static final String VALUE_KEY_EXPERIMENT_GID = "experimentGid";
	
	private static final String DEFAULT_EXPERIMENT_GID = "0";
	
	private static final String ACTION_VALUE_START = "start";
	private static final String ACTION_VALUE_STOP = "stop";
	private static final String ACTION_VALUE_PAUSE = "pause";

	private static final String VALUE_KEY_TECHNIQUE = "technique"; 
	private static final String VALUE_KEY_MODE = "xrdMode";
//	private static final String VALUE_KEY_TRIGGERMODE = "triggerMode";
	private static final String VALUE_KEY_FILEPATH = "filePath";
	private static final String VALUE_KEY_FILETEMPLATE = "fileTemplate";
	private static final String VALUE_KEY_AUTOINCREMENT = "autoIncrement";
	private static final String VALUE_KEY_FILENAME = "fileName";
	private static final String VALUE_KEY_FILENUMBER = "fileNumber";

	private StateMap scanDeviceProxy;

//	private StateMap ccdCollectionProxy;
	private StateMap ccdFileProxy;
	private String templateScan;
	private String filePath;
//	private String scanTriggerMode;
	
	private ScanAuthzDAO scanAuthzDAO;

	@ResponseBody
	@RequestMapping(value = "/scan/device/{action}*", method = RequestMethod.POST)
	public FormResponseMap handleRequest(@PathVariable String action, @RequestParam(required = false) String scanName) {

		if(!canWriteBeamline()) {
			return new FormResponseMap(false, "Not permitted to " + action + " scan.");
		}

		Map<String,Serializable> values = new HashMap<String,Serializable>();
		
		if(ACTION_VALUE_START.equals(action)) {
			
			String experimentGid = (String) beamlineSessionProxy.get(VALUE_KEY_EXPERIMENT_GID);
			if((experimentGid == null) || experimentGid.equalsIgnoreCase(DEFAULT_EXPERIMENT_GID)) {
				return new FormResponseMap(false, "Please select an experiment.");
			}
			
			if((scanName == null) || (scanName.trim().length() == 0)) {
				return new FormResponseMap(false, "Please enter a scan name.");
			}
			
			Date now = new Date();
			Scan scan = new Scan();
			scan.setName(scanName);
			scan.setDataUrl("/tmp");
			scan.setExperimentGid(experimentGid);
			scan.setStartDate(now);
			scan.setEndDate(now);
			
			AddResult result = scanAuthzDAO.add(SecurityUtil.getPersonGid(), scan).get();
			if(result.hasErrors()) {
				return new FormResponseMap(false, "Error while adding new scan. (1)");
			}
			
			GID gid = GID.parse(scan.getGid());
			if(gid == null) {
				return new FormResponseMap(false, "Error while adding new scan. (2)");
			}
			
			beamlineSessionProxy.put(VALUE_KEY_SCAN_GID, scan.getGid());
			beamlineSessionProxy.put(VALUE_KEY_SCAN_NAME, scan.getName());
			
			// check if xrd settings are required
			if (((String) beamlineSessionProxy.get(VALUE_KEY_TECHNIQUE)).contains("XRD")) {
				Map<String,Serializable> fileValues = new HashMap<String,Serializable>();
				fileValues.put(VALUE_KEY_FILEPATH, filePath+"scan-"+gid.getId()+"\\");
				fileValues.put(VALUE_KEY_FILENUMBER, new Integer(1)); // always starts from 1

				if(beamlineSessionProxy.get(VALUE_KEY_MODE)!= null && !((String)beamlineSessionProxy.get(VALUE_KEY_MODE)).equals("scan")) { // has not switched
					beamlineSessionProxy.put(VALUE_KEY_MODE, "scan");
					fileValues.put(VALUE_KEY_FILENAME, "scan");
					fileValues.put(VALUE_KEY_FILENUMBER, new Integer(1));
					fileValues.put(VALUE_KEY_FILETEMPLATE, templateScan);
					fileValues.put(VALUE_KEY_AUTOINCREMENT, new Integer(1));
//					ccdCollectionProxy.put(VALUE_KEY_TRIGGERMODE, new Integer(scanTriggerMode));  // daq cfg handles this
				}
				ccdFileProxy.putAll(fileValues);
			}
			
			values.put(VALUE_KEY_SCAN_ID, gid.getId());
			values.put(ACTION_VALUE_START, 1);
		}
		else if(ACTION_VALUE_STOP.equals(action)) {
			values.put(ACTION_VALUE_STOP, 1);
		}
		else if(ACTION_VALUE_PAUSE.equals(action)) {
			values.put(ACTION_VALUE_PAUSE, 1);
		}

		if(!values.isEmpty()) {
			scanDeviceProxy.putAll(values);
		}
		
		return new FormResponseMap(true);
	}

	public StateMap getScanDeviceProxy() {
		return scanDeviceProxy;
	}
	public void setScanDeviceProxy(StateMap scanDeviceProxy) {
		this.scanDeviceProxy = scanDeviceProxy;
	}

	public ScanAuthzDAO getScanAuthzDAO() {
		return scanAuthzDAO;
	}
	public void setScanAuthzDAO(ScanAuthzDAO scanAuthzDAO) {
		this.scanAuthzDAO = scanAuthzDAO;
	}

	public StateMap getCcdFileProxy() {
		return ccdFileProxy;
	}
	public void setCcdFileProxy(StateMap ccdFileProxy) {
		this.ccdFileProxy = ccdFileProxy;
	}

	public String getTemplateScan() {
		return templateScan;
	}
	public void setTemplateScan(String templateScan) {
		this.templateScan = templateScan;
	}

	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
//	public void getScanTriggerMode() {
//		return scanTriggerMode;
//	}
//	public void setScanTriggerMode(String scanTriggerMode) {
//		this.scanTriggerMode = scanTriggerMode;
//	}
}
