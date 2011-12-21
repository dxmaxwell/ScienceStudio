/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *	Description:
 *		 CCDFocusController class.
 *
 */
package ca.sciencestudio.vespers.service.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.util.state.StateMap;
import ca.sciencestudio.util.web.FormResponseMap;

/**
 * the controller to start or stop CCD focus.
 * 
 * @author Dong Liu
 * 
 */

@Controller
public class CCDFocusController extends AbstractBeamlineAuthzController {

	private static final String PARAM_ACQUIRE = "acquire";
	private static final String ACQUIRE_START = "start";
	private static final String ACQUIRE_STOP = "stop";
	private static final String DATE_FORMAT = "yyMMddHHmmssS";

	private static final String VALUE_KEY_MODE = "xrdMode";
	private static final String VALUE_KEY_ACQUIRE = "acquire";
	private static final String VALUE_KEY_FILEPATH = "filePath";
	private static final String VALUE_KEY_FILENAME = "fileName";
	private static final String VALUE_KEY_FILETEMPLATE = "fileTemplate";

	private String filePath;
	private String templateFocus;

	@Autowired
	private StateMap ccdCollectionProxy;
	@Autowired
	private StateMap ccdFileProxy;

	@ResponseBody
	@RequestMapping(value = "/ccdfocus", method = RequestMethod.POST)
	public FormResponseMap handleRequest(@RequestParam(PARAM_ACQUIRE) String acquireParam, HttpServletResponse response) throws IOException {

		if (!canWriteBeamline()) {
			response.setStatus(HttpStatus.UNAUTHORIZED_401);
			return new FormResponseMap(false, "Not permitted to setup CCD.");
		}

		if (((String) beamlineSessionProxy.get(VALUE_KEY_MODE)).equals("scan")) {
			response.setStatus(HttpStatus.CONFLICT_409);
			return new FormResponseMap(false, "wrong XRD mode.");
		}

		if (acquireParam != null) {
			if (acquireParam.equals(ACQUIRE_START)) {
				// set file path and name
				Map<String, Serializable> values = new HashMap<String, Serializable>();
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
				String date = dateFormat.format(cal.getTime());
				values.put(VALUE_KEY_FILEPATH, filePath);
				values.put(VALUE_KEY_FILETEMPLATE, templateFocus);
				values.put(VALUE_KEY_FILENAME, date);
				ccdFileProxy.putAll(values);
				ccdCollectionProxy.put(VALUE_KEY_ACQUIRE, new Integer(1));
			} else if (acquireParam.equals(ACQUIRE_STOP)) {
				ccdCollectionProxy.put(VALUE_KEY_ACQUIRE, new Integer(0));
			} else {
				response.setStatus(HttpStatus.BAD_REQUEST_400);
				return new FormResponseMap(false, "Wrong parameter value.");
			}
		} else {
			response.setStatus(HttpStatus.BAD_REQUEST_400);
			return new FormResponseMap(false, "Parameter not found.");
		}

		return new FormResponseMap(true, "Set focus state.");

	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void setTemplateFocus(String templateFocus) {
		this.templateFocus = templateFocus;
	}

}
