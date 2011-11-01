/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Experiment class.
 *     
 */
package ca.sciencestudio.model.session;

import ca.sciencestudio.model.Model;
import ca.sciencestudio.model.session.validators.ExperimentValidator;

/**
 * @author maxweld
 *
 */
public class Experiment implements Model {
	
	private static final long serialVersionUID = 1L;
	
	public static final String GID_TYPE = "E";
	
	private String gid;
	private String sessionGid;
	private String name;
	private String description;
	private String sourceGid;
	private String instrumentTechniqueGid;
	
	public Experiment() {
		gid = ExperimentValidator.DEFAULT_GID;
		sessionGid = ExperimentValidator.DEFAULT_SESSION_GID;
		instrumentTechniqueGid = ExperimentValidator.DEFAULT_INSTRUMENT_TECHNIQUE_GID;
		name = ExperimentValidator.DEFAULT_NAME;
		description = ExperimentValidator.DEFAULT_DESCRIPTION;
		sourceGid = ExperimentValidator.DEFAULT_SOURCE_GID;
	}
	
	public Experiment(Experiment experiment) {
		gid = experiment.getGid();
		sessionGid = experiment.getSessionGid();
		instrumentTechniqueGid = experiment.getInstrumentTechniqueGid();
		name = experiment.getName();
		description = experiment.getDescription();
		sourceGid = experiment.getSourceGid();
	}
	
	@Override
	public String getGid() {
		return gid;
	}
	@Override
	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getSessionGid() {
		return sessionGid;
	}
	public void setSessionGid(String sessionGid) {
		this.sessionGid = sessionGid;
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
	
	public String getSourceGid() {
		return sourceGid;
	}
	public void setSourceGid(String sourceGid) {
		this.sourceGid = sourceGid;
	}
	
	public String getInstrumentTechniqueGid() {
		return instrumentTechniqueGid;
	}
	public void setInstrumentTechniqueGid(String instrumentTechniqueGid) {
		this.instrumentTechniqueGid = instrumentTechniqueGid;
	}
}
