/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     StartStopFourElementDetectorController class.
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
 * @author medrand
 *
 */
@Controller
public class StartStopFourElementDetectorController {

	private static final String STATE_KEY_CONTROLLER_UID = "controllerUid";
	
	private static final String PARAM_VALUE_START_ALL = "start";
	private static final String PARAM_VALUE_STOP_ALL = "stop";
	
	private static final String VALUE_KEY_STOP_ALL = "acquireStopAll";
	private static final String VALUE_KEY_ERASE_START_ALL = "acquireEraseStartAll";
	
	private StateMap fourElementDetectorStateMap;
	private StateMap beamlineSessionStateMap;
	
	@RequestMapping(value = "/detector/fed/{action}.{format}", method = RequestMethod.POST)
	public String handleRequest(@PathVariable String action, @PathVariable String format, ModelMap model) {
		
		BindException errors = BindAndValidateUtils.buildBindException();
		model.put("errors", errors);
		
		String responseView = "response-" + format;
		
		String personUid = (String) beamlineSessionStateMap.get(STATE_KEY_CONTROLLER_UID);
		
		if(!SecurityUtil.getPerson().getUid().equals(personUid)) {
			errors.reject("permission.denied", "Not permitted to " + action + " acquisition.");
			return responseView;	
		}
		
		if(PARAM_VALUE_START_ALL.equals(action)) {
			fourElementDetectorStateMap.put(VALUE_KEY_ERASE_START_ALL, 1);
		} 
		else if(PARAM_VALUE_STOP_ALL.equals(action)) {
			fourElementDetectorStateMap.put(VALUE_KEY_STOP_ALL, 1);	
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
