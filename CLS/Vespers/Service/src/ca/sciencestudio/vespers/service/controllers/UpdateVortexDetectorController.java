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
public class UpdateVortexDetectorController extends AbstractBeamlineAuthzController {
	
	private static final String VALUE_KEY_PRESET_TIME_SP = "presetTime";
	private static final String VALUE_KEY_MAX_ENERGY_SP = "maxEnergySP";
	
	private StateMap vortexDetextorProxy;

	@ResponseBody
	@RequestMapping(value = "/detector/vtx/setup*", method = RequestMethod.POST)
	public FormResponseMap handleRequest(@RequestParam(required = false) String presetTimeSP,
											@RequestParam(required = false) String maxEngySetPoint) {
		
		if(!canWriteBeamline()) {
			return new FormResponseMap(false, "Not permitted to setup detector.");
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
			vortexDetextorProxy.putAll(values);
		}
		
		return new FormResponseMap(true);
	}

	public StateMap getVortexDetextorProxy() {
		return vortexDetextorProxy;
	}
	public void setVortexDetextorProxy(StateMap vortexDetextorProxy) {
		this.vortexDetextorProxy = vortexDetextorProxy;
	}
}
