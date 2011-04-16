/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisTechnique class.
 *     
 */
package ca.sciencestudio.model.facility.ibatis;

import ca.sciencestudio.model.facility.Technique;

/**
 * @author maxweld
 *
 */
public class IbatisTechnique implements Cloneable, Technique {

	private static final long serialVersionUID = 1L;
	
	private int id;
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
	public Technique clone() {
		try {
			return (Technique) super.clone();
		}
		catch(CloneNotSupportedException e) {
			return null;
		}
	}
}
