/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ExperimentGridController class.
 *     
 */
package ca.sciencestudio.service.session.controllers;

import java.util.ArrayList;
import java.util.List;

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
import ca.sciencestudio.model.facility.dao.TechniqueDAO;
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectDAO;
import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.sample.dao.SampleDAO;
import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.ExperimentDAO;
import ca.sciencestudio.model.session.dao.SessionDAO;
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.controllers.AbstractModelController;
import ca.sciencestudio.service.session.backers.ExperimentGridBacker;

/**
 * @author maxweld
 *
 */
@Controller
public class ExperimentGridController extends AbstractModelController {

	@Autowired
	private SampleDAO sampleDAO;
	
	@Autowired
	private ProjectDAO projectDAO;
	
	@Autowired
	private SessionDAO sessionDAO;
	
	@Autowired
	private ExperimentDAO experimentDAO;
	
	@Autowired
	private TechniqueDAO techniqueDAO;
	
	@Autowired
	private InstrumentDAO instrumentDAO;
	
	@Autowired
	private InstrumentTechniqueDAO instrumentTechniqueDAO;
	
	@RequestMapping(value = "/session/{sessionId}/experiments/grid.{format}", method = RequestMethod.GET)
	public String getSessionList(@PathVariable int sessionId, @PathVariable String format, ModelMap model) {
		
		List<ExperimentGridBacker> experimentGridBackerList = new ArrayList<ExperimentGridBacker>();
		model.put("response", experimentGridBackerList);
		
		String responseView = "response-" + format;
		
		Session session = sessionDAO.getSessionById(sessionId);
		if(session == null) {
			return responseView;
		}
		
		Project project = projectDAO.getProjectById(session.getProjectId());
		if(project == null) {
			return responseView;
		}
		
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		Object team = AuthorityUtil.buildProjectGroupAuthority(project.getId());
		if(!SecurityUtil.hasAnyAuthority(admin, team)) {
			return responseView;
		}
		
		int laboratoryId = session.getLaboratoryId();
		
		List<Sample> sampleList = sampleDAO.getSampleListByProjectId(project.getId());
		List<Experiment> experimentList = experimentDAO.getExperimentListBySessionId(sessionId);
		List<Technique> techniqueList = techniqueDAO.getTechniqueListByLaboratoryId(laboratoryId);
		List<Instrument> instrumentList = instrumentDAO.getInstrumentListByLaboratoryId(laboratoryId);
		List<InstrumentTechnique> instrumentTechniqueList = instrumentTechniqueDAO.getInstrumentTechniqueListByLaboratoryId(laboratoryId);
		
		for(Experiment experiment : experimentList) {
			
			Sample sample = null;
			for(Sample s : sampleList) {
				if(experiment.getSampleId() == s.getId()) {
					sample = s;
				}
			}
			if(sample == null) {
				continue;
			}
			
			InstrumentTechnique instrumentTechnique = null;
			for(InstrumentTechnique it : instrumentTechniqueList) {
				if(experiment.getInstrumentTechniqueId() == it.getId()) {
					instrumentTechnique = it;
				}
			}
			if(instrumentTechnique == null) {
				continue;
			}
				
			Instrument instrument = null;
			for(Instrument i : instrumentList) {
				if(instrumentTechnique.getInstrumentId() == i.getId()) {
					instrument = i;
				}
			}
			if(instrument == null) {
				continue;
			}
			
			Technique technique = null;		
			for(Technique t : techniqueList) {
				if(instrumentTechnique.getTechniqueId() == t.getId()) {
					technique = t;
				}
			}
			if(technique == null) {
				continue;
			}
			
			experimentGridBackerList.add(new ExperimentGridBacker(experiment, project.getId(), sample, instrument, technique));
		}
		
		return responseView;
	}

	public SampleDAO getSampleDAO() {
		return sampleDAO;
	}
	public void setSampleDAO(SampleDAO sampleDAO) {
		this.sampleDAO = sampleDAO;
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

	public ExperimentDAO getExperimentDAO() {
		return experimentDAO;
	}
	public void setExperimentDAO(ExperimentDAO experimentDAO) {
		this.experimentDAO = experimentDAO;
	}

	public TechniqueDAO getTechniqueDAO() {
		return techniqueDAO;
	}
	public void setTechniqueDAO(TechniqueDAO techniqueDAO) {
		this.techniqueDAO = techniqueDAO;
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
	public void setInstrumentTechniqueDAO(InstrumentTechniqueDAO instrumentTechniqueDAO) {
		this.instrumentTechniqueDAO = instrumentTechniqueDAO;
	}
}

	
