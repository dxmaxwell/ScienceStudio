/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		GetSampleCameraController class.
 *     
 */
package ca.sciencestudio.vespers.service.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.sciencestudio.util.state.StateMap;

/**
 * @author maxweld
 *
 */
@Controller
public class GetSampleCameraImageController extends AbstractBeamlineAuthzController {
	
	private static final String VALUE_KEY_SAMPLE_IMAGE = "image";
	
	private StateMap sampleCameraProxy;
	private String sampleNotAvailableImageUrl;

	@RequestMapping(value = "/sample/camera/image.png", method = RequestMethod.GET)
	public String handleRequest(HttpServletResponse response) {
		
		if(!canReadBeamline()) {
			return "redirect:" + sampleNotAvailableImageUrl;
		}
		
		Object image = sampleCameraProxy.get(VALUE_KEY_SAMPLE_IMAGE);
		
		if(!(image instanceof byte[])) {
			return "redirect:" + sampleNotAvailableImageUrl;
		}
		
		try {
			byte[] body = (byte[]) image;
			response.setContentType("image/png");
			response.setContentLength(body.length);
			response.getOutputStream().write(body);
		}
		catch(IOException e) {
			return "redirect:" + sampleNotAvailableImageUrl;
		}
		
		return null;
	}

	public StateMap getSampleCameraProxy() {
		return sampleCameraProxy;
	}
	public void setSampleCameraProxy(StateMap sampleCameraProxy) {
		this.sampleCameraProxy = sampleCameraProxy;
	}

	public String getSampleNotAvailableImageUrl() {
		return sampleNotAvailableImageUrl;
	}
	public void setSampleNotAvailableImageUrl(String sampleNotAvailableImageUrl) {
		this.sampleNotAvailableImageUrl = sampleNotAvailableImageUrl;
	}
}
