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
import ca.sciencestudio.model.utilities.GID;

/**
 * @author maxweld
 *
 */
public class Session implements Model {

	private static final long serialVersionUID = 1L;
	
	public static final String GID_TYPE = "S";
	
	public static final String DEFAULT_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_PROJECT_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_LABORATORY_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_NAME = "";
	public static final String DEFAULT_DESCRIPTION = "";
	public static final String DEFAULT_PROPOSAL = "";
	public static final Date DEFAULT_START_DATE = new Date(0);
	public static final Date DEFAULT_END_DATE = new Date(0);
	
	private String gid = DEFAULT_GID;
	private String projectGid = DEFAULT_PROJECT_GID;
	private String laboratoryGid = DEFAULT_LABORATORY_GID;
	private String name = DEFAULT_NAME;
	private String description = DEFAULT_DESCRIPTION;
	private String proposal = DEFAULT_PROPOSAL;
	private Date startDate = DEFAULT_START_DATE;
	private Date endDate = DEFAULT_END_DATE;
	
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
