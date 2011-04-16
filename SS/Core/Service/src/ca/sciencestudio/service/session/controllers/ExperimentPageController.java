/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ExperimentPageController class.
 *     
 */
package ca.sciencestudio.service.session.controllers;

import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.sciencestudio.model.facility.Instrument;
import ca.sciencestudio.model.facility.InstrumentTechnique;
import ca.sciencestudio.model.facility.Technique;
import ca.sciencestudio.model.facility.dao.InstrumentDAO;
import ca.sciencestudio.model.facility.dao.InstrumentTechniqueDAO;
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.session.backers.ExperimentFormBacker;
import ca.sciencestudio.service.session.backers.InstrumentTechniqueOption;
import ca.sciencestudio.model.facility.dao.TechniqueDAO;
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectDAO;
import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.sample.dao.SampleDAO;
import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.ExperimentDAO;
import ca.sciencestudio.model.session.dao.SessionDAO;
import ca.sciencestudio.service.controllers.AbstractModelController;

/**
 * @author maxweld
 *
 */
@Controller
public class ExperimentPageController extends AbstractModelController {
	
	private static final String ERROR_VIEW = "frag/error";
	
	@Autowired
	private SampleDAO sampleDAO;
	
	@Autowired
	private ProjectDAO projectDAO;
	
	@Autowired
	private SessionDAO sessionDAO;
	
	@Autowired
	private TechniqueDAO techniqueDAO;
	
	@Autowired
	private ExperimentDAO experimentDAO;
	
	@Autowired
	private InstrumentDAO instrumentDAO;
	
	@Autowired
	private InstrumentTechniqueDAO instrumentTechniqueDAO;
	
	@RequestMapping(value = "/session/{sessionId}/experiments.html", method = RequestMethod.GET)
	public String getExperimentsPage(@PathVariable int sessionId, HttpServletRequest request, ModelMap model) {
		
		Session session = sessionDAO.getSessionById(sessionId);
		if(session == null) {
			model.put("error", "Session not found.");
			return ERROR_VIEW;
		}
		
		Project project = projectDAO.getProjectById(session.getProjectId());
		if(project == null) {
			model.put("error", "Project not found.");
			return ERROR_VIEW;
		}
		
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		Object group = AuthorityUtil.buildProjectGroupAuthority(project.getId());
		if(!SecurityUtil.hasAnyAuthority(admin, group)) {
			model.put("error", "Permission denied.");
			return "frag/error";
		}
		
		List<Sample> sampleList = sampleDAO.getSampleListByProjectId(project.getId());
		List<Technique> techniqueList = techniqueDAO.getTechniqueListByLaboratoryId(session.getLaboratoryId());
		List<Instrument> instrumentList = instrumentDAO.getInstrumentListByLaboratoryId(session.getLaboratoryId());
		List<InstrumentTechnique> instrumentTechniqueList = instrumentTechniqueDAO.getInstrumentTechniqueListByLaboratoryId(session.getLaboratoryId());
		List<InstrumentTechniqueOption> instrumentTechniqueOptionList = getInstrumentTechniqueOptionList(instrumentTechniqueList, instrumentList, techniqueList);
		
		model.put("instrumentTechniqueOptionList", instrumentTechniqueOptionList);
		model.put("instrumentList", instrumentList);
		model.put("sampleList", sampleList);
		model.put("project", project);
		model.put("session", session);
		return "frag/experiments";
	}
	
	@RequestMapping(value = "/experiment/{experimentId}.html", method = RequestMethod.GET)
	public String getExperimentPage(@PathVariable int experimentId, HttpServletRequest request, ModelMap model) {
		
		Experiment experiment = experimentDAO.getExperimentById(experimentId);
		if(experiment == null) {
			model.put("error", "Experiment not found.");
			return ERROR_VIEW;
		}
		
		Session session = sessionDAO.getSessionById(experiment.getSessionId());
		if(session == null) {
			model.put("error", "Session not found.");
			return ERROR_VIEW;
		}
		
		Project project = projectDAO.getProjectById(session.getProjectId());
		if(project == null) {
			model.put("error", "Project not found.");
			return ERROR_VIEW;
		}
		
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		Object group = AuthorityUtil.buildProjectGroupAuthority(project.getId());
		if(!SecurityUtil.hasAnyAuthority(admin, group)) {
			model.put("error", "Permission denied.");
			return ERROR_VIEW;
		}
	
		List<Sample> sampleList = sampleDAO.getSampleListByProjectId(project.getId());
		List<Technique> techniqueList = techniqueDAO.getTechniqueListByLaboratoryId(session.getLaboratoryId());
		List<Instrument> instrumentList = instrumentDAO.getInstrumentListByLaboratoryId(session.getLaboratoryId());
		List<InstrumentTechnique> instrumentTechniqueList = instrumentTechniqueDAO.getInstrumentTechniqueListByLaboratoryId(session.getLaboratoryId());
		List<InstrumentTechniqueOption> instrumentTechniqueOptionList = getInstrumentTechniqueOptionList(instrumentTechniqueList, instrumentList, techniqueList);
		
		ExperimentFormBacker experimentFormBacker = null;
		for(InstrumentTechnique instrumentTechnique : instrumentTechniqueList) {
			if(instrumentTechnique.getId() == experiment.getInstrumentTechniqueId()) {
				experimentFormBacker = new ExperimentFormBacker(experiment, instrumentTechnique);
				break;
			}
		}	
	
		if(experimentFormBacker == null) {
			model.put("errorMessage", "Experiment does not have an instrument technique.");
			return ERROR_VIEW;
		}
		
		model.put("instrumentTechniqueOptionList", instrumentTechniqueOptionList);
		model.put("experimentFormBacker", experimentFormBacker);
		model.put("instrumentList", instrumentList);
		model.put("sampleList", sampleList);
		model.put("experiment", experiment);
		model.put("project", project);
		model.put("session", session);
		return "frag/experiment";
	}
	
	protected List<InstrumentTechniqueOption> getInstrumentTechniqueOptionList(List<InstrumentTechnique> instrumentTechniqueList, List<Instrument> instrumentList, List<Technique> techniqueList) {
		List<InstrumentTechniqueOption> instrumentTechniqueOptionList = new ArrayList<InstrumentTechniqueOption>();
		
		for(InstrumentTechnique instrumentTechnique : instrumentTechniqueList) {
			for(Instrument instrument : instrumentList) {
				if(instrument.getId() == instrumentTechnique.getInstrumentId()) {
					for(Technique technique : techniqueList) {
						if(technique.getId() == instrumentTechnique.getTechniqueId()) {
							instrumentTechniqueOptionList.add(new InstrumentTechniqueOption(instrumentTechnique, instrument, technique));
							break;
						}
					}
					break;
				}
			}
		}
		
		return instrumentTechniqueOptionList;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}
	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public SessionDAO getSessionDAO() {
		return sessionDAO;
	}
	public void setSessionDAO(SessionDAO sessionDAO) {
		this.sessionDAO = sessionDAO;
	}

	public TechniqueDAO getTechniqueDAO() {
		return techniqueDAO;
	}
	public void setTechniqueDAO(TechniqueDAO techniqueDAO) {
		this.techniqueDAO = techniqueDAO;
	}

	public ExperimentDAO getExperimentDAO() {
		return experimentDAO;
	}
	public void setExperimentDAO(ExperimentDAO experimentDAO) {
		this.experimentDAO = experimentDAO;
	}

	public InstrumentDAO getInstrumentDAO() {
		return instrumentDAO;
	}
	public void setInstrumentDAO(InstrumentDAO instrumentDAO) {
		this.instrumentDAO = instrumentDAO;
	}

	public InstrumentTechniqueDAO getInstrumentTechniqueDAO() {
		return instrumentTechniqueDAO;
	}
	public void setInstrumentTechniqueDAO(
			InstrumentTechniqueDAO instrumentTechniqueDAO) {
		this.instrumentTechniqueDAO = instrumentTechniqueDAO;
	}

	public SampleDAO getSampleDAO() {
		return sampleDAO;
	}
	public void setSampleDAO(SampleDAO sampleDAO) {
		this.sampleDAO = sampleDAO;
	}
}
