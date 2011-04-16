/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		UpdateScanDeviceController class.
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

import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.state.StateMap;
import ca.sciencestudio.util.web.BindAndValidateUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class UpdateScanDeviceController {
	
	private static final String STATE_KEY_CONTROLLER_UID = "controllerUid";
	
	private static final String VALUE_KEY_START_POSITION_X = "startPositionX";
	private static final String VALUE_KEY_START_POSITION_Y = "startPositionY";
	private static final String VALUE_KEY_END_POSITION_X = "endPositionX";
	private static final String VALUE_KEY_END_POSITION_Y = "endPositionY";
	private static final String VALUE_KEY_STEP_SIZE_X = "stepSizeX";
	private static final String VALUE_KEY_STEP_SIZE_Y = "stepSizeY";
	
	private static final String VALUE_KEY_SAMPLE_IMAGE = "sampleImage";
	private static final String VALUE_KEY_SAMPLE_IMAGE_SCALE = "sampleImageScale";
	private static final String VALUE_KEY_SAMPLE_IMAGE_POSITION_X = "sampleImagePositionX";
	private static final String VALUE_KEY_SAMPLE_IMAGE_POSITION_Y = "sampleImagePositionY";
	
	private static final String VALUE_KEY_SAMPLE_CAMERA_IMAGE = "image";
	private static final String VALUE_KEY_SAMPLE_CAMERA_SCALE = "scale";
	private static final String VALUE_KEY_SAMPLE_CAMERA_POSITION_X = "positionH";
	private static final String VALUE_KEY_SAMPLE_CAMERA_POSITION_Y = "positionV";
	
	private StateMap scanDeviceStateMap;
	private StateMap sampleCameraStateMap;
	private StateMap beamlineSessionStateMap;

	@RequestMapping(value = "/scan/device/setup.{format}", method = RequestMethod.POST)
	public String handleRequest(@RequestParam(required = false) String startPositionH, 
									@RequestParam(required = false) String startPositionV, 
										@RequestParam(required = false) String endPositionH, 
											@RequestParam(required = false) String endPositionV,
												@RequestParam(required = false) String stepSizeH, 
													@PathVariable String format, ModelMap model) {
		
		BindException errors = BindAndValidateUtils.buildBindException();
		model.put("errors", errors);
		
		String responseView = "response-" + format;
		
		String personKey = (String) beamlineSessionStateMap.get(STATE_KEY_CONTROLLER_UID);
		
		if(!SecurityUtil.getPerson().getUid().equals(personKey)) {
			errors.reject("permission.denied", "Not permitted to set scan region.");
			return responseView;
		}
		
		Map<String,Serializable> values = new HashMap<String,Serializable>();
		
		if(startPositionH != null) {
			try {
				values.put(VALUE_KEY_START_POSITION_X, Double.parseDouble(startPositionH));
			}
			catch (NumberFormatException e) { /* ignore */ }
		}
		
		if(startPositionV != null) {
			try {
				values.put(VALUE_KEY_START_POSITION_Y, Double.parseDouble(startPositionV));
			}
			catch(NumberFormatException e) { /* ignore */ }
		}
		
		if(endPositionH != null) {
			try {
				values.put(VALUE_KEY_END_POSITION_X, Double.parseDouble(endPositionH));
			}
			catch (NumberFormatException e) { /* ignore */ }
		}
		
		if(endPositionV != null) {
			try {
				values.put(VALUE_KEY_END_POSITION_Y, Double.parseDouble(endPositionV));
			}
			catch(NumberFormatException e) { /* ignore */ }
		}
		
		if(stepSizeH != null) {
			try {
				double stepSizeX = Double.parseDouble(stepSizeH);
				values.put(VALUE_KEY_STEP_SIZE_X, stepSizeX);
				values.put(VALUE_KEY_STEP_SIZE_Y, stepSizeX);
			}
			catch(NumberFormatException e) { /* ignore */ }
		}
		
		if(sampleCameraStateMap.containsKey(VALUE_KEY_SAMPLE_CAMERA_IMAGE) &&
				sampleCameraStateMap.containsKey(VALUE_KEY_SAMPLE_CAMERA_SCALE) &&
				sampleCameraStateMap.containsKey(VALUE_KEY_SAMPLE_CAMERA_POSITION_X) &&
				sampleCameraStateMap.containsKey(VALUE_KEY_SAMPLE_CAMERA_POSITION_Y)) {
			values.put(VALUE_KEY_SAMPLE_IMAGE, sampleCameraStateMap.get(VALUE_KEY_SAMPLE_CAMERA_IMAGE));
			values.put(VALUE_KEY_SAMPLE_IMAGE_SCALE, sampleCameraStateMap.get(VALUE_KEY_SAMPLE_CAMERA_SCALE));
			values.put(VALUE_KEY_SAMPLE_IMAGE_POSITION_X, sampleCameraStateMap.get(VALUE_KEY_SAMPLE_CAMERA_POSITION_X));
			values.put(VALUE_KEY_SAMPLE_IMAGE_POSITION_Y, sampleCameraStateMap.get(VALUE_KEY_SAMPLE_CAMERA_POSITION_Y));
		}
		else {
			values.put(VALUE_KEY_SAMPLE_IMAGE, new byte[0]);
			values.put(VALUE_KEY_SAMPLE_IMAGE_SCALE, 1.0);
			values.put(VALUE_KEY_SAMPLE_IMAGE_POSITION_X, 0.0);
			values.put(VALUE_KEY_SAMPLE_IMAGE_POSITION_Y, 0.0);	
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

	public StateMap getSampleCameraStateMap() {
		return sampleCameraStateMap;
	}
	public void setSampleCameraStateMap(StateMap sampleCameraStateMap) {
		this.sampleCameraStateMap = sampleCameraStateMap;
	}

	public StateMap getBeamlineSessionStateMap() {
		return beamlineSessionStateMap;
	}
	public void setBeamlineSessionStateMap(StateMap beamlineSessionStateMap) {
		this.beamlineSessionStateMap = beamlineSessionStateMap;
	}
}
