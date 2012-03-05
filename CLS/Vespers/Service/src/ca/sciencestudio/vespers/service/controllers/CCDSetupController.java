/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *	Description:
 *		CCDSetupController class.
 *
 */
package ca.sciencestudio.vespers.service.controllers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.util.log.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.util.state.StateMap;
import ca.sciencestudio.util.web.FormResponseMap;

/**
 * the controller to setting CCD ROI options.
 * 
 * @author Dong Liu
 * 
 */
@Controller
public class CCDSetupController extends AbstractBeamlineAuthzController {

	private static final String PARAM_binX = "binX";
	private static final String PARAM_binY = "binY";
	private static final String PARAM_startX = "startX";
	private static final String PARAM_startY = "startY";
	private static final String PARAM_sizeX = "sizeX";
	private static final String PARAM_sizeY = "sizeY";
	private static final String VALUE_KEY_binX = "binX";
	private static final String VALUE_KEY_binY = "binY";
	private static final String VALUE_KEY_startX = "regionStartX";
	private static final String VALUE_KEY_startY = "regionStartY";
	private static final String VALUE_KEY_sizeX = "regionSizeX";
	private static final String VALUE_KEY_sizeY = "regionSizeY";

	private StateMap ccdSetupProxy;

	@ResponseBody
	@RequestMapping(value = "/ccdsetup*", method = RequestMethod.POST)
	public FormResponseMap handleRequest(@RequestParam(value = PARAM_binX) String binX, @RequestParam(value = PARAM_binY) String binY, @RequestParam(value = PARAM_startX) String startX, @RequestParam(value = PARAM_startY) String startY,
			@RequestParam(value = PARAM_sizeX) String sizeX, @RequestParam(value = PARAM_sizeY) String sizeY, HttpServletResponse response) {

		if (!canWriteBeamline()) {
			response.setStatus(HttpStatus.UNAUTHORIZED_401);
			return new FormResponseMap(false, "Not permitted to setup CCD.");
		}

		Map<String, Serializable> values = new HashMap<String, Serializable>();

		try {
			Integer value = Integer.valueOf(binX);
			if (value.intValue() >= 1)
				values.put(VALUE_KEY_binX, value);
		} catch (NumberFormatException e) {
			Log.warn("The " + PARAM_binX + " is not a valid parameter.");
		}

		try {
			Integer value = Integer.valueOf(binY);
			if (value.intValue() >= 1)
				values.put(VALUE_KEY_binY, value);
		} catch (NumberFormatException e) {
			Log.warn("The " + PARAM_binY + " is not a valid parameter.");
		}

		try {
			Integer value = Integer.valueOf(startX);
			if (value.intValue() >= 0)
				values.put(VALUE_KEY_startX, value);
		} catch (NumberFormatException e) {
			Log.warn("The " + PARAM_startX + " is not a valid parameter.");
		}

		try {
			Integer value = Integer.valueOf(startY);
			if (value.intValue() >= 0)
				values.put(VALUE_KEY_startY, value);
		} catch (NumberFormatException e) {
			Log.warn("The " + PARAM_startY + " is not a valid parameter.");
		}

		try {
			Integer value = Integer.valueOf(sizeX);
			if (value.intValue() >= 0)
				values.put(VALUE_KEY_sizeX, value);
		} catch (NumberFormatException e) {
			Log.warn("The " + PARAM_sizeX + " is not a valid parameter.");
		}

		try {
			Integer value = Integer.valueOf(sizeY);
			if (value.intValue() >= 0)
				values.put(VALUE_KEY_sizeY, value);
		} catch (NumberFormatException e) {
			Log.warn("The " + PARAM_sizeY + " is not a valid parameter.");
		}

		if (!values.isEmpty()) {
			ccdSetupProxy.putAll(values);
			return new FormResponseMap(true, "CCD setup changed.");
		}
		
		response.setStatus(HttpStatus.BAD_REQUEST_400);
		return new FormResponseMap(false, "Exposure time parameter wrong.");
	}

	public void setCcdSetupProxy(StateMap ccdSetupProxy) {
		this.ccdSetupProxy = ccdSetupProxy;
	}
}
