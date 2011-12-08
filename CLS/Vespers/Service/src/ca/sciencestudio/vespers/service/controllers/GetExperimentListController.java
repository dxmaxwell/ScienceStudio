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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.dao.ExperimentAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.web.FormResponseMap;

/**
 * @author maxweld
 *
 */
@Controller
public class GetExperimentListController extends AbstractBeamlineAuthzController {

	private static final String STATE_KEY_SESSION_GID = "sessionGid";
	
	private ExperimentAuthzDAO experimentAuthzDAO;
	
	@ResponseBody
	@RequestMapping(value = "/experiments*", method = RequestMethod.POST)
	public FormResponseMap requestHandler() {
		
		if(!canReadBeamline()) {
			return new FormResponseMap(false, "Not permitted to view experiment list.");
		}
		
		String user = SecurityUtil.getPersonGid();
		
		String sessionGid = (String) beamlineSessionProxy.get(STATE_KEY_SESSION_GID);
		if(sessionGid == null) {
			return new FormResponseMap(false, "Session GID not found.");
		}
		
		List<Experiment> experimentList = experimentAuthzDAO.getAllBySessionGid(user, sessionGid).get();
		if(experimentList == null) {
			return new FormResponseMap(false, "Experiments not found.");
		}
		
		FormResponseMap response = new FormResponseMap(true);
		response.put("experiments", experimentList);
		return response;
	}

	public ExperimentAuthzDAO getExperimentAuthzDAO() {
		return experimentAuthzDAO;
	}
	public void setExperimentAuthzDAO(ExperimentAuthzDAO experimentAuthzDAO) {
		this.experimentAuthzDAO = experimentAuthzDAO;
	}
}
