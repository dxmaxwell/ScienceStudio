/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     StartStopVortexDetectorController class.
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
 * @author maxweld
 *
 */
@Controller
public class StartStopVortexDetectorController extends AbstractBeamlineAuthzController {

	private static final String ACTION_VALUE_STOP = "stop";
	private static final String ACTION_VALUE_START = "start";
	
	private static final String VALUE_KEY_STOP = "acquireStop";
	private static final String VALUE_KEY_START = "acquireEraseStart";
	
	private StateMap vortexDetectorProxy;
	
	@ResponseBody
	@RequestMapping(value = "/detector/vtx/{action}*", method = RequestMethod.POST)
	public FormResponseMap handleRequest(@PathVariable String action) {
		
		if(!canWriteBeamline()) {
			return new FormResponseMap(false, "Not permitted to " + action + " acquisition.");
		}
		
		if(ACTION_VALUE_START.equals(action)) {
			vortexDetectorProxy.put(VALUE_KEY_START, 1);
		}
		else if(ACTION_VALUE_STOP.equals(action)) {
			vortexDetectorProxy.put(VALUE_KEY_STOP, 1);
		}
		
		return new FormResponseMap(true);
	}

	public StateMap getVortexDetectorProxy() {
		return vortexDetectorProxy;
	}
	public void setVortexDetectorProxy(StateMap vortexDetectorProxy) {
		this.vortexDetectorProxy = vortexDetectorProxy;
	}
}
