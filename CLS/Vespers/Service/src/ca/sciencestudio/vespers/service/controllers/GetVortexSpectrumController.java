/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		GetVortexSpectrumController class.
 *     
 */
package ca.sciencestudio.vespers.service.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.util.state.StateMap;
import ca.sciencestudio.util.web.FormResponseMap;

/**
 * @author maxweld
 *
 */
@Controller
public class GetVortexSpectrumController extends AbstractBeamlineAuthzController {
	
	private static final String VALUE_KEY_SPECTRUM = "spectrum";
	private static final String VALUE_KEY_MAX_ENERGY = "maxEnergy";
		
	private static final int[] DEFAULT_VALUE_SPECTRUM = new int[0];
	private static final double DEFAULT_VALUE_MAX_ENERGY = 1.0;
	
	private StateMap vortexDetectorProxy;
	
	@ResponseBody
	@RequestMapping(value = "/detector/vtx/spectrum*", method = RequestMethod.GET)
	public FormResponseMap handleRequest() {
		
		if(!canReadBeamline()) {
			FormResponseMap response = new FormResponseMap(false, "Not permitted to get spectrum.");
			response.put(VALUE_KEY_SPECTRUM, DEFAULT_VALUE_SPECTRUM);
			response.put(VALUE_KEY_MAX_ENERGY, DEFAULT_VALUE_MAX_ENERGY);
			return response;
		}
		
		FormResponseMap response = new FormResponseMap(true);
		
		if(vortexDetectorProxy.containsKey(VALUE_KEY_SPECTRUM)) {
			response.put(VALUE_KEY_SPECTRUM, vortexDetectorProxy.get(VALUE_KEY_SPECTRUM));
		}
		else {
			response.put(VALUE_KEY_SPECTRUM, DEFAULT_VALUE_SPECTRUM);
		}
		
		if(vortexDetectorProxy.containsKey(VALUE_KEY_MAX_ENERGY)) {
			response.put(VALUE_KEY_MAX_ENERGY, vortexDetectorProxy.get(VALUE_KEY_MAX_ENERGY));
		}
		else {
			response.put(VALUE_KEY_MAX_ENERGY, DEFAULT_VALUE_MAX_ENERGY);
		}
		
		return response;
	}

	public StateMap getVortexDetectorProxy() {
		return vortexDetectorProxy;
	}
	public void setVortexDetectorProxy(StateMap vortexDetectorProxy) {
		this.vortexDetectorProxy = vortexDetectorProxy;
	}
}
