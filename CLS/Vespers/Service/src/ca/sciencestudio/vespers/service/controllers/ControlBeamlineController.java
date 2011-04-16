/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ControlBeamlineController class.
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
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.util.web.BindAndValidateUtils;
import ca.sciencestudio.vespers.device.proxy.event.BeamlineSessionProxyEventListener;

/**
 * @author maxweld
 *
 */
@Controller
public class ControlBeamlineController {

	private static final String STATE_KEY_PROJECT_ID = "projectId";
	
	private BeamlineSessionProxyEventListener beamlineSessionStateMap;
	
	@RequestMapping(value = "/session/control.{format}", method = RequestMethod.GET)
	public String handleRequest(@PathVariable String format, ModelMap model) {
		
		BindException errors = BindAndValidateUtils.buildBindException();
		model.put("errors", errors);
		
		Integer projectId = (Integer) beamlineSessionStateMap.get(STATE_KEY_PROJECT_ID);
		if(projectId == null) { projectId = new Integer(0); }
		
		Object admin = AuthorityUtil.buildRoleAuthority("ADMIN_VESPERS");
		Object exptr = AuthorityUtil.buildProjectExperimenterAuthority(projectId);
		
		if(SecurityUtil.hasAnyAuthority(exptr, admin)) {
			beamlineSessionStateMap.setController(SecurityUtil.getPerson());
		} else {
			errors.reject("permission.denied", "Not permitted to control session.");
		}
		
		return "response-" + format;
	}

	public BeamlineSessionProxyEventListener getBeamlineSessionStateMap() {
		return beamlineSessionStateMap;
	}
	public void setBeamlineSessionStateMap(BeamlineSessionProxyEventListener beamlineSessionStateMap) {
		this.beamlineSessionStateMap = beamlineSessionStateMap;
	}
}
