/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     GetExperimentListController class.
 *     
 */
package ca.sciencestudio.vespers.service.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.dao.ExperimentDAO;
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.state.StateMap;
import ca.sciencestudio.util.web.BindAndValidateUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class GetExperimentListController {

	private static final String STATE_KEY_PROJECT_ID = "projectId";
	private static final String STATE_KEY_SESSION_ID = "sessionId";
	
	private ExperimentDAO experimentDAO;
	private StateMap beamlineSessionStateMap;
	
	@RequestMapping(value = "/experiments.{format}", method = RequestMethod.POST)
	public String requestHandler(@PathVariable String format, ModelMap model) {

		BindException errors = BindAndValidateUtils.buildBindException();
		model.put("errors", errors);
		
		String responseView = "response-" + format;
		
		Integer projectId = (Integer) beamlineSessionStateMap.get(STATE_KEY_PROJECT_ID);
		if(projectId == null) { projectId = new Integer(0); }
		
		Object admin = AuthorityUtil.buildRoleAuthority("ADMIN_VESPERS");
		Object group = AuthorityUtil.buildProjectGroupAuthority(projectId);
		
		if(!SecurityUtil.hasAnyAuthority(group, admin)) {
			errors.reject("permission.denied", "Not permitted to view experiment list.");
			return responseView;
		}
		
		Integer sessionId = (Integer) beamlineSessionStateMap.get(STATE_KEY_SESSION_ID);
		if(sessionId == null) { sessionId = new Integer(0); }
		
		List<Experiment> experimentList = experimentDAO.getExperimentListBySessionId(sessionId);
		if(experimentList == null) {
			errors.reject("experiment.notfound", "Experiments not found.");
			return responseView;
		}
		
		model.put("response", experimentList);
		return "response-" + format;
	}

	public ExperimentDAO getExperimentDAO() {
		return experimentDAO;
	}
	public void setExperimentDAO(ExperimentDAO experimentDAO) {
		this.experimentDAO = experimentDAO;
	}

	public StateMap getBeamlineSessionStateMap() {
		return beamlineSessionStateMap;
	}
	public void setBeamlineSessionStateMap(StateMap beamlineSessionStateMap) {
		this.beamlineSessionStateMap = beamlineSessionStateMap;
	}
}
