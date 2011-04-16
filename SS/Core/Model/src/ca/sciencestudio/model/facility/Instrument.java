/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Instrument interface.
 *     
 */
package ca.sciencestudio.model.facility;

import java.io.Serializable;

/**
 * @author maxweld
 */
public interface Instrument extends Serializable {

	public int getId();
	public void setId(int id);
	
	public int getLaboratoryId();
	public void setLaboratoryId(int laboratoryId);
	
	public String getName();
	public void setName(String name);
	
	public String getLongName();
	public void setLongName(String longName);
	
	public String getDescription();
	public void setDescription(String description);
	
	public Instrument clone();
}
