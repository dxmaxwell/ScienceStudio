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
public class ExperimentGridBacker extends Experiment {

	private static final long serialVersionUID = 1L;
	
	private String sampleName;
	private String techniqueName;
	private String instrumentName;
	
	public ExperimentGridBacker(Experiment experiment, Sample sample, Instrument instrument, Technique technique) {
		super(experiment);
		setSampleName(sample.getName());
		setTechniqueName(technique.getName());
		setInstrumentName(instrument.getName());
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
