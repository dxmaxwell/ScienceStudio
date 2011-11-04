/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ExperimentAuthzController class.
 *     
 */
package ca.sciencestudio.rest.service.session.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.ModelBasicDAO;
import ca.sciencestudio.model.facility.InstrumentTechnique;
import ca.sciencestudio.model.facility.dao.InstrumentTechniqueBasicDAO;
import ca.sciencestudio.model.project.dao.ProjectAuthzDAO;
import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.sample.dao.SampleAuthzDAO;
import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.ExperimentBasicDAO;
import ca.sciencestudio.model.session.dao.ScanAuthzDAO;
import ca.sciencestudio.model.session.dao.ScanBasicDAO;
import ca.sciencestudio.model.session.dao.SessionBasicDAO;
import ca.sciencestudio.model.session.validators.ExperimentValidator;
import ca.sciencestudio.model.utilities.GID;
import ca.sciencestudio.model.validators.ModelValidator;
import ca.sciencestudio.rest.service.controllers.AbstractSessionAuthzController;
import ca.sciencestudio.util.authz.Authorities;
import ca.sciencestudio.util.exceptions.ModelAccessException;
import ca.sciencestudio.util.rest.AddResult;
import ca.sciencestudio.util.rest.EditResult;
import ca.sciencestudio.util.rest.RemoveResult;

/**
 * @author maxweld
 * 
 *
 */
@Controller
public class ExperimentAuthzController extends AbstractSessionAuthzController<Experiment> {

	private static final String EXPERIMENT_MODEL_PATH = "/experiments";
	
	private ScanBasicDAO scanBasicDAO;
	
	private SessionBasicDAO sessionBasicDAO;
	
	private ExperimentBasicDAO experimentBasicDAO;

	private InstrumentTechniqueBasicDAO instrumentTechniqueBasicDAO;
	
	private ExperimentValidator experimentValidator;
	
	private ScanAuthzDAO scanAuthzDAO;
	
	private SampleAuthzDAO sampleAuthzDAO;
	
	private ProjectAuthzDAO projectAuthzDAO;
	
	
	@ResponseBody
	@RequestMapping(value = EXPERIMENT_MODEL_PATH + "*", method = RequestMethod.POST)
	public AddResult add(@RequestBody Experiment experiment, @RequestParam String user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String sessionGid = experiment.getSessionGid();
		
		try {
			Session session = sessionBasicDAO.get(sessionGid);
			if(session == null) {
				response.setStatus(HttpStatus.NOT_FOUND.value());
				return new AddResult("Session (" + sessionGid + ") not found.");
			}
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new AddResult(e.getMessage());
		}
		
		Authorities authorities;
		try {
			authorities = sessionAuthorityAccessor.getAuthorities(user, sessionGid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new AddResult(e.getMessage());
		}
		
		if(authorities.containsNone(SESSION_EXPERIMENTER, FACILITY_ADMIN_SESSIONS)) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return new AddResult("Required authorities not found.");
		}
		
		InstrumentTechnique it;
		try {
			it = instrumentTechniqueBasicDAO.get(experiment.getInstrumentTechniqueGid());
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new AddResult(e.getMessage());
		}
		
		if(it == null) {
			response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
			return new AddResult("Instrument/Technique experiment references not found.");
		}
		
		GID sourceGid = GID.parse(experiment.getSourceGid());
		if(sourceGid == null) {
			response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
			return new AddResult("Source experiment references not found.");
		}
		
		Object source = null;
		
		if(sourceGid.isType(Sample.GID_TYPE)) {
			try {
				source = sampleAuthzDAO.get(user, sourceGid.toString());
			}
			catch(ModelAccessException e) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return new AddResult(e.getMessage());
			}
		}
		
		if((source == null) && sourceGid.isType(Scan.GID_TYPE)) {
			try {
				source = scanAuthzDAO.get(user, sourceGid.toString());
			}
			catch(ModelAccessException e) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return new AddResult(e.getMessage());
			}
		}
		
		if(source == null) {
			response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
			return new AddResult("Source experiment references not found.");
		}
		
		return doAdd(experiment, request, response);
	}

	@ResponseBody
	@RequestMapping(value = EXPERIMENT_MODEL_PATH + "/{gid}*", method = RequestMethod.PUT)
	public EditResult edit(@RequestBody Experiment experiment, @RequestParam String user, @PathVariable String gid, HttpServletResponse response) throws Exception {
		
		Experiment current;
		try {
			current = experimentBasicDAO.get(gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new EditResult(e.getMessage());
		}
		
		if(current == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return new EditResult("Experiment (" + gid + ") not found.");
		}
		
		Authorities authorities;
		try {
			authorities = sessionAuthorityAccessor.getAuthorities(user, experiment.getSessionGid());
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new EditResult(e.getMessage());
		}
		
		if(authorities.containsNone(SESSION_EXPERIMENTER, FACILITY_ADMIN_SESSIONS)) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return new EditResult("Required authorities not found.");
		}
		
		experiment.setGid(current.getGid());
		experiment.setSessionGid(current.getSessionGid());
		
		if(!experiment.getInstrumentTechniqueGid().equalsIgnoreCase(current.getInstrumentTechniqueGid())) {
		
			InstrumentTechnique it;
			try {
				it = instrumentTechniqueBasicDAO.get(experiment.getInstrumentTechniqueGid());
			}
			catch(ModelAccessException e) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return new EditResult(e.getMessage());
			}
		
			if(it == null) {
				response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
				return new EditResult("Instrument/Technique experiment references not found.");
			}
		}
		
		if(!experiment.getSourceGid().equals(current.getSessionGid())) {
		
			GID sourceGid = GID.parse(experiment.getSourceGid());
			if(sourceGid == null) {
				response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
				return new EditResult("Source experiment references not found.");
			}
			
			Object source = null;
			
			if(sourceGid.isType(Sample.GID_TYPE)) {
				try {
					source = sampleAuthzDAO.get(user, sourceGid.toString());
				}
				catch(ModelAccessException e) {
					response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
					return new EditResult(e.getMessage());
				}
			}
			
			if((source == null) && sourceGid.isType(Scan.GID_TYPE)) {
				try {
					source = scanAuthzDAO.get(user, sourceGid.toString());
				}
				catch(ModelAccessException e) {
					response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
					return new EditResult(e.getMessage());
				}
			}
			
			if(source == null) {
				response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
				return new EditResult("Source experiment references not found.");
			}
		}
		
		return doEdit(experiment, response);
	}

	@ResponseBody
	@RequestMapping(value = EXPERIMENT_MODEL_PATH + "/{gid}*", method = RequestMethod.DELETE)
	public RemoveResult remove(@RequestParam String user, @PathVariable String gid, HttpServletResponse response) throws Exception {
		
		Experiment experiment;
		try {
			experiment = experimentBasicDAO.get(gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new RemoveResult(e.getMessage());
		}
		
		if(experiment == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return new RemoveResult("Experiment (" + gid + ") not found.");
		}
		
		Authorities authorities;
		try {
			authorities = sessionAuthorityAccessor.getAuthorities(user, gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new RemoveResult(e.getMessage());
		}
		
		if(authorities.containsNone(SESSION_EXPERIMENTER, FACILITY_ADMIN_SESSIONS)) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return new RemoveResult("Required authorities not found.");
		}
		
		List<Scan> scans;
		try {
			scans = scanBasicDAO.getAllByExperimentGid(gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new RemoveResult(e.getMessage());
		}
		
		if(!scans.isEmpty()) {
			response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
			return new RemoveResult("Experiment has associated Scans.");
		}

		return doRemove(gid, response);
	}

	@ResponseBody
	@RequestMapping(value = EXPERIMENT_MODEL_PATH + "/{gid}*", method = RequestMethod.GET)
	public Object get(@RequestParam String user, @PathVariable String gid, HttpServletResponse response) throws Exception {
		
		Experiment experiment;
		try {
			experiment = experimentBasicDAO.get(gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyMap();
		}
		
		if(experiment == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return Collections.emptyMap();
		}
		
		Authorities authorities;
		try {
			authorities = sessionAuthorityAccessor.getAuthorities(user, experiment.getSessionGid());
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyMap();
		}
		
		if(!authorities.containsSessionAuthority() && authorities.containsNone(FACILITY_ADMIN_SESSIONS)) {
			
			Session session;
			try {
				session = sessionBasicDAO.get(experiment.getSessionGid());
			}
			catch(ModelAccessException e) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return Collections.emptyMap();
			}
			
			if(session == null) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return Collections.emptyMap();
			}
			
			try {
				authorities = projectAuthzDAO.getAuthorities(user, session.getProjectGid()).get();
			}
			catch(ModelAccessException e) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return Collections.emptyMap();
			}
			
			if(!authorities.containsProjectAuthority() && authorities.containsNone(FACILITY_ADMIN_PROJECTS)) {
				response.setStatus(HttpStatus.FORBIDDEN.value());
				return Collections.emptyMap();
			}
		}
		
		return experiment;
	}
	
	@ResponseBody
	@RequestMapping(value = EXPERIMENT_MODEL_PATH + "*", method = RequestMethod.GET, params = "session")
	public Object getAllBySessionGid(@RequestParam String user, @RequestParam("session") String sessionGid, HttpServletResponse response) throws Exception {
		
		Authorities authorities;
		try {
			authorities = sessionAuthorityAccessor.getAuthorities(user, sessionGid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyList();
		}
		
		if(!authorities.containsSessionAuthority() && authorities.containsNone(FACILITY_ADMIN_SESSIONS)) {
			
			Session session;
			try {
				session = sessionBasicDAO.get(sessionGid);
			}
			catch(ModelAccessException e) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return Collections.emptyList();
			}
			
			if(session == null) {
				return Collections.emptyList();
			}
			
			try {
				authorities = projectAuthzDAO.getAuthorities(user, session.getProjectGid()).get();
			}
			catch(ModelAccessException e) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return Collections.emptyList();
			}
			
			if(!authorities.containsProjectAuthority() && authorities.containsNone(FACILITY_ADMIN_PROJECTS)) {
				return Collections.emptyList();
			}
		}
		
		try {
			return experimentBasicDAO.getAllBySessionGid(sessionGid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyList();
		}
	}

	@ResponseBody
	@RequestMapping(value = EXPERIMENT_MODEL_PATH + "*", method = RequestMethod.GET, params = "source")
	public Object getAllBySourceGid(@RequestParam String user, @RequestParam("source") String sourceGid, HttpServletResponse response) throws Exception {
		
		List<Experiment> experiments;
		try {
			experiments = experimentBasicDAO.getAllBySourceGid(sourceGid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyList();
		}
		
		List<Experiment> authorizedExperiments = new ArrayList<Experiment>();
		
		MultiValueMap<String,Experiment> sessionGids = new LinkedMultiValueMap<String,Experiment>();
		for(Experiment experiment : experiments) {
			sessionGids.add(experiment.getSessionGid(), experiment);	
		}
		
		Set<String> authorizedSessionGids = new HashSet<String>();
		for(String sessionGid : sessionGids.keySet()) {
			Authorities authorities; 
			try {
				authorities = sessionAuthorityAccessor.getAuthorities(user, sessionGid);
			}
			catch(ModelAccessException e) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return Collections.emptyList();
			}
			
			if(authorities.containsSessionAuthority() || authorities.containsAny(FACILITY_ADMIN_SESSIONS)) {
				authorizedSessionGids.add(sessionGid);
			}
		}
		
		for(String authorizedSessionGid : authorizedSessionGids) {
			authorizedExperiments.addAll(sessionGids.get(authorizedSessionGid));
			sessionGids.remove(authorizedSessionGid);
		}
		
		MultiValueMap<String,Experiment> projectGids = new LinkedMultiValueMap<String,Experiment>();
		for(Map.Entry<String,List<Experiment>> entry: sessionGids.entrySet()) {
			Session session;
			try {
				session = sessionBasicDAO.get(entry.getKey());
			}
			catch(ModelAccessException e) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return Collections.emptyList();
			}
			
			if(session == null) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return Collections.emptyList();
			}
			
			for(Experiment experiment : entry.getValue()) {
				projectGids.add(session.getProjectGid(), experiment);
			}			
		}
		
		Map<String,Data<Authorities>> authoritiesDataMap = new HashMap<String,Data<Authorities>>(projectGids.size());
		for(String projectGid : projectGids.keySet()) {
			try {
				authoritiesDataMap.put(projectGid, projectAuthzDAO.getAuthorities(user, projectGid));
			}
			catch(ModelAccessException e) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return Collections.emptyList();
			}
		}

		Set<String> authorizedProjectGids = new HashSet<String>();
		for(String projectGid : projectGids.keySet()) {
			Authorities authorities;
			try {
				authorities = authoritiesDataMap.get(projectGid).get();
			}
			catch(ModelAccessException e) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return Collections.emptyList();
			}	
			
			if(authorities.containsSessionAuthority() || authorities.containsAny(FACILITY_ADMIN_PROJECTS)) {
				authorizedProjectGids.add(projectGid);
			}
		}
		
		for(String authorizedProjectGid : authorizedProjectGids) {
			authorizedExperiments.addAll(projectGids.get(authorizedProjectGid));
			projectGids.remove(authorizedProjectGid);
		}
	
		return authorizedExperiments;
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

	public ScanBasicDAO getScanBasicDAO() {
		return scanBasicDAO;
	}
	public void setScanBasicDAO(ScanBasicDAO scanBasicDAO) {
		this.scanBasicDAO = scanBasicDAO;
	}

	public SessionBasicDAO getSessionBasicDAO() {
		return sessionBasicDAO;
	}
	public void setSessionBasicDAO(SessionBasicDAO sessionBasicDAO) {
		this.sessionBasicDAO = sessionBasicDAO;
	}

	public InstrumentTechniqueBasicDAO getInstrumentTechniqueBasicDAO() {
		return instrumentTechniqueBasicDAO;
	}
	public void setInstrumentTechniqueBasicDAO(
			InstrumentTechniqueBasicDAO instrumentTechniqueBasicDAO) {
		this.instrumentTechniqueBasicDAO = instrumentTechniqueBasicDAO;
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
	
	public ScanAuthzDAO getScanAuthzDAO() {
		return scanAuthzDAO;
	}
	public void setScanAuthzDAO(ScanAuthzDAO scanAuthzDAO) {
		this.scanAuthzDAO = scanAuthzDAO;
	}

	public SampleAuthzDAO getSampleAuthzDAO() {
		return sampleAuthzDAO;
	}
	public void setSampleAuthzDAO(SampleAuthzDAO sampleAuthzDAO) {
		this.sampleAuthzDAO = sampleAuthzDAO;
	}

	public ProjectAuthzDAO getProjectAuthzDAO() {
		return projectAuthzDAO;
	}
	public void setProjectAuthzDAO(ProjectAuthzDAO projectAuthzDAO) {
		this.projectAuthzDAO = projectAuthzDAO;
	}
}
