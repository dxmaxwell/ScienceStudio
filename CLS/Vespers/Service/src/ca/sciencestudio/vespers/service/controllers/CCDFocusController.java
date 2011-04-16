/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *	Description:
 *		 CCDFocusController class.
 *
 */
package ca.sciencestudio.vespers.service.controllers;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.state.StateMap;

/**
 * the controller to start or stop CCD focus.
 *
 * @author Dong Liu
 *
 */
public class CCDFocusController implements Controller {

	private static final String PARAM_ACQUIRE = "acquire";
	private static final String ACQUIRE_START = "start";
	private static final String ACQUIRE_STOP = "stop";
	private static final String DATE_FORMAT = "yyMMddHHmmssS";

	private static final String VALUE_KEY_MODE = "xrdMode";
	private static final String VALUE_KEY_PERSON_KEY = "personKey";
	private static final String VALUE_KEY_ACQUIRE = "acquire";
	private static final String VALUE_KEY_TRIGGERMODE = "triggerMode";
	private static final String VALUE_KEY_FILEPATH = "filePath";
	private static final String VALUE_KEY_FILENAME = "fileName";
	private static final String VALUE_KEY_FILETEMPLATE = "fileTemplate";

	private String filePath;
	private String templateFocus;

	private StateMap beamlineSessionStateMap;
	private StateMap ccdCollectionStateMap;
	private StateMap ccdFileStateMap;

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

		if(((String)beamlineSessionStateMap.get(VALUE_KEY_MODE)).equals("scan")) {
			model.put("errors", "<error>Wrong mode.</error>");
			model.put("success", "false");
			return new ModelAndView("response", model);
		}

		String acquireParam = request.getParameter(PARAM_ACQUIRE);
		if(acquireParam != null) {
			if (acquireParam.equals(ACQUIRE_START)){
				// set file path and name
				Map<String,Serializable> values = new HashMap<String,Serializable>();
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
				String date = dateFormat.format(cal.getTime());
				values.put(VALUE_KEY_FILEPATH, filePath);
				values.put(VALUE_KEY_FILETEMPLATE, templateFocus);
				values.put(VALUE_KEY_FILENAME, date);
				ccdFileStateMap.putAll(values);



				Map<String,Serializable> focus = new HashMap<String,Serializable>();
				focus.put(VALUE_KEY_TRIGGERMODE, 0); // free run
				focus.put(VALUE_KEY_ACQUIRE, 1);

				ccdCollectionStateMap.putAll(focus);

			} else if (acquireParam.equals(ACQUIRE_STOP)) {
				ccdCollectionStateMap.put(VALUE_KEY_ACQUIRE, 0);
			} else {
				model.put("success", false);
				model.put("errors", "<error>Parameter value wrong.</error>");
				return new ModelAndView("response", model);
			}

		} else {
			model.put("success", false);
			model.put("errors", "<error>No parameter named acquire.</error>");
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


	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void setTemplateFocus(String templateFocus) {
		this.templateFocus = templateFocus;
	}

}
