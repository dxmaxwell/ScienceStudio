/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *	Description:
 *		CCDFileController class.
 */
package ca.sciencestudio.vespers.service.controllers;

import java.io.IOException;
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
 * The controller to set XRD file related PV's. They are set from the form only
 * in scan mode.
 * 
 * @author Dong Liu
 * 
 */
@Controller
public class CCDFileController extends AbstractBeamlineAuthzController {

	private static final String PARAM_KEY_FILE_NUMBER = "fileNumber";
	private static final String PARAM_KEY_FILE_NAME = "fileName";

	private static final String VALUE_KEY_XRD_MODE = "xrdMode";

	private static final String VALUE_KEY_FILENAME = "fileName";
	private static final String VALUE_KEY_FILENUMBER = "fileNumber";

	private static final String MODE_SCAN = "scan";
	
	private StateMap ccdCollectionProxy;

	@ResponseBody
	@RequestMapping(value = "/ccdfile", method = RequestMethod.POST)
	public FormResponseMap handleRequest(@RequestParam(PARAM_KEY_FILE_NUMBER) String fileNum, @RequestParam(PARAM_KEY_FILE_NAME) String fileName, HttpServletResponse response) throws IOException {

		if (!canWriteBeamline()) {
			response.setStatus(HttpStatus.UNAUTHORIZED_401);
			return new FormResponseMap(false, "Not permitted to setup CCD.");
		}

		String mode = (String) beamlineSessionProxy.get(VALUE_KEY_XRD_MODE);

		if (mode == null || !mode.equals(MODE_SCAN)) {
			response.setStatus(HttpStatus.CONFLICT_409);
			return new FormResponseMap(false, "wrong XRD mode.");
		}

		Map<String, Serializable> values = new HashMap<String, Serializable>();

		try {
			values.put(VALUE_KEY_FILENUMBER, Integer.valueOf(fileNum));
		} catch (NumberFormatException e) {
			Log.warn("The file numbner is not a valid integer.");
		}
		
		if (fileNum != null) {
			values.put(VALUE_KEY_FILENAME, fileName);
		}

		if (!values.isEmpty()) {
			ccdCollectionProxy.putAll(values);
			return new FormResponseMap(true, "CCD file name and/or number changed.");
		}

		response.setStatus(HttpStatus.BAD_REQUEST_400);
		return new FormResponseMap(false, "Parameter wrong.");
	}

	public void setCcdCollectionProxy(StateMap ccdCollectionProxy) {
		this.ccdCollectionProxy = ccdCollectionProxy;
	}
}
