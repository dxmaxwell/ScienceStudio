/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisSession class.
 *     
 */
package ca.sciencestudio.model.session.ibatis;

import java.util.Date;

import ca.sciencestudio.model.session.Session;

/**
 * @author maxweld
 *
 */
public class IbatisSession implements Cloneable, Session {
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String name;
	private String description;
	private String proposal;
	private Date startDate;
	private Date endDate;
	private int projectId;
	private int laboratoryId;
	
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
	public String getProposal() {
		return proposal;
	}
	@Override
	public void setProposal(String proposal) {
		this.proposal = proposal;
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
	public int getProjectId() {
		return projectId;
	}
	@Override
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	
	@Override
	public int getLaboratoryId() {
		return laboratoryId;
	}
	@Override
	public void setLaboratoryId(int laboratoryId) {
		this.laboratoryId = laboratoryId;
	}
	
	public Session clone() {
		try {
			return (Session) super.clone();
		}
		catch (CloneNotSupportedException e) {
			return null;
		}
	}
}
