/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisSession class.
 *     
 */
package ca.sciencestudio.model.session.dao.ibatis.support;

import java.util.Date;

import ca.sciencestudio.model.session.Session;

/**
 * @author maxweld
 *
 */
public class IbatisSession {
	
	public static final int DEFAULT_ID = 0;
	public static final int DEFAULT_PROJECT_ID = 0;
	public static final int DEFAULT_LABORATORY_ID = 0;
	
	private int id = DEFAULT_ID;
	private int laboratoryId = DEFAULT_LABORATORY_ID;
	private String projectGid = Session.DEFAULT_PROJECT_GID;
	private String name = Session.DEFAULT_NAME;
	private String description = Session.DEFAULT_DESCRIPTION;
	private String proposal = Session.DEFAULT_PROPOSAL;
	private Date startDate = Session.DEFAULT_START_DATE;
	private Date endDate = Session.DEFAULT_END_DATE;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getLaboratoryId() {
		return laboratoryId;
	}
	public void setLaboratoryId(int laboratoryId) {
		this.laboratoryId = laboratoryId;
	}
	
	public String getProjectGid() {
		return projectGid;
	}
	public void setProjectGid(String projectGid) {
		this.projectGid = projectGid;
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
