/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisExperiment class.
 *     
 */
package ca.sciencestudio.model.session.dao.ibatis.support;

import ca.sciencestudio.model.session.validators.ExperimentValidator;
import ca.sciencestudio.model.utilities.GID;

/**
 * @author maxweld
 *
 */
public class IbatisExperiment {
	
	public static final int DEFAULT_ID = GID.DEFAULT_ID;
	public static final int DEFAULT_SESSION_ID = GID.DEFAULT_ID;
	public static final int DEFAULT_INSTRUMENT_TECHNIQUE_ID = GID.DEFAULT_ID;
	
	private int id = DEFAULT_ID;
	private int sessionId = DEFAULT_SESSION_ID;
	private int instrumentTechniqueId = DEFAULT_INSTRUMENT_TECHNIQUE_ID;
	private String name = ExperimentValidator.DEFAULT_NAME;
	private String description = ExperimentValidator.DEFAULT_DESCRIPTION;
	private String sourceGid = ExperimentValidator.DEFAULT_SOURCE_GID;
	
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
	
	public int getInstrumentTechniqueId() {
		return instrumentTechniqueId;
	}
	public void setInstrumentTechniqueId(int instrumentTechniqueId) {
		this.instrumentTechniqueId = instrumentTechniqueId;
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
}
