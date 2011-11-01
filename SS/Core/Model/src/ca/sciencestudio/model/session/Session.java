/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Session class.
 *     
 */
package ca.sciencestudio.model.session;

import java.util.Date;

import ca.sciencestudio.model.Model;
import ca.sciencestudio.model.session.validators.SessionValidator;

/**
 * @author maxweld
 *
 */
public class Session implements Model {

	private static final long serialVersionUID = 1L;
	
	public static final String GID_TYPE = "S";
	
	private String gid;
	private String projectGid;
	private String laboratoryGid;
	private String name;
	private String description;
	private String proposal;
	private Date startDate;
	private Date endDate;
	
	public Session() {
		gid = SessionValidator.DEFAULT_GID;
		projectGid = SessionValidator.DEFAULT_PROJECT_GID;
		laboratoryGid = SessionValidator.DEFAULT_LABORATORY_GID;
		name = SessionValidator.DEFAULT_NAME;
		description = SessionValidator.DEFAULT_DESCRIPTION;
		proposal = SessionValidator.DEFAULT_PROPOSAL;
		startDate = SessionValidator.DEFAULT_START_DATE;
		endDate = SessionValidator.DEFAULT_END_DATE;
	}
	
	public Session(Session session) {
		gid = session.getGid();
		projectGid = session.getProjectGid();
		laboratoryGid = session.getLaboratoryGid();
		name = session.getName();
		description = session.getDescription();
		proposal = session.getProposal();
		startDate = session.getStartDate();
		endDate = session.getEndDate();
	}
	
	@Override
	public String getGid() {
		return gid;
	}
	@Override
	public void setGid(String gid) {
		this.gid = gid;
	}
	
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
