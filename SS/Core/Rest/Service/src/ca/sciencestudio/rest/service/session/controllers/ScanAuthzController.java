/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ScanAuthzController class.
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
import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.ExperimentBasicDAO;
import ca.sciencestudio.model.session.dao.ScanBasicDAO;
import ca.sciencestudio.model.session.validators.ScanValidator;
import ca.sciencestudio.model.validators.ModelValidator;
import ca.sciencestudio.rest.service.controllers.AbstractSessionAuthzController;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 *
 */
@Controller
public class ScanAuthzController extends AbstractSessionAuthzController<Scan> {

	private static final String SCAN_MODEL_PATH = "/scans";
	
	private ExperimentBasicDAO experimentBasicDAO;
	
	private ScanBasicDAO scanBasicDAO;
	
	private ScanValidator scanValidator;
	
	@ResponseBody
	@RequestMapping(value = SCAN_MODEL_PATH + "/perms*", method = RequestMethod.GET)
	public Permissions permissions(@RequestParam String user) {
		return new Permissions(true);
	}
	
	@ResponseBody
	@RequestMapping(value = SCAN_MODEL_PATH + "/{gid}/perms*", method = RequestMethod.GET)
	public Permissions permissions(@RequestParam String user, @PathVariable String gid) {
		return new Permissions(true);
	}
	
	@ResponseBody 
	@RequestMapping(value = SCAN_MODEL_PATH + "/{facility}*", method = RequestMethod.POST)
	public AddResult add(@RequestBody Scan scan, @PathVariable String facility, HttpServletRequest request, HttpServletResponse response) throws Exception{
		if(!facility.equals(scanBasicDAO.getGidFacility())) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return new AddResult();
		}
		// TODO: Check permissions. //
		return doAdd(scan, request, response);
	}
	
	@ResponseBody
	@RequestMapping(value = SCAN_MODEL_PATH + "/{gid}*", method = RequestMethod.PUT)
	public EditResult edit(@RequestBody Scan scan, @PathVariable String gid, HttpServletResponse response) throws Exception{
		
		Scan current = scanBasicDAO.get(gid); 
		if(current == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return new EditResult();
		}
		// Only name can be changed. //
		current.setName(scan.getName());
		
		// TODO: Check permissions. //
		return doEdit(current, response);
	}
	
	@RequestMapping(value = SCAN_MODEL_PATH + "/{gid}*", method = RequestMethod.DELETE)
	public void remove(@PathVariable String gid, HttpServletResponse response) throws Exception{
		// TODO: Check permissions. //
		response.setStatus(HttpStatus.FORBIDDEN.value());
		//doRemove(gid, response);
	}
	
	@ResponseBody 
	@RequestMapping(value = SCAN_MODEL_PATH + "/{gid}*", method = RequestMethod.GET)
	public Object get(@RequestParam String user, @PathVariable String gid, HttpServletResponse response) throws Exception {
		try {
			Scan scan = scanBasicDAO.get(gid);
			if(scan == null) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return Collections.emptyMap();
			}
			
			if(hasLoginRole(user, LOGIN_ROLE_ADMIN_SESSIONS)) {
				return scan;
			}
			
			Experiment experiment = experimentBasicDAO.get(scan.getExperimentGid());
			if(experiment != null) {				
				if(isSessionMember(user, experiment.getSessionGid())) {
					return scan;
				}
				
				Session session = getSessionBasicDAO().get(experiment.getSessionGid());
				if((session != null) && isProjectMember(user, user, session.getProjectGid())) {
					return scan;
				}
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
	@RequestMapping(value = SCAN_MODEL_PATH + "*", method = RequestMethod.GET, params = "experiment")
	public Object getAll(@RequestParam String user, @RequestParam("experiment") String experimentGid, HttpServletResponse response) {
		try {
			if(hasLoginRole(user, LOGIN_ROLE_ADMIN_SESSIONS)) {
				return scanBasicDAO.getAllByExperimentGid(experimentGid);
			}
			
			Experiment experiment = experimentBasicDAO.get(experimentGid);
			if(experiment != null) {
				if(isSessionMember(user, experiment.getSessionGid())) {
					return scanBasicDAO.getAllByExperimentGid(experimentGid);
				}
				
				Session session = getSessionBasicDAO().get(experiment.getSessionGid());
				if((session != null) && isProjectMember(user, user, session.getProjectGid())) {
					return scanBasicDAO.getAllByExperimentGid(experimentGid);
				}	
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
		return SCAN_MODEL_PATH;
	}
	
	@Override
	public ModelBasicDAO<Scan> getModelBasicDAO() {
		return scanBasicDAO;
	}

	@Override
	public ModelValidator<Scan> getModelValidator() {
		return scanValidator;
	}

	public ExperimentBasicDAO getExperimentBasicDAO() {
		return experimentBasicDAO;
	}
	public void setExperimentBasicDAO(ExperimentBasicDAO experimentBasicDAO) {
		this.experimentBasicDAO = experimentBasicDAO;
	}

	public ScanBasicDAO getScanBasicDAO() {
		return scanBasicDAO;
	}
	public void setScanBasicDAO(ScanBasicDAO scanBasicDAO) {
		this.scanBasicDAO = scanBasicDAO;
	}

	public ScanValidator getScanValidator() {
		return scanValidator;
	}
	public void setScanValidator(ScanValidator scanValidator) {
		this.scanValidator = scanValidator;
	}
}
