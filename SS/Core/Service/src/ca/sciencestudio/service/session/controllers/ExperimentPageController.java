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

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.facility.Instrument;
import ca.sciencestudio.model.facility.InstrumentTechnique;
import ca.sciencestudio.model.facility.Technique;
import ca.sciencestudio.model.facility.dao.InstrumentAuthzDAO;
import ca.sciencestudio.model.facility.dao.InstrumentTechniqueAuthzDAO;
import ca.sciencestudio.model.facility.dao.TechniqueAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.session.backers.ExperimentFormBacker;
import ca.sciencestudio.service.session.backers.InstrumentTechniqueOption;
import ca.sciencestudio.service.utilities.ModelPathUtils;
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectAuthzDAO;
import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.sample.dao.SampleAuthzDAO;
import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.ExperimentAuthzDAO;
import ca.sciencestudio.model.session.dao.SessionAuthzDAO;
import ca.sciencestudio.service.controllers.AbstractModelController;
import ca.sciencestudio.util.authz.Authorities;

/**
 * @author maxweld
 *
 */
@Controller
public class ExperimentPageController extends AbstractModelController {
	
	private SampleAuthzDAO sampleAuthzDAO;	

	private ProjectAuthzDAO projectAuthzDAO;
	
	private SessionAuthzDAO sessionAuthzDAO;
	
	private ExperimentAuthzDAO experimentAuthzDAO;
	
	private InstrumentAuthzDAO instrumentAuthzDAO;
	
	private InstrumentTechniqueAuthzDAO instrumentTechniqueAuthzDAO;

	private TechniqueAuthzDAO techniqueAuthzDAO;
	
	@RequestMapping(value = ModelPathUtils.EXPERIMENT_PATH + ".html")
	public String getExperimentsPage(@RequestParam("session") String sessionGid, HttpServletRequest request, ModelMap model) {
		
		String user = SecurityUtil.getPersonGid();
		
		Data<Authorities> sessionAuthoritiesData = sessionAuthzDAO.getAuthorities(user, sessionGid);
		
		Session session = sessionAuthzDAO.get(user, sessionGid).get();
		if(session == null) {
			model.put("error", "Session not found.");
			return ERROR_VIEW;
		}
		
		Data<Authorities> projectAuthoritiesData = projectAuthzDAO.getAuthorities(user, session.getProjectGid());
		
		Data<List<Sample>> sampleListData = sampleAuthzDAO.getAllByProjectGid(user, session.getProjectGid());
		Data<List<Technique>> techniqueListData = techniqueAuthzDAO.getAllByLaboratoryGid(session.getLaboratoryGid());
		Data<List<Instrument>> instrumentListData = instrumentAuthzDAO.getAllByLaboratoryGid(session.getLaboratoryGid());
		Data<List<InstrumentTechnique>> instrumentTechniqueListData = instrumentTechniqueAuthzDAO.getAllByLaboratoryGid(session.getLaboratoryGid());
		
		Project project = projectAuthzDAO.get(user, session.getProjectGid()).get();
		if(project == null) {
			model.put("error", "Project not found.");
			return ERROR_VIEW;
		}
		
		List<InstrumentTechniqueOption> instrumentTechniqueOptionList = 
				getInstrumentTechniqueOptionList(instrumentTechniqueListData.get(), instrumentListData.get(), techniqueListData.get());
		
		Authorities authorities = mergeAuthorities(projectAuthoritiesData.get(), sessionAuthoritiesData.get());
	
		model.put("instrumentTechniqueList", instrumentTechniqueOptionList);
		model.put("instrumentList", instrumentListData.get());
		model.put("sampleList", sampleListData.get());
		model.put("authorities", authorities);
		model.put("project", project);
		model.put("session", session);
		return "frag/experiments";
	}
	
	@RequestMapping(value = ModelPathUtils.EXPERIMENT_PATH + "/{experimentGid}.html")
	public String getExperimentPage(@PathVariable String experimentGid, HttpServletRequest request, ModelMap model) {
		
		String user = SecurityUtil.getPersonGid();
		
		Experiment experiment = experimentAuthzDAO.get(user, experimentGid).get();
		if(experiment == null) {
			model.put("error", "Experiment not found.");
			return ERROR_VIEW;
		}
		
		Data<Authorities> sessionAuthoritiesData = sessionAuthzDAO.getAuthorities(user, experiment.getSessionGid());
		
		Session session = sessionAuthzDAO.get(user, experiment.getSessionGid()).get();
		if(session == null) {
			model.put("error", "Session not found.");
			return ERROR_VIEW;
		}
		
		Data<Authorities> projectAuthoritiesData = projectAuthzDAO.getAuthorities(user, session.getProjectGid());
		
		Data<List<Sample>> sampleListData = sampleAuthzDAO.getAllByProjectGid(user, session.getProjectGid());
		Data<List<Technique>> techniqueListData = techniqueAuthzDAO.getAllByLaboratoryGid(session.getLaboratoryGid());
		Data<List<Instrument>> instrumentListData = instrumentAuthzDAO.getAllByLaboratoryGid(session.getLaboratoryGid());
		Data<List<InstrumentTechnique>> instrumentTechniqueListData = instrumentTechniqueAuthzDAO.getAllByLaboratoryGid(session.getLaboratoryGid());
		
		Project project = projectAuthzDAO.get(user, session.getProjectGid()).get();
		if(project == null) {
			model.put("error", "Project not found.");
			return ERROR_VIEW;
		}
				
		List<InstrumentTechniqueOption> instrumentTechniqueOptionList =
				getInstrumentTechniqueOptionList(instrumentTechniqueListData.get(), instrumentListData.get(), techniqueListData.get());
		
		ExperimentFormBacker experimentFormBacker = null;
		for(InstrumentTechnique instrumentTechnique : instrumentTechniqueListData.get()) {
			if(instrumentTechnique.getGid().equals(experiment.getInstrumentTechniqueGid())) {
				experimentFormBacker = new ExperimentFormBacker(experiment, instrumentTechnique);
				break;
			}
		}	
	
		if(experimentFormBacker == null) {
			model.put("errorMessage", "Experiment does not have an instrument technique.");
			return ERROR_VIEW;
		}
		
		Authorities authorities = mergeAuthorities(projectAuthoritiesData.get(), sessionAuthoritiesData.get());
		
		model.put("instrumentTechniqueList", instrumentTechniqueOptionList);
		model.put("instrumentList", instrumentListData.get());
		model.put("sampleList", sampleListData.get());
		model.put("experiment", experimentFormBacker);
		model.put("authorities", authorities);
		model.put("project", project);
		model.put("session", session);
		return "frag/experiment";
	}
	
	protected List<InstrumentTechniqueOption> getInstrumentTechniqueOptionList(List<InstrumentTechnique> instrumentTechniqueList, List<Instrument> instrumentList, List<Technique> techniqueList) {
		List<InstrumentTechniqueOption> instrumentTechniqueOptionList = new ArrayList<InstrumentTechniqueOption>();
		for(InstrumentTechnique instrumentTechnique : instrumentTechniqueList) {
			for(Instrument instrument : instrumentList) {
				if(instrument.getGid().equals(instrumentTechnique.getInstrumentGid())) {
					for(Technique technique : techniqueList) {
						if(technique.getGid().equals(instrumentTechnique.getTechniqueGid())) {
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

	public TechniqueAuthzDAO getTechniqueAuthzDAO() {
		return techniqueAuthzDAO;
	}
	public void setTechniqueAuthzDAO(TechniqueAuthzDAO techniqueAuthzDAO) {
		this.techniqueAuthzDAO = techniqueAuthzDAO;
	}
}
