/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisInstrument interface.
 *     
 */
package ca.sciencestudio.model.facility.ibatis;

import ca.sciencestudio.model.facility.Instrument;

/**
 * @author maxweld
 *
 */
public class IbatisInstrument implements Cloneable, Instrument {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private int laboratoryId;
	private String name;
	private String longName;
	private String description;
	
	@Override
	public int getId() {
		return id;
	}
	@Override
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public int getLaboratoryId() {
		return laboratoryId;
	}
	@Override
	public void setLaboratoryId(int laboratoryId) {
		this.laboratoryId = laboratoryId;
	}
	
	@Override
	public String getName() {
		return name;
	}
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getLongName() {
		return longName;
	}
	@Override
	public void setLongName(String longName) {
		this.longName = longName;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	@Override
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public Instrument clone() {
		try {
			return (Instrument) super.clone();
		}
		catch(CloneNotSupportedException e) {
			return null;
		}	
	}
}
