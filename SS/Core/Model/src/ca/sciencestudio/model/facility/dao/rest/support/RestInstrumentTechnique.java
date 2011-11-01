/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestInstrumentTechnique class.
 *     
 */
package ca.sciencestudio.model.facility.dao.rest.support;

import ca.sciencestudio.model.facility.InstrumentTechnique;

/**
 * @author maxweld
 * 
 *
 */
public class RestInstrumentTechnique {

	private String instrumentGid = InstrumentTechnique.DEFAULT_INSTRUMENT_GID;
	private String techniqueGid = InstrumentTechnique.DEFAULT_TECHNIQUE_GID;
		
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
