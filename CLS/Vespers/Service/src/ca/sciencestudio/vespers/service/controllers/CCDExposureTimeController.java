/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *	Description:
 *		CCDExposureTimeController class.
 */
package ca.sciencestudio.vespers.service.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.state.StateMap;

/**
 * the controller to set CCD exposure time
 *
 * @author Dong Liu
 *
 */
public class CCDExposureTimeController implements Controller {

	private static final String PARAM_ET = "exposureTime";
	private static final String VALUE_KEY_PERSON_KEY = "personKey";
	private static final String VALUE_KEY_EXPOSURETIME = "exposureTime";

	private StateMap beamlineSessionStateMap;
	private StateMap ccdCollectionStateMap;

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

		String timeParam = request.getParameter(PARAM_ET);
		if(timeParam != null) {
			try {
				double time = Double.parseDouble(timeParam);
				ccdCollectionStateMap.put(VALUE_KEY_EXPOSURETIME , time);
			}
			catch(NumberFormatException e) {
				model.put("success", false);
				return new ModelAndView("response", model);
			}
		} else {
			model.put("success", false);
			model.put("errors", "<error>No parameter named exposureTime.</error>");
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

}
