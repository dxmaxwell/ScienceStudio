/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisInstrument class.
 *     
 */
package ca.sciencestudio.model.facility.dao.ibatis.support;

import ca.sciencestudio.model.facility.Instrument;

/**
 * @author maxweld
 *
 */
public class IbatisInstrument {
	
	public static final int DEFAULT_ID = 0;
	public static final int DEFAULT_LABORATORY_ID = 0; 
	
	private int id = DEFAULT_ID;
	private int laboratoryId = DEFAULT_LABORATORY_ID;
	private String name = Instrument.DEFAULT_NAME;
	private String longName = Instrument.DEFAULT_LONG_NAME;
	private String description = Instrument.DEFAULT_DESCRIPTION;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getLaboratoryId() {
		return laboratoryId;
	}
	public void setLaboratoryId(int laboratoryId) {
		this.laboratoryId = laboratoryId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLongName() {
		return longName;
	}
	public void setLongName(String longName) {
		this.longName = longName;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
