/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *	Description:
 *		CCDSetupController class.
 *
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
 * the controller to setting CCD ROI options.
 *
 * @author Dong Liu
 *
 */
public class CCDSetupController implements Controller {

	private static final String PARAM_binX = "binX";
	private static final String PARAM_binY = "binY";
	private static final String PARAM_startX = "startX";
	private static final String PARAM_startY = "startY";
	private static final String PARAM_sizeX = "sizeX";
	private static final String PARAM_sizeY = "sizeY";
	private static final String VALUE_KEY_PERSON_KEY = "personKey";
	private static final String VALUE_KEY_binX = "binX";
	private static final String VALUE_KEY_binY = "binY";
	private static final String VALUE_KEY_startX = "regionStartX";
	private static final String VALUE_KEY_startY = "regionStartY";
	private static final String VALUE_KEY_sizeX = "regionSizeX";
	private static final String VALUE_KEY_sizeY = "regionSizeY";

	private StateMap beamlineSessionStateMap;
	private StateMap ccdSetupStateMap;

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

		String binXParam = request.getParameter(PARAM_binX);
		String binYParam = request.getParameter(PARAM_binY);
		String startXParam = request.getParameter(PARAM_startX);
		String startYParam = request.getParameter(PARAM_startY);
		String sizeXParam = request.getParameter(PARAM_sizeX);
		String sizeYParam = request.getParameter(PARAM_sizeY);
		if (binXParam == null && binYParam == null && startXParam == null && startYParam == null && sizeXParam == null && sizeYParam == null) {
			model.put("success", false);
			model.put("errors", "<error>No parameter can be parsed.</error>");
			return new ModelAndView("response", model);
		}
		if(binXParam != null && binXParam.length() > 0) {
			try {
				int binX = Integer.parseInt(binXParam);
				ccdSetupStateMap.put(VALUE_KEY_binX , binX);
			}
			catch(NumberFormatException e) {
				model.put("success", false);
				return new ModelAndView("response", model);
			}
		}
		if(binYParam != null && binYParam.length() > 0) {
			try {
				int binY = Integer.parseInt(binYParam);
				ccdSetupStateMap.put(VALUE_KEY_binY , binY);
			}
			catch(NumberFormatException e) {
				model.put("success", false);
				return new ModelAndView("response", model);
			}
		}
		if(startXParam != null && startXParam.length() > 0) {
			try {
				int startX = Integer.parseInt(startXParam);
				ccdSetupStateMap.put(VALUE_KEY_startX , startX);
			}
			catch(NumberFormatException e) {
				model.put("success", false);
				return new ModelAndView("response", model);
			}
		}
		if(startYParam != null && startYParam.length() > 0) {
			try {
				int startY = Integer.parseInt(startYParam);
				ccdSetupStateMap.put(VALUE_KEY_startY , startY);
			}
			catch(NumberFormatException e) {
				model.put("success", false);
				return new ModelAndView("response", model);
			}
		}
		if(sizeXParam != null && sizeXParam.length() > 0) {
			try {
				int sizeX = Integer.parseInt(binXParam);
				ccdSetupStateMap.put(VALUE_KEY_sizeX , sizeX);
			}
			catch(NumberFormatException e) {
				model.put("success", false);
				return new ModelAndView("response", model);
			}
		}
		if(sizeYParam != null && sizeYParam.length() > 0) {
			try {
				int sizeY = Integer.parseInt(sizeYParam);
				ccdSetupStateMap.put(VALUE_KEY_sizeY , sizeY);
			}
			catch(NumberFormatException e) {
				model.put("success", false);
				return new ModelAndView("response", model);
			}
		}

		model.put("success", true);
		return new ModelAndView("response", model);

	}

	public void setBeamlineSessionStateMap(StateMap beamlineSessionStateMap) {
		this.beamlineSessionStateMap = beamlineSessionStateMap;
	}


	public void setCcdSetupStateMap(StateMap ccdSetupStateMap) {
		this.ccdSetupStateMap = ccdSetupStateMap;
	}
}
