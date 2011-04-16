/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Facility interface.
 *     
 */
package ca.sciencestudio.model.facility;

import java.io.Serializable;

/**
 * @author maxweld
 *
 */
public interface Facility extends Serializable {
	
	public int getId();
	public void setId(int id);
	
	public String getName();
	public void setName(String name);
	
	public String getLongName();
	public void setLongName(String longName);
	
	public String getDescription();
	public void setDescription(String description);
	
	public String getPhoneNumber();
	public void setPhoneNumber(String phoneNumber);
	
	public String getEmailAddress();
	public void setEmailAddress(String emailAddress);
	
	public String getLocation();
	public void setLocation(String location);
	
	public Facility clone();
}
