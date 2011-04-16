/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		StartStopPauseScanDeviceController class.
 *
 */
package ca.sciencestudio.vespers.service.controllers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.dao.ScanDAO;

import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.state.StateMap;
import ca.sciencestudio.util.web.BindAndValidateUtils;

/**
 * @author medrand
 *
 */
@Controller
public class StartStopPauseScanDeviceController {

	private static final String STATE_KEY_EXPERIMENT_ID = "experimentId";
	private static final String STATE_KEY_CONTROLLER_UID = "controllerUid";
	
	private static final String VALUE_KEY_SCAN_ID = "scanId";
	
	/* Removed for initial production release *
	private static final String VALUE_KEY_MODE = "xrdMode";
	*/
	
	private static final String ACTION_VALUE_START = "start";
	private static final String ACTION_VALUE_STOP = "stop";
	private static final String ACTION_VALUE_PAUSE = "pause";

	/* Removed for initial production release *
	private static final String VALUE_KEY_TRIGGERMODE = "triggerMode";
	private static final String VALUE_KEY_FILEPATH = "filePath";
	private static final String VALUE_KEY_FILETEMPLATE = "fileTemplate";
	private static final String VALUE_KEY_AUTOINCREMENT = "autoIncrement";
	private static final String VALUE_KEY_FILENAME = "fileName";
	private static final String VALUE_KEY_FILENUMBER = "fileNumber";
	*/

	private StateMap scanDeviceStateMap;
	private StateMap beamlineSessionStateMap;

	/* Removed for initial production release *
	private StateMap ccdCollectionStateMap;
	private StateMap ccdFileStateMap;
	private String templateScan;
	private String filePath;
	*/
	
	private ScanDAO scanDAO;

	@RequestMapping(value = "/scan/device/{action}.{format}", method = RequestMethod.POST)
	public String handleRequest(@PathVariable String action, @PathVariable String format, 
									@RequestParam(required = false) String scanName, ModelMap model) {

		BindException errors = BindAndValidateUtils.buildBindException();
		model.put("errors", errors);
		
		String responseView = "response-" + format;
		
		String personUid = (String) beamlineSessionStateMap.get(STATE_KEY_CONTROLLER_UID);

		if(!SecurityUtil.getPerson().getUid().equals(personUid)) {
			errors.reject("permission.denied", "Not permitted to " + action + " scan.");
			return responseView;
		}

		Map<String,Serializable> values = new HashMap<String,Serializable>();
		
		if(ACTION_VALUE_START.equals(action)) {
			
			int experimentId = (Integer) beamlineSessionStateMap.get(STATE_KEY_EXPERIMENT_ID);
			if(experimentId <= 0) {
				errors.reject("permission.denied", "Please select an experiment.");
				return responseView;
			}
			
			if((scanName == null) || (scanName.length() == 0)) {
				errors.reject("permission.denied", "Please enter a scan name.");
				return responseView;
			}
			
			beamlineSessionStateMap.put("scanName", scanName);

			Scan scan = scanDAO.createScan();
			scan.setName(scanName);
			scan.setExperimentId(experimentId);
			int scanId = scanDAO.addScan(scan);
			
			/* Removed for initial production release *
			// FIXME checking the session type when there is session type as XRF+XRD
			Map<String,Serializable> fileValues = new HashMap<String,Serializable>();
			// FIXME need a better solution later, commented for testing
			//fileValues.put(VALUE_KEY_FILEPATH, filePath+scanId+"\\");
			fileValues.put(VALUE_KEY_FILEPATH, filePath);

			if(beamlineSessionStateMap.get(VALUE_KEY_MODE)!= null && !((String)beamlineSessionStateMap.get(VALUE_KEY_MODE)).equals("scan")) { // has not switched
				beamlineSessionStateMap.put(VALUE_KEY_MODE, "scan");
				fileValues.put(VALUE_KEY_FILENAME, "scan");
				fileValues.put(VALUE_KEY_FILENUMBER, 1);
				fileValues.put(VALUE_KEY_FILETEMPLATE, templateScan);
				fileValues.put(VALUE_KEY_AUTOINCREMENT, 1);
				ccdCollectionStateMap.put(VALUE_KEY_TRIGGERMODE, 0); // freerun, does not hurt
			}
			ccdFileStateMap.putAll(fileValues);
			*/
			
			values.put(VALUE_KEY_SCAN_ID, scanId);
			values.put(ACTION_VALUE_START, 1);
		}
		else if(ACTION_VALUE_STOP.equals(action)) {
			values.put(ACTION_VALUE_STOP, 1);
		}
		else if(ACTION_VALUE_PAUSE.equals(action)) {
			values.put(ACTION_VALUE_PAUSE, 1);
		}

		if(!values.isEmpty()) {
			scanDeviceStateMap.putAll(values);
		}
		
		return responseView;
	}

	public StateMap getScanDeviceStateMap() {
		return scanDeviceStateMap;
	}
	public void setScanDeviceStateMap(StateMap scanDeviceStateMap) {
		this.scanDeviceStateMap = scanDeviceStateMap;
	}
	
	public StateMap getBeamlineSessionStateMap() {
		return beamlineSessionStateMap;
	}
	public void setBeamlineSessionStateMap(StateMap beamlineSessionStateMap) {
		this.beamlineSessionStateMap = beamlineSessionStateMap;
	}

	public ScanDAO getScanDAO() {
		return scanDAO;
	}
	public void setScanDAO(ScanDAO scanDAO) {
		this.scanDAO = scanDAO;
	}

	/* Removed for initial production release *
	public void setCcdCollectionStateMap(StateMap ccdCollectionStateMap) {
		this.ccdCollectionStateMap = ccdCollectionStateMap;
	}

	public void setCcdFileStateMap(StateMap ccdFileStateMap) {
		this.ccdFileStateMap = ccdFileStateMap;
	}

	public void setTemplateScan(String templateScan) {
		this.templateScan = templateScan;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	*/
}
