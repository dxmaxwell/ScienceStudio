/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Account interface.
 *     
 */
package ca.sciencestudio.login.model;

import java.util.Date;

/**
 * @author maxweld
 *
 */
public interface Account {

	public static enum Status {
		ACTIVE, DISABLED, EXPIRED, UNKNOWN
	}
	
	public int getId();
	public void setId(int id);
	
	public String getUsername();
	public void setUsername(String username);
	
	public String getPassword();
	public void setPassword(String password);
	
	public String getPersonUid();
	public void setPersonUid(String personUid);
		
	public Status getStatus();
	public void setStatus(Status status);
	
	public Date getCreationDate();
}
