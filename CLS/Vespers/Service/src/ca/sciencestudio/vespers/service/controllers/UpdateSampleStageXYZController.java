/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     UpdateSampleStageXYZController class.
 *     
 */
package ca.sciencestudio.vespers.service.controllers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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
public class UpdateSampleStageXYZController extends AbstractBeamlineAuthzController {

	private static final String VALUE_KEY_SET_POINT_X = "setPointX";
	private static final String VALUE_KEY_SET_POINT_Y = "setPointY";
	private static final String VALUE_KEY_SET_POINT_Z = "setPointZ";
	
	private StateMap sampleStateXYZProxy;
	
	@ResponseBody
	@RequestMapping(value = "/sample/stage/xyz*", method = RequestMethod.POST)
	public FormResponseMap handleRequest(@RequestParam(required=false) String setPointX,
											@RequestParam(required=false) String setPointY,
												@RequestParam(required=false) String setPointZ) {
				
		if(!canWriteBeamline()) {
			return new FormResponseMap(false, "Not permitted to set stage position.");
		}
		
		Map<String,Serializable> fields = new HashMap<String,Serializable>();
		
		double spX = 0;
		double spY = 0;
		double spZ = 0;
		
		try {
			spX = (setPointX != null) ? (spX = Double.parseDouble(setPointX)) : 0;
			fields.put(VALUE_KEY_SET_POINT_X, spX);
		}
		catch (NumberFormatException e) { /* ignore */ }
		
		try {
			spY = (setPointY != null) ? (spY = Double.parseDouble(setPointY)) : 0;
			fields.put(VALUE_KEY_SET_POINT_Y, spY);
		}
		catch (NumberFormatException e) { /* ignore */ }
		
		
		try {
			spZ = (setPointZ != null) ? (spZ = Double.parseDouble(setPointZ)) : 0;
			fields.put(VALUE_KEY_SET_POINT_Z, spZ);
		}
		catch (NumberFormatException e) { /* ignore */ }
	
		if(!fields.isEmpty()) {
			sampleStateXYZProxy.putAll(fields);
		}
		
		return new FormResponseMap(true);
	}

	public StateMap getSampleStateXYZProxy() {
		return sampleStateXYZProxy;
	}
	public void setSampleStateXYZProxy(StateMap sampleStateXYZProxy) {
		this.sampleStateXYZProxy = sampleStateXYZProxy;
	}
}
