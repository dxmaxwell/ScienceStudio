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
public class UpdateFourElementDetectorController extends AbstractBeamlineAuthzController {

	private static final String VALUE_KEY_MAX_ENERGY_ALL = "maxEnergyAll";
	private static final String VALUE_KEY_PRESET_TIME_ALL =  "presetTimeAll";
	
	private StateMap fourElementDetectorProxy;
	
	@ResponseBody
	@RequestMapping(value = "/detector/fed/setup*", method = RequestMethod.POST)
	public FormResponseMap handleRequest(@RequestParam(required = false) String presetTimeAllSP,
											@RequestParam(required = false) String maxEnergyAllSP) {
		
		if(!canWriteBeamline()) {
			return new FormResponseMap(false,"Not permitted to setup detector.");
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
			fourElementDetectorProxy.putAll(values);
		}
		
		return new FormResponseMap(true);
	}

	public StateMap getFourElementDetectorProxy() {
		return fourElementDetectorProxy;
	}
	public void setFourElementDetectorProxy(StateMap fourElementDetectorProxy) {
		this.fourElementDetectorProxy = fourElementDetectorProxy;
	}
}
