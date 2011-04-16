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

import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.state.StateMap;

/**
 * @author maxweld
 *
 */
@Controller
public class GetSampleCameraImageController {
	
	private static final String VALUE_KEY_SAMPLE_IMAGE = "image";
	private static final String VALUE_KEY_PROJECT_ID = "projectId";
	
	private StateMap sampleCameraStateMap;
	private StateMap beamlineSessionStateMap;
	private String sampleNotAvailableImageUrl;

	@RequestMapping(value = "/sample/camera/image.png", method = RequestMethod.GET)
	public String handleRequest(HttpServletResponse response) {
		
		Integer projectId = (Integer) beamlineSessionStateMap.get(VALUE_KEY_PROJECT_ID);
		if(projectId == null) { projectId = new Integer(0); }
		
		Object admin = AuthorityUtil.buildRoleAuthority("ADMIN_VESPERS");
		Object group = AuthorityUtil.buildProjectGroupAuthority(projectId);
		
		if(!(SecurityUtil.hasAnyAuthority(group,admin))) {
			return "redirect:" + sampleNotAvailableImageUrl;
		}
		
		Object image = sampleCameraStateMap.get(VALUE_KEY_SAMPLE_IMAGE);
		
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

	public String getSampleNotAvailableImageUrl() {
		return sampleNotAvailableImageUrl;
	}
	public void setSampleNotAvailableImageUrl(String sampleNotAvailableImageUrl) {
		this.sampleNotAvailableImageUrl = sampleNotAvailableImageUrl;
	}
}
