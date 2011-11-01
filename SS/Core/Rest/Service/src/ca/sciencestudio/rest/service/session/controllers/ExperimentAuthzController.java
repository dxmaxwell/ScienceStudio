/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ExperimentAuthzController class.
 *     
 */
package ca.sciencestudio.rest.service.session.controllers;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.AddResult;
import ca.sciencestudio.model.EditResult;
import ca.sciencestudio.model.Permissions;
import ca.sciencestudio.model.dao.ModelBasicDAO;
import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.ExperimentBasicDAO;
import ca.sciencestudio.model.session.validators.ExperimentValidator;
import ca.sciencestudio.model.validators.ModelValidator;
import ca.sciencestudio.rest.service.controllers.AbstractSessionAuthzController;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 * 
 *
 */
@Controller
public class ExperimentAuthzController extends AbstractSessionAuthzController<Experiment> {

	private static final String EXPERIMENT_MODEL_PATH = "/experiments";
	
	private ExperimentBasicDAO experimentBasicDAO;

	private ExperimentValidator experimentValidator;
	
	@ResponseBody
	@RequestMapping(value = EXPERIMENT_MODEL_PATH + "/perms*", method = RequestMethod.GET)
	public Permissions permissions(@RequestParam String user) {
		return new Permissions(true);
	}
	
	@ResponseBody
	@RequestMapping(value = EXPERIMENT_MODEL_PATH + "/{gid}/perms*", method = RequestMethod.GET)
	public Permissions permissions(@RequestParam String user, @PathVariable String gid) {
		return new Permissions(true);
	}
	
	@ResponseBody
	@RequestMapping(value = EXPERIMENT_MODEL_PATH + "/{facility}*", method = RequestMethod.POST)
	public AddResult add(@RequestBody Experiment experiment, @PathVariable String facility, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(!facility.equals(experimentBasicDAO.getGidFacility())) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return new AddResult();
		}
		// Check permissions. //
		return doAdd(experiment, request, response);
	}

	@ResponseBody
	@RequestMapping(value = EXPERIMENT_MODEL_PATH + "/{gid}*", method = RequestMethod.PUT)
	public EditResult edit(@RequestBody Experiment experiment, @PathVariable String gid, HttpServletResponse response) throws Exception {
		experiment.setGid(gid);
		
		// Check permissions. //
		return doEdit(experiment, response);
	}

	@ResponseBody
	@RequestMapping(value = EXPERIMENT_MODEL_PATH + "/{gid}*", method = RequestMethod.DELETE)
	public void remove(@PathVariable String gid, HttpServletResponse response) throws Exception {
		// Check permissions. //
		
		response.setStatus(HttpStatus.FORBIDDEN.value());
		//doRemove(gid, response);
	}

	@ResponseBody
	@RequestMapping(value = EXPERIMENT_MODEL_PATH + "/{gid}*", method = RequestMethod.GET)
	public Object get(@RequestParam String user, @PathVariable String gid, HttpServletResponse response) throws Exception {
		try {
			Experiment experiment = experimentBasicDAO.get(gid);
			if(experiment == null) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return Collections.emptyMap();
			}
			
			if(hasLoginRole(user, LOGIN_ROLE_ADMIN_SESSIONS)) {
				return experiment;
			}
			
			if(isSessionMember(user, experiment.getSessionGid())) {
				return experiment;
			}
			
			Session session = getSessionBasicDAO().get(experiment.getSessionGid());
			if((session != null) && isProjectMember(user, user, session.getProjectGid())) {
				return experiment;
			}
		
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return Collections.emptyMap();
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return Collections.emptyList();
		}	
	}
	@ResponseBody
	@RequestMapping(value = EXPERIMENT_MODEL_PATH + "*", method = RequestMethod.GET, params = "session")
	public Object getAll(@RequestParam String user, @RequestParam("session") String sessionGid, HttpServletResponse response) {
		try {
			if(hasLoginRole(user, LOGIN_ROLE_ADMIN_SESSIONS)) {
				return experimentBasicDAO.getAllBySessionGid(sessionGid);
			}
			
			if(isSessionMember(user, sessionGid)) {
				return experimentBasicDAO.getAllBySessionGid(sessionGid);
			}
			
			Session session = getSessionBasicDAO().get(sessionGid);
			if((session != null) && isProjectMember(user, user, session.getProjectGid())) {
				return experimentBasicDAO.getAllBySessionGid(sessionGid);
			}
			
			return Collections.emptyList();
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return Collections.emptyList();
		}
	}

	@Override
	public String getModelPath() {
		return EXPERIMENT_MODEL_PATH;
	}
	
	@Override
	public ModelBasicDAO<Experiment> getModelBasicDAO() {
		return experimentBasicDAO;
	}

	@Override
	public ModelValidator<Experiment> getModelValidator() {
		return experimentValidator;
	}

	public ExperimentBasicDAO getExperimentBasicDAO() {
		return experimentBasicDAO;
	}
	public void setExperimentBasicDAO(ExperimentBasicDAO experimentBasicDAO) {
		this.experimentBasicDAO = experimentBasicDAO;
	}

	public ExperimentValidator getExperimentValidator() {
		return experimentValidator;
	}
	public void setExperimentValidator(ExperimentValidator experimentValidator) {
		this.experimentValidator = experimentValidator;
	}
}
