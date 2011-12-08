/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     UpdateSampleStageHVController class.
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
public class UpdateSampleStageHVController extends AbstractBeamlineAuthzController {

	private static final String VALUE_KEY_SET_POINT_H = "setPointH";
	private static final String VALUE_KEY_SET_POINT_V = "setPointV";
	
	private StateMap sampleStateHVProxy;
	
	@ResponseBody
	@RequestMapping(value = "/sample/stage/hv*", method = RequestMethod.POST)
	public FormResponseMap handleRequest(@RequestParam(required=false) String setPointH,
											@RequestParam(required=false) String setPointV) {
		
		if(!canWriteBeamline()) {
			return new FormResponseMap(false, "Not permitted to set stage position.");
		}
		
		Map<String,Serializable> values = new HashMap<String,Serializable>();
		
		if(setPointH != null) {
			try {
				values.put(VALUE_KEY_SET_POINT_H, Double.parseDouble(setPointH));
			}
			catch (NumberFormatException e) { /* ignore */ }
		}
		
		if(setPointV != null) {
			try {
				values.put(VALUE_KEY_SET_POINT_V, Double.parseDouble(setPointV));
			}
			catch (NumberFormatException e) { /* ignore */ }
		}
		
		if(!values.isEmpty()) {
			sampleStateHVProxy.putAll(values);
		}
		
		return new FormResponseMap(true);
	}

	public StateMap getSampleStateHVProxy() {
		return sampleStateHVProxy;
	}
	public void setSampleStateHVProxy(StateMap sampleStateHVProxy) {
		this.sampleStateHVProxy = sampleStateHVProxy;
	}
}
