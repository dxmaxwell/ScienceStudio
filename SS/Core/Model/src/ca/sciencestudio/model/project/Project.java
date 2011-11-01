/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Project class.
 *     
 */
package ca.sciencestudio.model.project;

import java.util.Date;

import ca.sciencestudio.model.Model;
import ca.sciencestudio.model.project.validators.ProjectValidator;

/**
 * @author maxweld
 *
 */
public class Project implements Model {
	
	private static final long serialVersionUID = 1L;

	public static final String GID_TYPE = "P";
	
	public static enum Status { ACTIVE, INACTIVE }
	
	private String gid;
	private String name;
	private String description;
	private String facilityGid;
	private Date startDate;
	private Date endDate;
	private Status status;
	
	public Project() {
		gid = ProjectValidator.DEFAULT_GID;
		name = ProjectValidator.DEFAULT_NAME;
		description = ProjectValidator.DEFAULT_DESCRIPTION;
		facilityGid = ProjectValidator.DEFAULT_FACILITY_GID;
		startDate = ProjectValidator.DEFAULT_START_DATE;
		endDate = ProjectValidator.DEFAULT_END_DATE;
		status = ProjectValidator.DEFAULT_STATUS;
	}
	
	public Project(Project project) {
		gid = project.getGid();
		name = project.getName();
		description = project.getDescription();
		facilityGid = project.getFacilityGid();
		startDate = project.getStartDate();
		endDate = project.getEndDate();
		status = project.getStatus();
	}
	
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
		else if(facilityGid == null || !facilityGid.equals(project.getFacilityGid())) {
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
		buffer.append("\", facilityGid:").append(facilityGid);
		buffer.append(", startDate:\"").append(startDate);
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
	
	public String getFacilityGid() {
		return facilityGid;
	}
	public void setFacilityGid(String facilityGid) {
		this.facilityGid = facilityGid;
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

	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
}
