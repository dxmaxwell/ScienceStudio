/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestProject class.
 *     
 */
package ca.sciencestudio.model.dao.rest.support;

import java.util.Date;

import ca.sciencestudio.model.Project;

/**
 * @author maxweld
 *
 */
public class RestProject {

	private String name = Project.DEFAULT_NAME;
	private String description = Project.DEFAULT_DESCRIPTION;
	private Date startDate = Project.DEFAULT_START_DATE;
	private Date endDate = Project.DEFAULT_END_DATE;
	private String status = Project.DEFAULT_STATUS;
	
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
