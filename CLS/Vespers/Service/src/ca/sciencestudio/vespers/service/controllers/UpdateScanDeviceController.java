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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.util.state.StateMap;
import ca.sciencestudio.util.web.FormResponseMap;

/**
 * @author maxweld
 *
 */
@Controller
public class UpdateScanDeviceController extends AbstractBeamlineAuthzController {
	
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
	
	private StateMap scanDeviceProxy;
	private StateMap sampleCameraProxy;

	@ResponseBody
	@RequestMapping(value = "/scan/device/setup*", method = RequestMethod.POST)
	public FormResponseMap handleRequest(@RequestParam(required = false) String startPositionH,
											@RequestParam(required = false) String startPositionV,
												@RequestParam(required = false) String endPositionH,
													@RequestParam(required = false) String endPositionV,
														@RequestParam(required = false) String stepSizeH) {
		
		if(!canWriteBeamline()) {
			return new FormResponseMap(false, "Not permitted to set scan region.");
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
		
		if(sampleCameraProxy.containsKey(VALUE_KEY_SAMPLE_CAMERA_IMAGE) &&
				sampleCameraProxy.containsKey(VALUE_KEY_SAMPLE_CAMERA_SCALE) &&
				sampleCameraProxy.containsKey(VALUE_KEY_SAMPLE_CAMERA_POSITION_X) &&
				sampleCameraProxy.containsKey(VALUE_KEY_SAMPLE_CAMERA_POSITION_Y)) {
			values.put(VALUE_KEY_SAMPLE_IMAGE, sampleCameraProxy.get(VALUE_KEY_SAMPLE_CAMERA_IMAGE));
			values.put(VALUE_KEY_SAMPLE_IMAGE_SCALE, sampleCameraProxy.get(VALUE_KEY_SAMPLE_CAMERA_SCALE));
			values.put(VALUE_KEY_SAMPLE_IMAGE_POSITION_X, sampleCameraProxy.get(VALUE_KEY_SAMPLE_CAMERA_POSITION_X));
			values.put(VALUE_KEY_SAMPLE_IMAGE_POSITION_Y, sampleCameraProxy.get(VALUE_KEY_SAMPLE_CAMERA_POSITION_Y));
		}
		else {
			values.put(VALUE_KEY_SAMPLE_IMAGE, new byte[0]);
			values.put(VALUE_KEY_SAMPLE_IMAGE_SCALE, 1.0);
			values.put(VALUE_KEY_SAMPLE_IMAGE_POSITION_X, 0.0);
			values.put(VALUE_KEY_SAMPLE_IMAGE_POSITION_Y, 0.0);	
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

	public StateMap getSampleCameraProxy() {
		return sampleCameraProxy;
	}
	public void setSampleCameraProxy(StateMap sampleCameraProxy) {
		this.sampleCameraProxy = sampleCameraProxy;
	}
}
