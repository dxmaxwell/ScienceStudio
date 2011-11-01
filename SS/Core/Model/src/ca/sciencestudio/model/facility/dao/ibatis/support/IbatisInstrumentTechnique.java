/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisInstrumentTechnique class.
 *     
 */
package ca.sciencestudio.model.facility.dao.ibatis.support;

/**
 * @author maxweld
 *
 */
public class IbatisInstrumentTechnique {

	private static final long serialVersionUID = 1L;
	
	public static final int DEFAULT_ID = 0;
	public static final int DEFAULT_INSTRUMENT_ID = 0;
	public static final int DEFAULT_TECHNIQUE_ID = 0;
	
	private int id = DEFAULT_ID;
	private int instrumentId = DEFAULT_TECHNIQUE_ID;
	private int techniqueId = DEFAULT_INSTRUMENT_ID;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getInstrumentId() {
		return instrumentId;
	}
	public void setInstrumentId(int instrumentId) {
		this.instrumentId = instrumentId;
	}
	
	public int getTechniqueId() {
		return techniqueId;
	}
	public void setTechniqueId(int techniqueId) {
		this.techniqueId = techniqueId;
	}
}
