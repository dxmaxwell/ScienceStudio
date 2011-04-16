/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisProject class.
 *     
 */
package ca.sciencestudio.model.project.ibatis;

import java.util.Date;

import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.ProjectStatus;

/**
 * @author maxweld
 *
 */
public class IbatisProject implements Project {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String name;
	private String description;
	private Date startDate;
	private Date endDate;
	private ProjectStatus status;
	
	public String getStatusString() {
		return status.name();
	}
	public void setStatusString(String status) {
		try {
			this.status = ProjectStatus.valueOf(status.toUpperCase());
		}
		catch(Exception e) {
			this.status = ProjectStatus.UNKNOWN;
		}	
	}
	
	@Override
	public Project clone() {
		try {
			return (Project) super.clone();
		}
		catch(CloneNotSupportedException e) {
			return null;
		}
	}
	
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
	public String getDescription() {
		return description;
	}
	@Override
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public Date getStartDate() {
		return startDate;
	}
	@Override
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@Override
	public Date getEndDate() {
		return endDate;
	}
	@Override
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@Override
	public ProjectStatus getStatus() {
		return status;
	}
	@Override
	public void setStatus(ProjectStatus status) {
		this.status = status;
	}
}
