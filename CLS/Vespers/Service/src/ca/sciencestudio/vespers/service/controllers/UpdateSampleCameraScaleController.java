/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     UpdateSampleImageScaleController class.
 *     
 */
package ca.sciencestudio.vespers.service.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.util.authz.Authorities;
import ca.sciencestudio.util.state.StateMap;
import ca.sciencestudio.util.web.FormResponseMap;

/**
 * @author maxweld
 *
 */
@Controller
public class UpdateSampleCameraScaleController extends AbstractBeamlineAuthzController {
		
	private static final String VALUE_KEY_SCALE = "scale";
	
	private StateMap sampleCameraProxy;
	
	@ResponseBody
	@RequestMapping(value = "/sample/camera/scale*", method = RequestMethod.POST)
	public FormResponseMap handleRequest(@RequestParam(required = false) String scaleSP) {
		
		// User must be controller and VESPERS administrator to update Sample Camera Scale. //
		
		if(!canWriteBeamline()) {
			return new FormResponseMap(false, "Not permitted to set sample image scale. (1)");
		}
		
		Authorities authorities = getAuthorities();
		if((authorities == null) || !authorities.containsAny(FACILITY_ADMIN_VESPERS)) {
			return new FormResponseMap(false, "Not permitted to set sample image scale. (1)");
		}
		
		if(scaleSP != null) {
			try {
				sampleCameraProxy.put(VALUE_KEY_SCALE, Double.parseDouble(scaleSP));
			}
			catch(NumberFormatException e) { /* Nothing To Do */ }
		}
		
		return new FormResponseMap(true);
	}

	public StateMap getSampleCameraProxy() {
		return sampleCameraProxy;
	}
	public void setSampleCameraProxy(StateMap sampleCameraProxy) {
		this.sampleCameraProxy = sampleCameraProxy;
	}
}
