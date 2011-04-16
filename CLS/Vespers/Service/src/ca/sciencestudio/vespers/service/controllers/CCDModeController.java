/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *	Description:
 *		CCDModeController class.
 *
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
 * The controller to switch CCD mode
 * <p> The file pv's are set to some default values when the mode is switched. The user still has the freedom the modify them further though.
 * </p>
 *
 * @author Dong Liu
 *
 */
public class CCDModeController implements Controller {

	private static final String PARAM_MODE = "mode";
	private static final String VALUE_KEY_PERSON_KEY = "personKey";
	private static final String VALUE_KEY_MODE = "xrdMode";


	private StateMap beamlineSessionStateMap;
	private StateMap ccdCollectionStateMap;
	private StateMap ccdFileStateMap;

	private String templateScan;



	private static final String VALUE_KEY_TRIGGERMODE = "triggerMode";
//	private static final String VALUE_KEY_FILEPATH = "filePath";
	private static final String VALUE_KEY_FILENAME = "fileName";
	private static final String VALUE_KEY_FILENUMBER = "fileNumber";
	private static final String VALUE_KEY_FILETEMPLATE = "fileTemplate";
	public static final String VALUE_KEY_AUTOINCREMENT = "autoIncrement";

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

		String modeParam = request.getParameter(PARAM_MODE);

		if (modeParam == null) {
			model.put("success", false);
			model.put("errors", "<error>No parameter named mode.</error>");
			return new ModelAndView("response", model);
		}

		if (modeParam.equals("scan")) {
			beamlineSessionStateMap.put(VALUE_KEY_MODE, modeParam);
			// TODO this needs to be changed if the user is able to use the external trigger mode
			ccdCollectionStateMap.put(VALUE_KEY_TRIGGERMODE, 0); // free run

			// set some file pv's. The user can change them from the file form.
			Map<String,Serializable> values = new HashMap<String,Serializable>();
			values.put(VALUE_KEY_FILENAME, "scan");
			values.put(VALUE_KEY_FILENUMBER, new Integer(1));
			values.put(VALUE_KEY_FILETEMPLATE, templateScan);
			values.put(VALUE_KEY_AUTOINCREMENT, new Short((short) 1));
			ccdFileStateMap.putAll(values);

		}
		else if (modeParam.equals("focus")) {
			beamlineSessionStateMap.put(VALUE_KEY_MODE, modeParam);
			ccdCollectionStateMap.put(VALUE_KEY_TRIGGERMODE, 0); // free run
		}
		else {
			model.put("success", false);
			model.put("errors", "<error>Unknown mode.</error>");
			return new ModelAndView("response", model);
		}


		model.put("success", true);
		return new ModelAndView("response", model);

	}

	public void setBeamlineSessionStateMap(StateMap beamlineSessionStateMap) {
		this.beamlineSessionStateMap = beamlineSessionStateMap;
	}

	public void setCcdCollectionStateMap(StateMap ccdCollectionStateMap) {
		this.ccdCollectionStateMap = ccdCollectionStateMap;
	}

	public void setCcdFileStateMap(StateMap ccdFileStateMap) {
		this.ccdFileStateMap = ccdFileStateMap;
	}

	public void setTemplateScan(String templateScan) {
		this.templateScan = templateScan;
	}

}
