/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      BeamlineInformationController class.
 *      	     
 */
package ca.sciencestudio.vespers.service.admin.controllers;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sciencestudio.device.proxy.DeviceProxy;
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.web.BindAndValidateUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class BeamlineInformationController {

	private static final String VALUE_KEY_GENERAL_COMMENT = "generalComment";
	private static final String VALUE_KEY_SPOT_SIZE_COMMENT = "spotSizeComment";
	
	private DeviceProxy beamlineInformationDeviceProxy;
	
	@RequestMapping(value = "/information.{format}", method = RequestMethod.POST)
	public String postInformationEdit(@RequestParam(required = false) String generalCommentSP, 
										@RequestParam(required = false) String spotSizeCommentSP, 
														@PathVariable String format, ModelMap model) {
		
		BindException errors = BindAndValidateUtils.buildBindException();
		model.put("errors", errors);
		
		String responseView = "response-" + format;
		
		Object admin = AuthorityUtil.buildRoleAuthority("ADMIN_VESPERS");
		
		if(!SecurityUtil.hasAuthority(admin)) {
			errors.reject("permission.denied", "Not permitted to set beamline information.");
			return responseView;
		}
		
		HashMap<String,String> values = new HashMap<String,String>();
		
		if(generalCommentSP != null) {
			values.put(VALUE_KEY_GENERAL_COMMENT, generalCommentSP);
		}
		
		if(spotSizeCommentSP != null) {
			values.put(VALUE_KEY_SPOT_SIZE_COMMENT, spotSizeCommentSP);
		}
		
		if(!values.isEmpty()) {
			beamlineInformationDeviceProxy.putAll(values);
		}

		return responseView;
	}

	public DeviceProxy getBeamlineInformationDeviceProxy() {
		return beamlineInformationDeviceProxy;
	}
	public void setBeamlineInformationDeviceProxy(DeviceProxy beamlineInformationDeviceProxy) {
		this.beamlineInformationDeviceProxy = beamlineInformationDeviceProxy;
	}	
}
