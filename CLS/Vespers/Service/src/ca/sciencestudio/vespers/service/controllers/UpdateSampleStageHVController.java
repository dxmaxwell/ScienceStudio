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
public class UpdateSampleStageHVController {

	private static final String VALUE_KEY_SET_POINT_H = "setPointH";
	private static final String VALUE_KEY_SET_POINT_V = "setPointV";
	
	private static final String STATE_KEY_CONTROLLER_UID = "controllerUid";
	
	private StateMap sampleStateHVStateMap;
	private StateMap beamlineSessionStateMap;
	
	@RequestMapping(value = "/sample/stage/hv.{format}", method = RequestMethod.POST)
	public String handleRequest(@RequestParam(required=false) String setPointH, 
									@RequestParam(required=false) String setPointV,
											@PathVariable String format, ModelMap model) {
		
		BindException errors = BindAndValidateUtils.buildBindException();
		model.put("errors", errors);
		
		String responseView = "response-" + format;
		
		String personUid = (String) beamlineSessionStateMap.get(STATE_KEY_CONTROLLER_UID);
		
		if(!SecurityUtil.getPerson().getUid().equals(personUid)) {
			errors.reject("permission.denied", "Not permitted to set stage position.");
			return responseView;
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
			sampleStateHVStateMap.putAll(values);	
		}
		
		return responseView;
	}	
	
	public StateMap getSampleStateHVStateMap() {
		return sampleStateHVStateMap;
	}
	public void setSampleStateHVStateMap(StateMap sampleStateHVStateMap) {
		this.sampleStateHVStateMap = sampleStateHVStateMap;
	}

	public StateMap getBeamlineSessionStateMap() {
		return beamlineSessionStateMap;
	}
	public void setBeamlineSessionStateMap(StateMap beamlineSessionStateMap) {
		this.beamlineSessionStateMap = beamlineSessionStateMap;
	}
}
