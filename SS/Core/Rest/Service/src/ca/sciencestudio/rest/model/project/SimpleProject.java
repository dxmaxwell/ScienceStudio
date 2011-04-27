/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SimpleProject class.
 *     
 */
package ca.sciencestudio.rest.model.project;

import java.util.Date;

import ca.sciencestudio.rest.model.project.Project;

/**
 * @author maxweld
 *
 */
public class SimpleProject implements Project {

	private String uid;
	private String name;
	private String description;
	private Date startDate;
	private Date endDate;
	private Status status;
	
	@Override
	public String getUid() {
		return uid;
	}
	@Override
	public void setUid(String uid) {
		this.uid = uid;
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
	public Status getStatus() {
		return status;
	}
	@Override
	public void setStatus(Status status) {
		this.status = status;
	}
}
