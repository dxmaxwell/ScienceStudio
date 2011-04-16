/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Person interface.
 *     
 */
package ca.sciencestudio.model.person;

import java.io.Serializable;
import java.util.Date;

/*
 * @author maxweld
 */
public interface Person extends Serializable {
	
	public int getId();
	public void setId(int id);
	
	public String getUid();
	public void setUid(String uid);
	
	public String getTitle();
	public void setTitle(String title);
	
	public String getFirstName();
	public void setFirstName(String firstName);
	
	public String getMiddleName();
	public void setMiddleName(String middleName);
	
	public String getLastName();
	public void setLastName(String lastName);
	
	public String getPhoneNumber();
	public void setPhoneNumber(String phoneNumber);
	
	public String getMobileNumber();
	public void setMobileNumber(String mobileNumber);
	
	public String getEmailAddress();
	public void setEmailAddress(String emailAddress);
	
	public Date getModificationDate();
}
