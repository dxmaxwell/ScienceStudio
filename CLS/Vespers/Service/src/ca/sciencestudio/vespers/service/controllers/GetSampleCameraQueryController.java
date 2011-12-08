/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		GetSampleCameraCheckController class.
 *     
 */
package ca.sciencestudio.vespers.service.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.util.state.StateMap;
import ca.sciencestudio.util.web.FormResponseMap;

/**
 * @author maxweld
 *
 */
@Controller
public class GetSampleCameraQueryController extends AbstractBeamlineAuthzController {
	
	private StateMap sampleCameraProxy;
	
	@ResponseBody
	@RequestMapping(value = "/sample/camera/query*", method = RequestMethod.POST)
	public FormResponseMap handleRequest(@RequestParam long timestamp, ModelMap model) {
		
		if(!canReadBeamline()) {
			return new FormResponseMap(false, "Not permitted to query sample camera.");
		}
				
		FormResponseMap response = new FormResponseMap(true);
		
		long deviceTimestamp = sampleCameraProxy.getTimestamp().getTime();
		if(deviceTimestamp > timestamp) {
			response.put("timestamp", deviceTimestamp);
		} else {
			response.put("timestamp", timestamp);
		}
		
		return response;
	}

	public StateMap getSampleCameraProxy() {
		return sampleCameraProxy;
	}
	public void setSampleCameraProxy(StateMap sampleCameraProxy) {
		this.sampleCameraProxy = sampleCameraProxy;
	}
}
