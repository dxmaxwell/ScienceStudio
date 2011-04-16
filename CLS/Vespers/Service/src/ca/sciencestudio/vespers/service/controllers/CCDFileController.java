/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *	Description:
 *		CCDFileController class.
 */
package ca.sciencestudio.vespers.service.controllers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.state.StateMap;

/**
 * 	The controller to set XRD file related PV's. They are set from the
 *  form only in scan mode.
 *
 * @author Dong Liu
 *
 */
public class CCDFileController implements Controller {

	private static final String PARAM_KEY_FILE_NUMBER = "fileNumber";
	private static final String PARAM_KEY_FILE_NAME = "fileName";

	private static final String VALUE_KEY_PERSON_KEY = "personKey";
	private static final String VALUE_KEY_XRD_MODE = "xrdMode";

	private static final String VALUE_KEY_FILENAME = "fileName";
	private static final String VALUE_KEY_FILENUMBER = "fileNumber";

	private static final String MODE_SCAN = "scan";

	private StateMap ccdFileStateMap;
	private StateMap beamlineSessionStateMap;

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map<String,Object> model = new HashMap<String,Object>();

		if (!request.getMethod().equalsIgnoreCase("POST")) {
			model.put("errors", "<error>Only POST is supported.</error>");
			model.put("success", "false");
			return new ModelAndView("response", model);
		}

		String personKey = (String) beamlineSessionStateMap.get(VALUE_KEY_PERSON_KEY);

		if(!SecurityUtil.getUsername().equals(personKey)) {
			model.put("errors", "<error>You do not have permission to view this session.</error>");
			model.put("success", "false");
			return new ModelAndView("response", model);
		}

		String mode = (String) beamlineSessionStateMap.get(VALUE_KEY_XRD_MODE);

		if (mode == null) {
			model.put("success", false);
			model.put("errors", "<error>XRD mode unknown</error>");
			return new ModelAndView("response", model);
		}

		if (!mode.equals(MODE_SCAN)) {
			model.put("success", false);
			model.put("errors", "<error>XRD mode wrong</error>");
			return new ModelAndView("response", model);
		}

		Map<String, Serializable> values = new HashMap<String, Serializable>();

		// some default values have to be set here

		String fileNum = request.getParameter(PARAM_KEY_FILE_NUMBER);
		if (fileNum != null && fileNum.length() > 0) {
			try {
				values.put(VALUE_KEY_FILENUMBER, Integer.parseInt(fileNum));
			} catch (NumberFormatException e) {
			}
		}

		String fileName = request.getParameter(PARAM_KEY_FILE_NAME);
		if (fileName != null && fileName.length() > 0) {
			values.put(VALUE_KEY_FILENAME, fileName);
		}

		ccdFileStateMap.putAll(values);

		model.put("success", true);
		return new ModelAndView("response", model);

	}

	public void setCcdFileStateMap(StateMap ccdFileStateMap) {
		this.ccdFileStateMap = ccdFileStateMap;
	}

	public void setBeamlineSessionStateMap(StateMap beamlineSessionStateMap) {
		this.beamlineSessionStateMap = beamlineSessionStateMap;
	}

}
