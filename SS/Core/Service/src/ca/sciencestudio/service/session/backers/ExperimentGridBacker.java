/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ExperimentGridBacker class.
 *     
 */
package ca.sciencestudio.service.session.backers;

import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.facility.Instrument;
import ca.sciencestudio.model.facility.Technique;
import ca.sciencestudio.model.session.Experiment;

/**
 * @author maxweld
 *
 */
public class ExperimentGridBacker {

	private int id;
	private int sessionId;
	private String name;
	private String description;
	
	private int projectId;
	private String sampleName;
	private String techniqueName;
	private String instrumentName;
	
	public ExperimentGridBacker(Experiment experiment, int projectId, Sample sample, Instrument instrument, Technique technique) {
		setId(experiment.getId());
		setSessionId(experiment.getSessionId());
		setName(experiment.getName());
		setDescription(experiment.getDescription());
		
		setProjectId(projectId);
		setSampleName(sample.getName());
		setTechniqueName(technique.getName());
		setInstrumentName(instrument.getName());
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getSessionId() {
		return sessionId;
	}
	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
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

	public String getSampleName() {
		return sampleName;
	}
	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}

	public String getTechniqueName() {
		return techniqueName;
	}
	public void setTechniqueName(String techniqueName) {
		this.techniqueName = techniqueName;
	}

	public String getInstrumentName() {
		return instrumentName;
	}
	public void setInstrumentName(String instrumentName) {
		this.instrumentName = instrumentName;
	}
}
