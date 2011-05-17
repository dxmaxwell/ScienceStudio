/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Project class.
 *     
 */
package ca.sciencestudio.model;

import java.util.Date;

import ca.sciencestudio.model.Model;
import ca.sciencestudio.model.utilities.GID;

/**
 * @author maxweld
 *
 */
public final class Project implements Model {
	
	private static final long serialVersionUID = 1L;

	public static final String GID_TYPE = "P";
	
	public static enum Status { ACTIVE, INACTIVE }
	
	// Maximum field length should match database limits. //
	public static final int MAX_LENGTH_NAME = 80;
	public static final int MAX_LENGTH_DESCRIPTION = 255;
	public static final int MAX_LENGTH_STATUS = 20;
	////////////////////////////////////////////////////////
	
	public static final String DEFAULT_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_NAME = "";
	public static final String DEFAULT_DESCRIPTION = ""; 
	public static final Date DEFAULT_START_DATE = new Date(0);
	public static final Date DEFAULT_END_DATE = new Date(0);
	public static final String DEFAULT_STATUS = "UNKNOWN";
	
	private String gid = DEFAULT_GID;
	private String name = DEFAULT_NAME;
	private String description = DEFAULT_DESCRIPTION;
	private Date startDate = DEFAULT_START_DATE;
	private Date endDate = DEFAULT_END_DATE;
	private String status = DEFAULT_STATUS;	
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Project)) {
			return false;
		}
		Project project = (Project)obj;
		if(gid == null || !gid.equals(project.getGid())) {
			return false;
		}
		else if(name == null || !name.equals(project.getName())) {
			return false;
		}
		else if(description == null || !description.equals(project.getDescription())) {
			return false;
		}
		else if(startDate == null || !startDate.equals(project.getStartDate())) {
			return false;
		}
		else if(endDate == null || !endDate.equals(project.getEndDate())) {
			return false;
		}
		else if(status == null || !status.equals(project.getStatus())) {
			return false;
		}
		else {
			return true;
		}
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[ gid:").append(gid);
		buffer.append(", name:\"").append(name);
		buffer.append("\", description:\"").append(description);
		buffer.append("\", startDate:\"").append(startDate);
		buffer.append("\", endDate:\"").append(endDate);
		buffer.append("\", status:").append(status);
		buffer.append(" ]");
		return buffer.toString();
	}
	
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
