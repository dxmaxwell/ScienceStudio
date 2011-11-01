/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ExperimentFormBacker class.
 *     
 */
package ca.sciencestudio.service.session.backers;

import ca.sciencestudio.model.facility.InstrumentTechnique;
import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.utilities.GID;
import ca.sciencestudio.util.rest.ValidationResult;

/**
 * @author ExperimentFormBacker
 *
 */
public class ExperimentFormBacker extends Experiment {
	
	private static final long serialVersionUID = 1L;
	
	private String instrumentGid;
	private String techniqueGid;
	
	public static ValidationResult transformResult(ValidationResult result) {
		return result;
	}
	
	public ExperimentFormBacker() {
		setInstrumentGid(GID.DEFAULT_GID);
		setTechniqueGid(GID.DEFAULT_GID);
	}
	
//	public ExperimentFormBacker(int sessionId) {
//		setId(0);
//		setName("");
//		setDescription("");
//		setSampleId(0);
//		setSessionId(sessionId);
//		setInstrumentId(0);
//		setInstrumentTechniqueId(0);
//	}
	
	public ExperimentFormBacker(Experiment experiment, InstrumentTechnique instrumentTechnique) {
		super(experiment);
		
		if(!getInstrumentTechniqueGid().equals(instrumentTechnique.getGid())) {
			throw new IllegalArgumentException();
		}
		
		setInstrumentGid(instrumentTechnique.getInstrumentGid());
		setTechniqueGid(instrumentTechnique.getTechniqueGid());
	}

	public String getInstrumentGid() {
		return instrumentGid;
	}
	public void setInstrumentGid(String instrumentGid) {
		this.instrumentGid = instrumentGid;
	}

	public String getTechniqueGid() {
		return techniqueGid;
	}
	public void setTechniqueGid(String techniqueGid) {
		this.techniqueGid = techniqueGid;
	}
}
