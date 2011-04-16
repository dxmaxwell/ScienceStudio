/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     HeartbeatController class.
 *     
 */
package ca.sciencestudio.vespers.service.controllers;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;

import ca.sciencestudio.util.state.SimpleStateMap;
import ca.sciencestudio.util.state.StateMap;

/**
 * @author medrand
 * The Heartbeat controller
 */
@Controller
public class HeartbeatController {

	private static final String VALUE_KEY_PROJECT_ID = "projectId";
	
	private StateMap beamlineSessionStateMap;
	
	private List<StateMap> stateMapList;
	private Map<StateMap, Collection<String>> excludeMap;
	
	@RequestMapping(value = "/heartbeat.{format}", method = RequestMethod.GET)
	public String handleRequest(@PathVariable String format, ModelMap model) {
	
		Map<String,Object> response = new HashMap<String,Object>();
		model.put("response", response);
		
		Integer projectId = (Integer) beamlineSessionStateMap.get(VALUE_KEY_PROJECT_ID);
		if(projectId == null) { projectId = new Integer(0); }
		
		Object admin = AuthorityUtil.buildRoleAuthority("ADMIN_VESPERS");
		Object group = AuthorityUtil.buildProjectGroupAuthority(projectId);
		
		if(!SecurityUtil.hasAnyAuthority(group,admin)) {
			Map<String,String> error = new HashMap<String,String>();
			error.put("message", "Mot permitted to view session.</error>");
			response.put("error", error);
			return "response-" + format;
		}
		
		for(StateMap stateMap : stateMapList) {
			StateMap tempStateMap = new SimpleStateMap(stateMap);
			
			if(!tempStateMap.containsKey("timestamp")) {
				tempStateMap.put("timestamp", tempStateMap.getTimestamp().getTime());
			}
			
			if((excludeMap != null) && excludeMap.containsKey(stateMap)) {
				tempStateMap.keySet().removeAll(excludeMap.get(stateMap));
			}
			
			response.put(tempStateMap.getName(), tempStateMap);
		}
		
		return "response-" + format;
	}

	public StateMap getBeamlineSessionStateMap() {
		return beamlineSessionStateMap;
	}
	public void setBeamlineSessionStateMap(StateMap beamlineSessionStateMap) {
		this.beamlineSessionStateMap = beamlineSessionStateMap;
	}

	public List<StateMap> getStateMapList() {
		return stateMapList;
	}
	public void setStateMapList(List<StateMap> stateMapList) {
		this.stateMapList = stateMapList;
	}

	public Map<StateMap, Collection<String>> getExcludeMap() {
		return excludeMap;
	}
	public void setExcludeMap(Map<StateMap, Collection<String>> excludeMap) {
		this.excludeMap = excludeMap;
	}
}
