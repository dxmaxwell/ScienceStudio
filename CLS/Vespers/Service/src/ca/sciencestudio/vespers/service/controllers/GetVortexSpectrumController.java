/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		GetVortexSpectrumController class.
 *     
 */
package ca.sciencestudio.vespers.service.controllers;

import java.util.Map;
import java.util.HashMap;

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
public class GetVortexSpectrumController {
	
	private static final String STATE_KEY_PROJECT_ID = "projectId";
	
	private static final String VALUE_KEY_SPECTRUM = "spectrum";
	private static final String VALUE_KEY_MAX_ENERGY = "maxEnergy";
		
	private static final int[] DEFAULT_VALUE_SPECTRUM = new int[0];
	private static final double DEFAULT_VALUE_MAX_ENERGY = 1.0;
	
	private StateMap vortexDetectorStateMap;
	private StateMap beamlineSessionStateMap;
	
	@RequestMapping(value = "/detector/vtx/spectrum.{format}", method = RequestMethod.GET)
	public String handleRequest(@PathVariable String format, ModelMap model) {
		
		Map<String,Object> response = new HashMap<String,Object>();
		model.put("response", response);
		
		String responseView = "response-" + format;
		
		Integer projectId = (Integer) beamlineSessionStateMap.get(STATE_KEY_PROJECT_ID);
		if(projectId == null) { projectId = new Integer(0); }
		
		Object admin = AuthorityUtil.buildRoleAuthority("ADMIN_VESPERS");
		Object group = AuthorityUtil.buildProjectGroupAuthority(projectId);
		
		if(!SecurityUtil.hasAnyAuthority(group, admin)) {
			response.put("error", "Not permitted to get spectrum.");
			response.put(VALUE_KEY_SPECTRUM, DEFAULT_VALUE_SPECTRUM);
			response.put(VALUE_KEY_MAX_ENERGY, DEFAULT_VALUE_MAX_ENERGY);
			return responseView;
		}
		
		if(vortexDetectorStateMap.containsKey(VALUE_KEY_SPECTRUM)) {
			response.put(VALUE_KEY_SPECTRUM, vortexDetectorStateMap.get(VALUE_KEY_SPECTRUM));
		}
		else {
			response.put(VALUE_KEY_SPECTRUM, DEFAULT_VALUE_SPECTRUM);
		}
		
		if(vortexDetectorStateMap.containsKey(VALUE_KEY_MAX_ENERGY)) {
			response.put(VALUE_KEY_MAX_ENERGY, vortexDetectorStateMap.get(VALUE_KEY_MAX_ENERGY));
		}
		else {
			response.put(VALUE_KEY_MAX_ENERGY, DEFAULT_VALUE_MAX_ENERGY);
		}
		
		return responseView;
	}

	public StateMap getVortexDetectorStateMap() {
		return vortexDetectorStateMap;
	}
	public void setVortexDetectorStateMap(StateMap vortexDetectorStateMap) {
		this.vortexDetectorStateMap = vortexDetectorStateMap;
	}

	public StateMap getBeamlineSessionStateMap() {
		return beamlineSessionStateMap;
	}
	public void setBeamlineSessionStateMap(StateMap beamlineSessionStateMap) {
		this.beamlineSessionStateMap = beamlineSessionStateMap;
	}
}
