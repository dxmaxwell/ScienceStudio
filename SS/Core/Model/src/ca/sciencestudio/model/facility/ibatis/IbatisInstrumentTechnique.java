/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisInstrumentTechnique class.
 *     
 */
package ca.sciencestudio.model.facility.ibatis;

import ca.sciencestudio.model.facility.InstrumentTechnique;

/**
 * @author maxweld
 *
 */
public class IbatisInstrumentTechnique implements Cloneable, InstrumentTechnique {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private int instrumentId;
	private int techniqueId;
	
	@Override
	public int getId() {
		return id;
	}
	@Override
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public int getInstrumentId() {
		return instrumentId;
	}
	@Override
	public void setInstrumentId(int instrumentId) {
		this.instrumentId = instrumentId;
	}
	
	@Override
	public int getTechniqueId() {
		return techniqueId;
	}
	@Override
	public void setTechniqueId(int techniqueId) {
		this.techniqueId = techniqueId;
	}
	
	@Override
	public InstrumentTechnique clone() {
		try {
			return (InstrumentTechnique) super.clone();
		}
		catch(CloneNotSupportedException e) {
			return null;
		}
	}
}
