/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		GetFourElementSpectrumController class.
 *     
 */
package ca.sciencestudio.vespers.service.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.state.StateMap;

/**
 * @author maxweld
 *
 */
@Controller
public class GetFourElementSpectrumController {

	private static final String STATE_KEY_PROJECT_ID = "projectId";
	
	private static final String VALUE_KEY_SPECTRUM_ALL = "spectrumAll";
	private static final String VALUE_KEY_MAX_ENERGY_ALL = "maxEnergyAll";
		
	private static final int[] DEFAULT_VALUE_SPECTRUM_ALL = new int[0];
	private static final double DEFAULT_VALUE_MAX_ENERGY_ALL = 1.0;
	
	private StateMap fourElementDetectorStateMap;
	private StateMap beamlineSessionStateMap;
	
	@RequestMapping(value = "/detector/fed/spectrum.{format}", method = RequestMethod.GET)
	public String handleRequest(@PathVariable String format, ModelMap model) {
		
		Map<String,Object> response = new HashMap<String,Object>();
		model.put("response", response);
		
		Integer projectId = (Integer) beamlineSessionStateMap.get(STATE_KEY_PROJECT_ID);
		if(projectId == null) { projectId = new Integer(0); }
		
		Object admin = AuthorityUtil.buildRoleAuthority("ADMIN_VESPERS");
		Object group = AuthorityUtil.buildProjectGroupAuthority(projectId);
		
		if(!SecurityUtil.hasAnyAuthority(group, admin)) {
			response.put("error", "Not permitted to get spectrum.");
			response.put(VALUE_KEY_SPECTRUM_ALL, DEFAULT_VALUE_SPECTRUM_ALL);
			response.put(VALUE_KEY_MAX_ENERGY_ALL, DEFAULT_VALUE_MAX_ENERGY_ALL);
			return "response-" + format;
		}
		
		if(fourElementDetectorStateMap.containsKey(VALUE_KEY_SPECTRUM_ALL)) {
			response.put(VALUE_KEY_SPECTRUM_ALL,
					fourElementDetectorStateMap.get(VALUE_KEY_SPECTRUM_ALL));
		}
		else {
			response.put(VALUE_KEY_SPECTRUM_ALL, DEFAULT_VALUE_SPECTRUM_ALL);
		}
		
		if(fourElementDetectorStateMap.containsKey(VALUE_KEY_MAX_ENERGY_ALL)) {
			response.put(VALUE_KEY_MAX_ENERGY_ALL,
					fourElementDetectorStateMap.get(VALUE_KEY_MAX_ENERGY_ALL));
		}
		else {
			response.put(VALUE_KEY_MAX_ENERGY_ALL, DEFAULT_VALUE_MAX_ENERGY_ALL);
		}
		
		model.put("response", response);
		return "response-" + format;
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
