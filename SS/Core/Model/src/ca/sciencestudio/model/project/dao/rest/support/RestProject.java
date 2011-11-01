/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestProject class.
 *     
 */
package ca.sciencestudio.model.project.dao.rest.support;

import java.util.Date;

import ca.sciencestudio.model.project.Project.Status;
import ca.sciencestudio.model.project.validators.ProjectValidator;

/**
 * @author maxweld
 *
 */
public class RestProject {

	private String name = ProjectValidator.DEFAULT_NAME;
	private String description = ProjectValidator.DEFAULT_DESCRIPTION;
	private Date startDate = ProjectValidator.DEFAULT_START_DATE;
	private Date endDate = ProjectValidator.DEFAULT_END_DATE;
	private Status status = ProjectValidator.DEFAULT_STATUS;
	
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
	
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
}
