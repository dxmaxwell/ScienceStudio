/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     UpdateSampleImageScaleController class.
 *     
 */
package ca.sciencestudio.vespers.service.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.state.StateMap;
import ca.sciencestudio.util.web.BindAndValidateUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class UpdateSampleCameraScaleController {
	
	private static final String STATE_KEY_CONTROLLER_UID = "controllerUid";
	
	private static final String VALUE_KEY_SCALE = "scale";
	
	
	private StateMap sampleCameraStateMap;
	private StateMap beamlineSessionStateMap;
	
	@RequestMapping(value = "/sample/camera/scale.{format}", method = RequestMethod.POST)
	public String handleRequest(@RequestParam(required = false) String scaleSP, 
									@PathVariable String format, ModelMap model) {	
		
		BindException errors = BindAndValidateUtils.buildBindException();
		model.put("errors", errors);
		
		String responseView = "response-" + format;
		
		String personUid = (String) beamlineSessionStateMap.get(STATE_KEY_CONTROLLER_UID);
		
		if(!SecurityUtil.getPerson().getUid().equals(personUid)) {
			errors.reject("permission.denied", "Not permitted to set sample image scale. (1)");
			return responseView;
		}
		
		Object admin = AuthorityUtil.buildRoleAuthority("ADMIN_VESPERS");
		
		if(!SecurityUtil.hasAuthority(admin)) {
			errors.reject("permission.denied", "Not permitted to set sample image scale. (1)");
			return responseView;
		}
		
		if(scaleSP != null) {
			try {
				sampleCameraStateMap.put(VALUE_KEY_SCALE, Double.parseDouble(scaleSP));
			}
			catch(NumberFormatException e) { /* Nothing To Do */ }
		}
		
		return responseView;
	}

	public void setSampleCameraStateMap(StateMap sampleCameraStateMap) {
		this.sampleCameraStateMap = sampleCameraStateMap;
	}

	public void setBeamlineSessionStateMap(StateMap beamlineSessionStateMap) {
		this.beamlineSessionStateMap = beamlineSessionStateMap;
	}
}
