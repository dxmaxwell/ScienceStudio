/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		HeartbeatController class.
 *     
 */
package ca.sciencestudio.nanofab.service.controllers;

import java.util.Map;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.sciencestudio.nanofab.state.NanofabSessionStateMap;
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.web.BindAndValidateUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class HeartbeatController {

	private NanofabSessionStateMap nanofabSessionStateMap;
	
	@RequestMapping(value = "/heartbeat.{format}", method = RequestMethod.GET)
	public String getHeartbeat(@PathVariable String format, ModelMap model) {
		
		BindException errors = BindAndValidateUtils.buildBindException();
		model.put("errors", errors);
		
		String responseView = "response-" + format;
		
		int projectId = nanofabSessionStateMap.getProjectId();
		Object admin = AuthorityUtil.buildRoleAuthority("ADMIN_NANOFAB");
		Object group = AuthorityUtil.buildProjectGroupAuthority(projectId);
		
		if(!SecurityUtil.hasAnyAuthority(group, admin)) {
			errors.reject("permission.denied", "Not permitted to view heartbeat.");
			return responseView;
		}
		
		Map<String,Object> response  = new HashMap<String,Object>();
		response.put(nanofabSessionStateMap.getName(),	nanofabSessionStateMap);
		
		model.put("response", response);
		return responseView;
	}

	public NanofabSessionStateMap getNanofabSessionStateMap() {
		return nanofabSessionStateMap;
	}
	public void setNanofabSessionStateMap(NanofabSessionStateMap nanofabSessionStateMap) {
		this.nanofabSessionStateMap = nanofabSessionStateMap;
	}
}
