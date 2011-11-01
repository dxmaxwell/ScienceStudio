/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     InstrumentTechnique class.
 *     
 */
package ca.sciencestudio.model.facility;

import ca.sciencestudio.model.Model;
import ca.sciencestudio.model.utilities.GID;

/**
 * @author maxweld
 */
public class InstrumentTechnique implements Model {

	private static final long serialVersionUID = 1L;

	public static final String GID_TYPE = "Q";

	public static final String DEFAULT_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_INSTRUMENT_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_TECHNIQUE_GID = GID.DEFAULT_GID;
	
	private String gid = DEFAULT_GID;
	private String instrumentGid = DEFAULT_INSTRUMENT_GID;
	private String techniqueGid = DEFAULT_TECHNIQUE_GID;
	
	@Override
	public String getGid() {
		return gid;
	}
	@Override
	public void setGid(String gid) {
		this.gid = gid;
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
