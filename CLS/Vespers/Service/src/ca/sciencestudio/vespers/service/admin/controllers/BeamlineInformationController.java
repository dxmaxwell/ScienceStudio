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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.device.proxy.DeviceProxy;
import ca.sciencestudio.util.web.FormResponseMap;

/**
 * @author maxweld
 *
 */
@Controller
public class BeamlineInformationController extends AbstractBeamlineAdminController {

	private static final String VALUE_KEY_GENERAL_COMMENT = "generalComment";
	private static final String VALUE_KEY_SPOT_SIZE_COMMENT = "spotSizeComment";
	
	private DeviceProxy beamlineInformationDeviceProxy;
	
	@ResponseBody
	@RequestMapping(value = "/information*", method = RequestMethod.POST)
	public FormResponseMap postInformationEdit(@RequestParam(required = false) String generalCommentSP,
												@RequestParam(required = false) String spotSizeCommentSP) {
		
		if(!canAdminBeamline()) {
			return new FormResponseMap(false, "Not permitted to set beamline information.");
		}
				
		HashMap<String,String> values = new HashMap<String,String>();
		
		if((generalCommentSP != null) && (generalCommentSP.trim().length() > 0)) {
			values.put(VALUE_KEY_GENERAL_COMMENT, generalCommentSP);
		}
		
		if((spotSizeCommentSP != null) && (spotSizeCommentSP.trim().length() > 0)) {
			values.put(VALUE_KEY_SPOT_SIZE_COMMENT, spotSizeCommentSP);
		}
		
		if(!values.isEmpty()) {
			beamlineInformationDeviceProxy.putAll(values);
		}

		return new FormResponseMap(true);
	}

	public DeviceProxy getBeamlineInformationDeviceProxy() {
		return beamlineInformationDeviceProxy;
	}
	public void setBeamlineInformationDeviceProxy(DeviceProxy beamlineInformationDeviceProxy) {
		this.beamlineInformationDeviceProxy = beamlineInformationDeviceProxy;
	}	
}
