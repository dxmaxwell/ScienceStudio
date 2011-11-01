/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestSession class.
 *     
 */
package ca.sciencestudio.model.session.dao.rest.support;

import java.util.Date;

import ca.sciencestudio.model.session.validators.SessionValidator;

/**
 * @author maxweld
 * 
 *
 */
public class RestSession {

	private String projectGid = SessionValidator.DEFAULT_PROJECT_GID;
	private String laboratoryGid = SessionValidator.DEFAULT_LABORATORY_GID;
	private String name = SessionValidator.DEFAULT_NAME;
	private String description = SessionValidator.DEFAULT_DESCRIPTION;
	private String proposal = SessionValidator.DEFAULT_PROPOSAL;
	private Date startDate = SessionValidator.DEFAULT_START_DATE;
	private Date endDate = SessionValidator.DEFAULT_END_DATE;
	
	public String getProjectGid() {
		return projectGid;
	}
	public void setProjectGid(String projectGid) {
		this.projectGid = projectGid;
	}
	
	public String getLaboratoryGid() {
		return laboratoryGid;
	}
	public void setLaboratoryGid(String laboratoryGid) {
		this.laboratoryGid = laboratoryGid;
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
	
	public String getProposal() {
		return proposal;
	}
	public void setProposal(String proposal) {
		this.proposal = proposal;
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
}
