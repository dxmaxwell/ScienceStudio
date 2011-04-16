/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     UpdateSampleStageXYZController class.
 *     
 */
package ca.sciencestudio.vespers.service.controllers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.state.StateMap;

/**
 * @author medrand
 *
 */

public class UpdateSampleStageXYZController {

	private static final String SET_POINT_X_PARAMTER_KEY = "setPointX";
	private static final String SET_POINT_Y_PARAMTER_KEY = "setPointY";
	private static final String SET_POINT_Z_PARAMTER_KEY = "setPointZ";
	
	private static final String VALUE_KEY_PERSON_KEY = "personKey";
	
	private StateMap sampleStateXYZStateMap;
	private StateMap beamlineSessionStateMap;
	
	@RequestMapping(value = "/updateSampleStageXYZ.xml", method = RequestMethod.GET)
	public String handleRequest(HttpServletRequest request, ModelMap model) {
			
		String personKey = (String) beamlineSessionStateMap.get(VALUE_KEY_PERSON_KEY);
		
		if(!SecurityUtil.getUsername().equals(personKey)) {
			model.put("errors", "<error>You do not have permission to view this session.</error>");
			model.put("success", "false");
			return "response";	
		}
		
		Map<String,Serializable> fields = new HashMap<String,Serializable>();
		
		String setPointXParmeter = request.getParameter(SET_POINT_X_PARAMTER_KEY);
		String setPointYParmeter = request.getParameter(SET_POINT_Y_PARAMTER_KEY);
		String setPointZParmeter = request.getParameter(SET_POINT_Z_PARAMTER_KEY);
		
		double spX = 0;
		double spY = 0;
		double spZ = 0;
		
		try {
			spX = (setPointXParmeter != null) ? (spX = Double.parseDouble(setPointXParmeter)) : 0;
			fields.put(SET_POINT_X_PARAMTER_KEY, spX);
		} catch (NumberFormatException e) {
		}
		
		try {
			spY = (setPointYParmeter != null) ? (spY = Double.parseDouble(setPointYParmeter)) : 0;
			fields.put(SET_POINT_Y_PARAMTER_KEY, spY);
		} catch (NumberFormatException e) {
		}
		
		try {
			spZ = (setPointZParmeter != null) ? (spZ = Double.parseDouble(setPointZParmeter)) : 0;
			fields.put(SET_POINT_Z_PARAMTER_KEY, spZ);
		} catch (NumberFormatException e) {
		}
		
		
		if(fields.isEmpty()) {
			model.put("success", false);
		} else {
			sampleStateXYZStateMap.putAll(fields);
			model.put("success", true);	
		}
		return "response";
	}

	public void setSampleStateXYZStateMap(StateMap sampleStateXYZStateMap) {
		this.sampleStateXYZStateMap = sampleStateXYZStateMap;
	}

	public void setBeamlineSessionStateMap(StateMap beamlineSessionStateMap) {
		this.beamlineSessionStateMap = beamlineSessionStateMap;
	}
}
