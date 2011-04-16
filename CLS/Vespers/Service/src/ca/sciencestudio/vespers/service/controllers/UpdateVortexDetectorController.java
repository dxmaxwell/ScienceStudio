/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     UpdateVortexDetectorController class.
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

import ca.sciencestudio.util.state.StateMap;
import ca.sciencestudio.util.web.BindAndValidateUtils;
import ca.sciencestudio.security.util.SecurityUtil;

/**
 * @author maxweld
 *
 */
@Controller
public class UpdateVortexDetectorController {
	
	private static final String STATE_KEY_CONTROLLER_UID = "controllerUid";
	
	private static final String VALUE_KEY_PRESET_TIME_SP = "presetTime";
	private static final String VALUE_KEY_MAX_ENERGY_SP = "maxEnergySP";
	
	private StateMap vortexDetextorStateMap;
	private StateMap beamlineSessionStateMap;

	@RequestMapping(value = "/detector/vtx/setup.{format}", method = RequestMethod.POST)
	public String handleRequest(@RequestParam(required = false) String presetTimeSP, 
									@RequestParam(required = false) String maxEngySetPoint,
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

		if(presetTimeSP != null) {
			try {
				values.put(VALUE_KEY_PRESET_TIME_SP, Double.parseDouble(presetTimeSP));
			}
			catch(Exception e) { /* ignore */ }
		}
		
		if(maxEngySetPoint != null) {
			try {
				values.put(VALUE_KEY_MAX_ENERGY_SP, Double.parseDouble(maxEngySetPoint));
			}
			catch(Exception e) { /* ignore */ }
		}
		
		if(!values.isEmpty()) {
			vortexDetextorStateMap.putAll(values);
		}
		
		return responseView;
	}

	public StateMap getVortexDetextorStateMap() {
		return vortexDetextorStateMap;
	}
	public void setVortexDetextorStateMap(StateMap vortexDetextorStateMap) {
		this.vortexDetextorStateMap = vortexDetextorStateMap;
	}

	public StateMap getBeamlineSessionStateMap() {
		return beamlineSessionStateMap;
	}
	public void setBeamlineSessionStateMap(StateMap beamlineSessionStateMap) {
		this.beamlineSessionStateMap = beamlineSessionStateMap;
	}
}
