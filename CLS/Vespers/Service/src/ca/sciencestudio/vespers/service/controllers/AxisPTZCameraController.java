/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		AxisPTZCameraController class.
 *     
 */
package ca.sciencestudio.vespers.service.controllers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Collection;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.util.web.FormResponseMap;
import ca.sciencestudio.vespers.device.camera.axis.AxisHttpPTZCamera;

/**
 * @author maxweld
 *
 */
@Controller
public class AxisPTZCameraController extends AbstractBeamlineAuthzController {
	
	private String cameraNotAvailableImageUrl;
	private Collection<AxisHttpPTZCamera> axisHttpPTZCameras;
	
	protected Log logger = LogFactory.getLog(getClass());
	
	@ResponseBody
	@RequestMapping(value = "/camera/{name}/control*", method = RequestMethod.POST)
	public FormResponseMap control(@RequestParam(required = false) String rcenter,
									@RequestParam(required = false) String rzoom,
										@RequestParam(required = false) String rfocus,
											@RequestParam(required = false) String riris,
																@PathVariable String name) {
		
		if(!canWriteBeamline()) {
			return new FormResponseMap(false, "Not permitted to control camera.");
		}
		
		AxisHttpPTZCamera axisPTZCamera = findCameraByName(name);
		if(axisPTZCamera == null) {
			return new FormResponseMap(false, "Camera, '" + name + "', not found.");
		}
		
		if(rcenter != null) {
			try {
				String[] centerRelative = rcenter.split(",");					
				if(centerRelative.length < 2) { throw new NumberFormatException(); }
				int centerRelativeX = Integer.parseInt(centerRelative[0]);
				int centerRelativeY = Integer.parseInt(centerRelative[1]);
				axisPTZCamera.setCenterRelative(centerRelativeX, centerRelativeY);
			}
			catch(NumberFormatException e) {
				return new FormResponseMap(false, "Camera move location invalid (" + rcenter + ").");
			}
		}
		
		if(rzoom != null) {
			try {
				int zoomRelative = Integer.parseInt(rzoom);
				axisPTZCamera.setZoomRelative(zoomRelative);
			}
			catch(NumberFormatException e) {
				return new FormResponseMap(false, "Camera zoom factor invalid (" + rzoom + ").");
			}
		}
		
		if(rfocus != null) {
			try {
				int focusRelative = Integer.parseInt(rfocus);
				axisPTZCamera.setFocusRelative(focusRelative);
			}
			catch(NumberFormatException e) {
				return new FormResponseMap(false, "Camera focus parameter invalid (" + rfocus + ").");
			}
		}
		
		if(riris != null) {
			try {
				int irisRelative = Integer.parseInt(riris);
				axisPTZCamera.setIrisRelative(irisRelative);
			}
			catch(NumberFormatException e) {
				return new FormResponseMap(false, "Camera iris parameter invalid (" + riris + ").");
			}
		}
		
		return new FormResponseMap(true);
	}
	
	@RequestMapping(value = "/camera/{name}/image.jpg", method = RequestMethod.GET)
	public String image(@PathVariable String name, HttpServletResponse response) {
		
		if(!canReadBeamline()) {
			return "redirect:" + cameraNotAvailableImageUrl;
		}
		
		AxisHttpPTZCamera axisPTZCamera = findCameraByName(name);
		if(axisPTZCamera == null) {
			return "redirect:" + cameraNotAvailableImageUrl;
		}
		
		try {
			ImageIO.write(axisPTZCamera.getImage(), "jpg", response.getOutputStream());
		}
		catch(IOException e) {
			// Ignore, data has already been sent. //
		}
		
		return null;
	}
	
	@RequestMapping(value = "/camera/{name}/video.mjpg", method = RequestMethod.GET)
	public String video(@PathVariable String name, HttpServletResponse response) {

		if(!canReadBeamline()) {
			return "redirect:" + cameraNotAvailableImageUrl;
		}
		
		AxisHttpPTZCamera axisPTZCamera = findCameraByName(name);
		if(axisPTZCamera == null) {
			return "redirect:" + cameraNotAvailableImageUrl;
		}
		
		URLConnection urlConnection = axisPTZCamera.getVideo();
		if(urlConnection == null) {
			logger.warn("Unable to connect to streaming video. (" + axisPTZCamera.getBaseUrl()+ ")");
			return "redirect:" + cameraNotAvailableImageUrl;
		}
		
		response.setContentLength(urlConnection.getContentLength());
		response.setContentType(urlConnection.getContentType());
		
		BufferedInputStream inStream;
		BufferedOutputStream outStream;
		try {
			inStream = new BufferedInputStream(urlConnection.getInputStream());
			outStream = new BufferedOutputStream(response.getOutputStream());
		}
		catch(IOException e) {
			return "redirect:" + cameraNotAvailableImageUrl;
		}
		
		try {
			IOUtils.copy(inStream, outStream);		
			outStream.flush();
			outStream.close();
		}
		catch (IOException e) {
			// Ignore, data has already been sent. //
		}
		
		return null;
	}
	
	protected AxisHttpPTZCamera findCameraByName(String name) {
		for(AxisHttpPTZCamera axisHttpPTZCamera : axisHttpPTZCameras) {
			if(axisHttpPTZCamera.getName().equals(name)) {
				return axisHttpPTZCamera;
			}	
		}
		return null;
	}

	public String getCameraNotAvailableImageUrl() {
		return cameraNotAvailableImageUrl;
	}
	public void setCameraNotAvailableImageUrl(String cameraNotAvailableImageUrl) {
		this.cameraNotAvailableImageUrl = cameraNotAvailableImageUrl;
	}

	public Collection<AxisHttpPTZCamera> getAxisHttpPTZCameras() {
		return axisHttpPTZCameras;
	}
	public void setAxisHttpPTZCameras(
			Collection<AxisHttpPTZCamera> axisHttpPTZCameras) {
		this.axisHttpPTZCameras = axisHttpPTZCameras;
	}
}
