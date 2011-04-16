/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ExperimentBacker class.
 *     
 */
package ca.sciencestudio.service.session.backers;

import ca.sciencestudio.model.facility.InstrumentTechnique;
import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.dao.ExperimentDAO;

/**
 * @author ExperimentFormBacker
 *
 */
public class ExperimentFormBacker {
	
	private int id;
	private String name;
	private String description;
	private int sampleId;
	private int sessionId;
	private int instrumentId;
	private int instrumentTechniqueId;
	
	public ExperimentFormBacker(int sessionId) {
		setId(0);
		setName("");
		setDescription("");
		setSampleId(0);
		setSessionId(sessionId);
		setInstrumentId(0);
		setInstrumentTechniqueId(0);
	}
	
	public ExperimentFormBacker(Experiment experiment, InstrumentTechnique instrumentTechnique) {
		if(experiment.getInstrumentTechniqueId() != instrumentTechnique.getId()) {
			throw new IllegalArgumentException();
		}
		
		setId(experiment.getId());
		setName(experiment.getName());
		setDescription(experiment.getDescription());
		setSampleId(experiment.getSampleId());
		setSessionId(experiment.getSessionId());
		
		setInstrumentId(instrumentTechnique.getInstrumentId());
		setInstrumentTechniqueId(instrumentTechnique.getId());
	}

	public Experiment createExperiment(ExperimentDAO experimentDAO) {
		Experiment experiment = experimentDAO.createExperiment();
		experiment.setId(getId());
		experiment.setName(getName());
		experiment.setDescription(getDescription());
		experiment.setSampleId(getSampleId());
		experiment.setSessionId(getSessionId());
		experiment.setInstrumentTechniqueId(getInstrumentTechniqueId());
		return experiment;
	}

	public int getId() {
		return id;
	}
	private void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public int getSampleId() {
		return sampleId;
	}
	public void setSampleId(int sampleId) {
		this.sampleId = sampleId;
	}

	public int getSessionId() {
		return sessionId;
	}
	private void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	public int getInstrumentId() {
		return instrumentId;
	}
	public void setInstrumentId(int instrumentId) {
		this.instrumentId = instrumentId;
	}
	
	public int getInstrumentTechniqueId() {
		return instrumentTechniqueId;
	}
	public void setInstrumentTechniqueId(int instrumentTechniqueId) {
		this.instrumentTechniqueId = instrumentTechniqueId;
	}
}
