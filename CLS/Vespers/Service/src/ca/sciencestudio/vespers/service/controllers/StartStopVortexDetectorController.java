/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     StartStopVortexDetectorController class.
 *     
 */
package ca.sciencestudio.vespers.service.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.state.StateMap;
import ca.sciencestudio.util.web.BindAndValidateUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class StartStopVortexDetectorController {

	private static final String STATE_KEY_CONTROLLER_UID = "controllerUid";
	
	private static final String ACTION_VALUE_STOP = "stop";
	private static final String ACTION_VALUE_START = "start";
	
	private static final String VALUE_KEY_STOP = "acquireStop";
	private static final String VALUE_KEY_START = "acquireEraseStart";
	
	private StateMap vortexDetectorStateMap;
	private StateMap beamlineSessionStateMap;
	
	@RequestMapping(value = "/detector/vtx/{action}.{format}", method = RequestMethod.POST)
	public String handleRequest(@PathVariable String action, @PathVariable String format, ModelMap model) {
		
		BindException errors = BindAndValidateUtils.buildBindException();
		model.put("errors", errors);
		
		String responseView = "response-" + format;
		
		String personUid = (String) beamlineSessionStateMap.get(STATE_KEY_CONTROLLER_UID);
		
		if(!SecurityUtil.getPerson().getUid().equals(personUid)) {
			errors.reject("permission.denied", "Not permitted to " + action + " acquisition.");
			return responseView;
		}
		
		if(ACTION_VALUE_START.equals(action)) {
			vortexDetectorStateMap.put(VALUE_KEY_START, 1);
		} 
		else if(ACTION_VALUE_STOP.equals(action)) {
			vortexDetectorStateMap.put(VALUE_KEY_STOP, 1);
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
