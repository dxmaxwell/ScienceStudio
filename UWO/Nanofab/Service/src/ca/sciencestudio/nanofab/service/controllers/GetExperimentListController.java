/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		GetSessionExperimentListController class.
 *     
 */
package ca.sciencestudio.nanofab.service.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
public class GetExperimentListController extends AbstractLaboratoryAuthzController {
	
	private ExperimentAuthzDAO experimentAuthzDAO;
	
	@ResponseBody
	@RequestMapping(value = "/experiments*")
	public FormResponseMap getExperimentList() {
		
		if(!canReadLaboratory()) {
			return new FormResponseMap(false, "Not permitted to view experiment list.");
		}
		
		String user = SecurityUtil.getPersonGid();
		
		String sessionGid = nanofabSessionStateMap.getSessionGid();
		
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
