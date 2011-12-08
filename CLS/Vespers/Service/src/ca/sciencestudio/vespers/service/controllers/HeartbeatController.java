/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     HeartbeatController class.
 *     
 */
package ca.sciencestudio.vespers.service.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.util.state.SimpleStateMap;
import ca.sciencestudio.util.state.StateMap;
import ca.sciencestudio.util.web.FormResponseMap;

/**
 * @author maxweld
 * The Heartbeat controller
 */
@Controller
public class HeartbeatController extends AbstractBeamlineAuthzController {
	
	protected static final String VALUE_KEY_TIMESTAMP = "timestamp";
	 
	private Collection<StateMap> deviceProxyList = new ArrayList<StateMap>();
	private Collection<StateMap> authzDeviceProxyList = new ArrayList<StateMap>();
	private Map<StateMap, Collection<String>> excludeMap = new HashMap<StateMap, Collection<String>>();
	
	@ResponseBody
	@RequestMapping(value = "/heartbeat*", method = RequestMethod.GET)
	public FormResponseMap handleRequest() {
		
		Collection<StateMap> fullDeviceProxyList = new ArrayList<StateMap>(deviceProxyList);
		if(canReadBeamline()) {
			fullDeviceProxyList.addAll(authzDeviceProxyList);
		}
		
		FormResponseMap response = new FormResponseMap(true);
		
		for(StateMap deviceProxy : fullDeviceProxyList) {
			StateMap tempStateMap = new SimpleStateMap(deviceProxy);
			
			if(!tempStateMap.containsKey(VALUE_KEY_TIMESTAMP)) {
				tempStateMap.put(VALUE_KEY_TIMESTAMP, tempStateMap.getTimestamp().getTime());
			}
			
			if((excludeMap != null) && excludeMap.containsKey(deviceProxy)) {
				tempStateMap.keySet().removeAll(excludeMap.get(deviceProxy));
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

	public Collection<StateMap> getAuthzDeviceProxyList() {
		return authzDeviceProxyList;
	}
	public void setAuthzDeviceProxyList(Collection<StateMap> authzDeviceProxyList) {
		this.authzDeviceProxyList = authzDeviceProxyList;
	}

	public Map<StateMap, Collection<String>> getExcludeMap() {
		return excludeMap;
	}
	public void setExcludeMap(Map<StateMap, Collection<String>> excludeMap) {
		this.excludeMap = excludeMap;
	}
}
