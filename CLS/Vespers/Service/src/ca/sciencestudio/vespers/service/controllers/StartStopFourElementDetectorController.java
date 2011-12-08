/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     StartStopFourElementDetectorController class.
 *     
 */
package ca.sciencestudio.vespers.service.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.util.state.StateMap;
import ca.sciencestudio.util.web.FormResponseMap;

/**
 * @author medrand
 *
 */
@Controller
public class StartStopFourElementDetectorController extends AbstractBeamlineAuthzController {

	private static final String PARAM_VALUE_START_ALL = "start";
	private static final String PARAM_VALUE_STOP_ALL = "stop";
	
	private static final String VALUE_KEY_STOP_ALL = "acquireStopAll";
	private static final String VALUE_KEY_ERASE_START_ALL = "acquireEraseStartAll";
	
	private StateMap fourElementDetectorProxy;
	
	@ResponseBody
	@RequestMapping(value = "/detector/fed/{action}*", method = RequestMethod.POST)
	public FormResponseMap handleRequest(@PathVariable String action) {
		
		if(!canWriteBeamline()) {
			return new FormResponseMap(false, "Not permitted to " + action + " acquisition.");
		}
		
		if(PARAM_VALUE_START_ALL.equals(action)) {
			fourElementDetectorProxy.put(VALUE_KEY_ERASE_START_ALL, 1);
		}
		else if(PARAM_VALUE_STOP_ALL.equals(action)) {
			fourElementDetectorProxy.put(VALUE_KEY_STOP_ALL, 1);
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
