/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     HeartbeatController class.
 *     
 */
package ca.sciencestudio.vespers.service.admin.controllers;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.util.state.SimpleStateMap;
import ca.sciencestudio.util.state.StateMap;
import ca.sciencestudio.util.web.FormResponseMap;

/**
 * @author maxweld
 * 
 *
 */
@Controller
public class HeartbeatController extends AbstractBeamlineAdminController {
	
	private static final String VALUE_KEY_TIMESTAMP = "timestamp";
	
	private Collection<StateMap> deviceProxyList = new ArrayList<StateMap>();
		
	@ResponseBody
	@RequestMapping(value = "/heartbeat*", method = RequestMethod.GET)
	public FormResponseMap handleRequest() {
	
		if(!canAdminBeamline()) {
			return new FormResponseMap(false, "Not permitted to view heartbeat.");			
		}
		
		FormResponseMap response = new FormResponseMap(true);
	
		for(StateMap deviceProxy : deviceProxyList) {
			StateMap tempStateMap = new SimpleStateMap(deviceProxy);
		
			if(!tempStateMap.containsKey(VALUE_KEY_TIMESTAMP)) {
				tempStateMap.put(VALUE_KEY_TIMESTAMP, tempStateMap.getTimestamp().getTime());
			}
		
			response.put(tempStateMap.getName(), tempStateMap);
		}
	
		return response;
	}

	public Collection<StateMap> getDeviceProxyList() {
		return deviceProxyList;
	}
	public void setDeviceProxyList(Collection<StateMap> deviceProxyList) {
		this.deviceProxyList = deviceProxyList;
	}	
}
