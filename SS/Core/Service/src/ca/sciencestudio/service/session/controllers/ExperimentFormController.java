/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ExperimentFormController class.
 *     
 */
package ca.sciencestudio.service.session.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.session.dao.ExperimentAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.controllers.AbstractModelController;
import ca.sciencestudio.service.session.backers.ExperimentFormBacker;
import ca.sciencestudio.service.utilities.ModelPathUtils;
import ca.sciencestudio.util.exceptions.AuthorizationException;
import ca.sciencestudio.util.rest.AddResult;
import ca.sciencestudio.util.rest.EditResult;
import ca.sciencestudio.util.rest.RemoveResult;
import ca.sciencestudio.util.web.FormResponseMap;

/**
 * @author maxweld
 *
 */
@Controller
public class ExperimentFormController extends AbstractModelController {
	
	private ExperimentAuthzDAO experimentAuthzDAO;

	@ResponseBody
	@RequestMapping(value = ModelPathUtils.EXPERIMENT_PATH + "/form/add*", method = RequestMethod.POST)
	public FormResponseMap experimentFormAdd(ExperimentFormBacker experiment, Errors errors) {
		// Bind errors are intentionally ignored, however the argument must be 
		// present to avoid automatic delegation to the exception handler.
				
		String user = SecurityUtil.getPersonGid();
		
		AddResult result = experimentAuthzDAO.add(user, experiment).get();
		
		FormResponseMap response = new FormResponseMap(ExperimentFormBacker.transformResult(result));
		
		if(response.isSuccess()) {				
			response.put("viewUrl", ModelPathUtils.getModelExperimentPath("/", experiment.getGid(), ".html"));
		}
		
		return response;
	}

	@ResponseBody
	@RequestMapping(value = ModelPathUtils.EXPERIMENT_PATH + "/form/edit*", method = RequestMethod.POST)
	public FormResponseMap experimentFormEdit(ExperimentFormBacker experiment, Errors errors) {
		// Bind errors are intentionally ignored, however the argument must be 
		// present to avoid automatic delegation to the exception handler.
		
		String user = SecurityUtil.getPersonGid();
		
		EditResult result = experimentAuthzDAO.edit(user, experiment).get();
		
		FormResponseMap response = new FormResponseMap(ExperimentFormBacker.transformResult(result));
		
		if(response.isSuccess()) {
			response.setMessage("Experiment Saved");
		}
		
		return response;
	}

	@ResponseBody
	@RequestMapping(value = ModelPathUtils.EXPERIMENT_PATH + "/form/remove*", method = RequestMethod.POST)
	public FormResponseMap removeExperiment(@RequestParam String gid) {
		
		String user = SecurityUtil.getPersonGid();
		
		RemoveResult result;
		try {
			result = experimentAuthzDAO.remove(user, gid).get();
		}
		catch(AuthorizationException e) {
			return new FormResponseMap(false, "Not Permitted");
		}
		
		return new FormResponseMap(result);
	}

	public ExperimentAuthzDAO getExperimentAuthzDAO() {
		return experimentAuthzDAO;
	}
	public void setExperimentAuthzDAO(ExperimentAuthzDAO experimentAuthzDAO) {
		this.experimentAuthzDAO = experimentAuthzDAO;
	}
}
