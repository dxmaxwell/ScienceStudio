/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		GetSampleCameraCheckController class.
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
import org.springframework.web.bind.annotation.RequestParam;

import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.state.StateMap;

/**
 * @author maxweld
 *
 */
@Controller
public class GetSampleCameraQueryController {
	
	private static final String VALUE_KEY_PROJECT_ID = "projectId";
	
	private StateMap sampleCameraStateMap;
	private StateMap beamlineSessionStateMap;
	
	@RequestMapping(value = "/sample/camera/query.{format}", method = RequestMethod.POST)
	public String handleRequest(@PathVariable String format, @RequestParam long timestamp, ModelMap model) {

		Map<String,Object> response = new HashMap<String, Object>();
		model.put("response", response);
		
		Integer projectId = (Integer) beamlineSessionStateMap.get(VALUE_KEY_PROJECT_ID);
		if(projectId == null) { projectId = new Integer(0); }
		
		Object admin = AuthorityUtil.buildRoleAuthority("ADMIN_VESPERS");
		Object group = AuthorityUtil.buildProjectGroupAuthority(projectId);
		
		if(!(SecurityUtil.hasAnyAuthority(group,admin))) {
			Map<String,String> error = new HashMap<String,String>();
			error.put("message", "Not permitted to query sample camera.");
			response.put("error", error);
			return "response-" + format;
		}
		
		long deviceTimestamp = sampleCameraStateMap.getTimestamp().getTime();
		
		if(deviceTimestamp > timestamp) {
			response.put("timestamp", deviceTimestamp);
		} else {
			response.put("timestamp", timestamp);
		}
		
		return "response-" + format;		
	}

	public StateMap getSampleCameraStateMap() {
		return sampleCameraStateMap;
	}
	public void setSampleCameraStateMap(StateMap sampleCameraStateMap) {
		this.sampleCameraStateMap = sampleCameraStateMap;
	}
	
	public StateMap getBeamlineSessionStateMap() {
		return beamlineSessionStateMap;
	}
	public void setBeamlineSessionStateMap(StateMap beamlineSessionStateMap) {
		this.beamlineSessionStateMap = beamlineSessionStateMap;
	}
}
