/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     UpdateFourElementDetectorController class.
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
public class UpdateFourElementDetectorController {

	private static final String STATE_KEY_CONTROLLER_UID = "controllerUid";
	
	private static final String VALUE_KEY_MAX_ENERGY_ALL = "maxEnergyAll";
	private static final String VALUE_KEY_PRESET_TIME_ALL =  "presetTimeAll";
	
	private StateMap fourElementDetectorStateMap;
	private StateMap beamlineSessionStateMap;
	
	@RequestMapping(value = "/detector/fed/setup.{format}", method = RequestMethod.POST)
	public String handleRequest(@RequestParam(required = false) String presetTimeAllSP, 
									@RequestParam(required = false) String maxEnergyAllSP,
												@PathVariable String format, ModelMap model) {
		
		BindException errors = BindAndValidateUtils.buildBindException();
		model.put("errors", errors);
		 
		String responseView = "response-" + format;
		
		String personUid = (String) beamlineSessionStateMap.get(STATE_KEY_CONTROLLER_UID);
		
		if(!SecurityUtil.getPerson().getUid().equals(personUid)) {
			errors.reject("permission.denied", "Not permitted to setup detector.");	
			return responseView;
		}
		
		Map<String,Serializable> values = new HashMap<String,Serializable>();
		
		if(presetTimeAllSP != null) {
			try {
				values.put(VALUE_KEY_PRESET_TIME_ALL, Double.parseDouble(presetTimeAllSP));
			}
			catch(NumberFormatException e) { /* ignore */ }
		}
		
		if(maxEnergyAllSP != null) {
			try {
				values.put(VALUE_KEY_MAX_ENERGY_ALL, Double.parseDouble(maxEnergyAllSP));
			}
			catch(NumberFormatException e) { /* ignore */ }
		}
		
		if(!values.isEmpty()) {
			fourElementDetectorStateMap.putAll(values);
		}
		
		return responseView;
	}
	
	public StateMap getFourElementDetectorStateMap() {
		return fourElementDetectorStateMap;
	}
	public void setFourElementDetectorStateMap(StateMap fourElementDetectorStateMap) {
		this.fourElementDetectorStateMap = fourElementDetectorStateMap;
	}

	public StateMap getBeamlineSessionStateMap() {
		return beamlineSessionStateMap;
	}
	public void setBeamlineSessionStateMap(StateMap beamlineSessionStateMap) {
		this.beamlineSessionStateMap = beamlineSessionStateMap;
	}
}
