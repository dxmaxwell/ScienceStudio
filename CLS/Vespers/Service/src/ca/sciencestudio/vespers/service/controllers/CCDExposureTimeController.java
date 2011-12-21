/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *	Description:
 *		CCDExposureTimeController class.
 */
package ca.sciencestudio.vespers.service.controllers;

import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.util.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.util.state.StateMap;
import ca.sciencestudio.util.web.FormResponseMap;

/**
 * the controller to set CCD exposure time
 * 
 * @author Dong Liu
 * 
 */

@Controller
public class CCDExposureTimeController extends AbstractBeamlineAuthzController {

	private static final String PARAM_ET = "exposureTime";
	private static final String VALUE_KEY_EXPOSURETIME = "exposureTime";

	@Autowired
	private StateMap ccdCollectionProxy;

	@ResponseBody
	@RequestMapping(value = "/ccdexposuretime", method = RequestMethod.POST)
	public FormResponseMap handleRequest(@RequestParam(PARAM_ET) String time, HttpServletResponse response) {

		if (!canWriteBeamline()) {
			response.setStatus(HttpStatus.UNAUTHORIZED_401);
			return new FormResponseMap(false, "Not permitted to setup CCD.");
		}

		try {
			ccdCollectionProxy.put(VALUE_KEY_EXPOSURETIME, Double.parseDouble(time));
			return new FormResponseMap(true, "Set exposure time to " + Double.parseDouble(time));
		} catch (NumberFormatException e) {
			Log.warn("The time user input is not a valid double.");
		}

		response.setStatus(HttpStatus.BAD_REQUEST_400);
		return new FormResponseMap(false, "Exposure time parameter wrong.");

	}

}
