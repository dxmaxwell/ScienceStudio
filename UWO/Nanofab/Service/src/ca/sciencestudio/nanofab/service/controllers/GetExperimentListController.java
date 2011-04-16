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
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.dao.ExperimentDAO;
import ca.sciencestudio.nanofab.state.NanofabSessionStateMap;
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.web.BindAndValidateUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class GetExperimentListController {
	
	private ExperimentDAO experimentDAO;
	private NanofabSessionStateMap nanofabSessionStateMap;
	
	@RequestMapping(value = "/experiments.{format}")
	public String getExperimentList(@PathVariable String format, ModelMap model) {
		
		BindException errors = BindAndValidateUtils.buildBindException();
		model.put("errors", errors);
		
		String responseView = "response-" + format;
		
		int projectId = nanofabSessionStateMap.getProjectId();
		
		Object admin = AuthorityUtil.buildRoleAuthority("ADMIN_NANOFAB");
		Object group = AuthorityUtil.buildProjectGroupAuthority(projectId);
		
		if(!SecurityUtil.hasAnyAuthority(group, admin)) {
			errors.reject("permission.denied", "Not permitted to view experiment list.");
			return responseView;
		}
		
		int sessionId = nanofabSessionStateMap.getSessionId();
		
		List<Experiment> experimentList = experimentDAO.getExperimentListBySessionId(sessionId);
		if(experimentList == null) {
			errors.reject("experiment.notfound", "Experiments not found.");
			return responseView;
		}
		
		model.put("response", experimentList);
		return responseView;
	}

	public ExperimentDAO getExperimentDAO() {
		return experimentDAO;
	}
	public void setExperimentDAO(ExperimentDAO experimentDAO) {
		this.experimentDAO = experimentDAO;
	}

	public NanofabSessionStateMap getNanofabSessionStateMap() {
		return nanofabSessionStateMap;
	}
	public void setNanofabSessionStateMap(NanofabSessionStateMap nanofabSessionStateMap) {
		this.nanofabSessionStateMap = nanofabSessionStateMap;
	}
}
