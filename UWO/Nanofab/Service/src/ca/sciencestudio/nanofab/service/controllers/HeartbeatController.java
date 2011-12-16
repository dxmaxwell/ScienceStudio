/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		HeartbeatController class.
 *     
 */
package ca.sciencestudio.nanofab.service.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.util.web.FormResponseMap;

/**
 * @author maxweld
 *
 */
@Controller
public class HeartbeatController extends AbstractLaboratoryAuthzController {
	
	@ResponseBody
	@RequestMapping(value = "/heartbeat*", method = RequestMethod.GET)
	public FormResponseMap getHeartbeat() {
		
		// No security check here because the user needs to know if the session
		// has been stopped, and that information comes from the heartbeat.
		FormResponseMap response  = new FormResponseMap(true);
		response.put(nanofabSessionStateMap.getName(),	nanofabSessionStateMap);
		return response;
	}
}
