/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Experiment class.
 *     
 */
package ca.sciencestudio.model.session;

import ca.sciencestudio.model.Model;
import ca.sciencestudio.model.utilities.GID;

/**
 * @author maxweld
 *
 */
public class Experiment implements Model {
	
	private static final long serialVersionUID = 1L;
	
	public static final String GID_TYPE = "E";
	
	public static final String DEFAULT_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_SESSION_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_INSTRUMENT_TECHNIQUE_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_NAME = "";
	public static final String DEFAULT_DESCRIPTION = "";
	public static final String DEFAULT_SOURCE_GID = GID.DEFAULT_GID;
	
	private String gid = DEFAULT_GID;
	private String sessionGid = DEFAULT_SESSION_GID;
	private String instrumentTechniqueGid = DEFAULT_INSTRUMENT_TECHNIQUE_GID;
	private String name = DEFAULT_NAME;
	private String description = DEFAULT_DESCRIPTION;
	private String sourceGid = DEFAULT_SOURCE_GID;
	
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
	
	public String getInstrumentTechniqueGid() {
		return instrumentTechniqueGid;
	}
	public void setInstrumentTechniqueGid(String instrumentTechniqueGid) {
		this.instrumentTechniqueGid = instrumentTechniqueGid;
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
