/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		GetFourElementSpectrumController class.
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
public class GetFourElementSpectrumController extends AbstractBeamlineAuthzController {
	
	private static final String VALUE_KEY_SPECTRUM_ALL = "spectrumAll";
	private static final String VALUE_KEY_MAX_ENERGY_ALL = "maxEnergyAll";
		
	private static final int[] DEFAULT_VALUE_SPECTRUM_ALL = new int[0];
	private static final double DEFAULT_VALUE_MAX_ENERGY_ALL = 1.0;
	
	private StateMap fourElementDetectorProxy;
	
	@ResponseBody
	@RequestMapping(value = "/detector/fed/spectrum*", method = RequestMethod.GET)
	public FormResponseMap handleRequest() {
		
		if(!canReadBeamline()) {
			FormResponseMap response =  new FormResponseMap(false, "Not permitted to get spectrum.");
			response.put(VALUE_KEY_SPECTRUM_ALL, DEFAULT_VALUE_SPECTRUM_ALL);
			response.put(VALUE_KEY_MAX_ENERGY_ALL, DEFAULT_VALUE_MAX_ENERGY_ALL);
			return response;
		}
		
		FormResponseMap response =  new FormResponseMap(true);
		
		if(fourElementDetectorProxy.containsKey(VALUE_KEY_SPECTRUM_ALL)) {
			response.put(VALUE_KEY_SPECTRUM_ALL,
					fourElementDetectorProxy.get(VALUE_KEY_SPECTRUM_ALL));
		}
		else {
			response.put(VALUE_KEY_SPECTRUM_ALL, DEFAULT_VALUE_SPECTRUM_ALL);
		}
		
		if(fourElementDetectorProxy.containsKey(VALUE_KEY_MAX_ENERGY_ALL)) {
			response.put(VALUE_KEY_MAX_ENERGY_ALL,
					fourElementDetectorProxy.get(VALUE_KEY_MAX_ENERGY_ALL));
		}
		else {
			response.put(VALUE_KEY_MAX_ENERGY_ALL, DEFAULT_VALUE_MAX_ENERGY_ALL);
		}
		
		return response;
	}

	public StateMap getFourElementDetectorProxy() {
		return fourElementDetectorProxy;
	}
	public void setFourElementDetectorProxy(StateMap fourElementDetectorProxy) {
		this.fourElementDetectorProxy = fourElementDetectorProxy;
	}
}
