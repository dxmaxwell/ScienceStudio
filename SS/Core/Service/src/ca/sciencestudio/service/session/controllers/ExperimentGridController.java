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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.facility.Instrument;
import ca.sciencestudio.model.facility.InstrumentTechnique;
import ca.sciencestudio.model.facility.Technique;
import ca.sciencestudio.model.facility.dao.InstrumentAuthzDAO;
import ca.sciencestudio.model.facility.dao.InstrumentTechniqueAuthzDAO;
import ca.sciencestudio.model.facility.dao.TechniqueAuthzDAO;
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectAuthzDAO;
import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.sample.dao.SampleAuthzDAO;
import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.ExperimentAuthzDAO;
import ca.sciencestudio.model.session.dao.SessionAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.controllers.AbstractModelController;
import ca.sciencestudio.service.session.backers.ExperimentGridBacker;
import ca.sciencestudio.service.utilities.ModelPathUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class ExperimentGridController extends AbstractModelController {

	private SampleAuthzDAO sampleAuthzDAO;	

	private ProjectAuthzDAO projectAuthzDAO;
	
	private SessionAuthzDAO sessionAuthzDAO;
	
	private ExperimentAuthzDAO experimentAuthzDAO;
	
	private TechniqueAuthzDAO techniqueAuthzDAO;
	
	private InstrumentAuthzDAO instrumentAuthzDAO;
	
	private InstrumentTechniqueAuthzDAO instrumentTechniqueAuthzDAO;
	
	@ResponseBody
	@RequestMapping(value = ModelPathUtils.EXPERIMENT_PATH + "/grid*", method = RequestMethod.GET, params = "session")
	public List<ExperimentGridBacker> getSessionList(@RequestParam("session") String sessionGid) {
		
		String user = SecurityUtil.getPersonGid();
		
		List<ExperimentGridBacker> experimentGridBackerList = new ArrayList<ExperimentGridBacker>();
		
		Session session = sessionAuthzDAO.get(user, sessionGid).get();
		if(session == null) {
			return experimentGridBackerList;
		}
		
		Data<Project> dataProject = projectAuthzDAO.get(user, session.getProjectGid());
		Data<List<Sample>> dataSampleList = sampleAuthzDAO.getAllByProjectGid(user, session.getProjectGid());
		Data<List<Experiment>> dataExperimentList = experimentAuthzDAO.getAllBySessionGid(user, sessionGid);
		Data<List<Technique>> dataTechniqueList = techniqueAuthzDAO.getAllByLaboratoryGid(session.getLaboratoryGid());
		Data<List<Instrument>> dataInstrumentList = instrumentAuthzDAO.getAllByLaboratoryGid(session.getLaboratoryGid());
		Data<List<InstrumentTechnique>> dataInstrumentTechniqueList = instrumentTechniqueAuthzDAO.getAllByLaboratoryGid(session.getLaboratoryGid());
		
		Project project = dataProject.get();
		if(project == null) {
			return experimentGridBackerList;
		}
		
		List<Sample> sampleList = dataSampleList.get();
		List<Experiment> experimentList = dataExperimentList.get();
		List<Technique> techniqueList = dataTechniqueList.get();
		List<Instrument> instrumentList = dataInstrumentList.get();
		List<InstrumentTechnique> instrumentTechniqueList = dataInstrumentTechniqueList.get();
		
		for(Experiment experiment : experimentList) {
			
			Sample sample = null;
			for(Sample s : sampleList) {
				if(experiment.getSourceGid().equals(s.getGid())) {
					sample = s;
				}
			}
			if(sample == null) {
				continue;
			}
			
			InstrumentTechnique instrumentTechnique = null;
			for(InstrumentTechnique it : instrumentTechniqueList) {
				if(experiment.getInstrumentTechniqueGid().equals(it.getGid())) {
					instrumentTechnique = it;
				}
			}
			if(instrumentTechnique == null) {
				continue;
			}
				
			Instrument instrument = null;
			for(Instrument i : instrumentList) {
				if(instrumentTechnique.getInstrumentGid().equals(i.getGid())) {
					instrument = i;
				}
			}
			if(instrument == null) {
				continue;
			}
			
			Technique technique = null;		
			for(Technique t : techniqueList) {
				if(instrumentTechnique.getTechniqueGid().equals(t.getGid())) {
					technique = t;
				}
			}
			if(technique == null) {
				continue;
			}
			
			experimentGridBackerList.add(new ExperimentGridBacker(experiment, sample, instrument, technique));
		}
		
		return experimentGridBackerList;
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

	public SessionAuthzDAO getSessionAuthzDAO() {
		return sessionAuthzDAO;
	}
	public void setSessionAuthzDAO(SessionAuthzDAO sessionAuthzDAO) {
		this.sessionAuthzDAO = sessionAuthzDAO;
	}

	public ExperimentAuthzDAO getExperimentAuthzDAO() {
		return experimentAuthzDAO;
	}
	public void setExperimentAuthzDAO(ExperimentAuthzDAO experimentAuthzDAO) {
		this.experimentAuthzDAO = experimentAuthzDAO;
	}

	public TechniqueAuthzDAO getTechniqueAuthzDAO() {
		return techniqueAuthzDAO;
	}
	public void setTechniqueAuthzDAO(TechniqueAuthzDAO techniqueAuthzDAO) {
		this.techniqueAuthzDAO = techniqueAuthzDAO;
	}

	public InstrumentAuthzDAO getInstrumentAuthzDAO() {
		return instrumentAuthzDAO;
	}
	public void setInstrumentAuthzDAO(InstrumentAuthzDAO instrumentAuthzDAO) {
		this.instrumentAuthzDAO = instrumentAuthzDAO;
	}

	public InstrumentTechniqueAuthzDAO getInstrumentTechniqueAuthzDAO() {
		return instrumentTechniqueAuthzDAO;
	}
	public void setInstrumentTechniqueAuthzDAO(InstrumentTechniqueAuthzDAO instrumentTechniqueAuthzDAO) {
		this.instrumentTechniqueAuthzDAO = instrumentTechniqueAuthzDAO;
	}
}
