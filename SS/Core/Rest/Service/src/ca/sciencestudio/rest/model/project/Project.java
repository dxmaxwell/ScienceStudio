/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Project interface.
 *     
 */
package ca.sciencestudio.rest.model.project;

import java.util.Date;

/**
 * @author maxweld
 *
 */
public interface Project {

	public enum Status {
		ACTIVE, INACTIVE, UNKNOWN;
	}
	
	public String getUid();
	public void setUid(String uid);
	
	public String getName();
	public void setName(String name);
	
	public String getDescription();
	public void setDescription(String description);
	
	public Date getStartDate();
	public void setStartDate(Date startDate);
	
	public Date getEndDate();
	public void setEndDate(Date endDate);
	
	public Status getStatus();
	public void setStatus(Status status);
}
