/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionFormBacker class.
 *     
 */
package ca.sciencestudio.service.session.backers;

import java.text.ParseException;
import java.util.Date;

import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.SessionDAO;
import ca.sciencestudio.util.web.BindAndValidateUtils;

/**
 * @author maxweld
 *
 */
public class SessionFormBacker {
		
	private int id;
	private int projectId;
	private int laboratoryId;
	private String name;
	private String description;
	private String proposal;
	private String startDate;
	private String endDate;
	private String startTime;
	private String endTime;
	
	public SessionFormBacker(int projectId) {
		setId(0);
		setProjectId(projectId);
		setLaboratoryId(0);
		setName("");
		setDescription("");
		setProposal("");
		setStartDate("");
		setEndDate("");
		setStartTime("");
		setEndTime("");
	}
	
	public SessionFormBacker(Session session) {
		setId(session.getId());
		setProjectId(session.getProjectId());
		setLaboratoryId(session.getLaboratoryId());
		setName(session.getName());
		setDescription(session.getDescription());
		setProposal(session.getProposal());
		setRawStartDate(session.getStartDate());
		setRawEndDate(session.getEndDate());
	}
	
	public Session createSession(SessionDAO sessionDAO) {
		Session session = sessionDAO.createSession();
		session.setId(getId());
		session.setProjectId(getProjectId());
		session.setLaboratoryId(getLaboratoryId());
		session.setName(getName());
		session.setDescription(getDescription());
		session.setProposal(getProposal());
		session.setStartDate(getRawStartDate());
		session.setEndDate(getRawEndDate());
		return session;
	}
	
	public Date getRawStartDate() {
		try {
			return BindAndValidateUtils.DATE_FORMAT_ISO8601_SHRT.parse(getStartDate() + " " + getStartTime());
		}
		catch(ParseException e) {
			return null;
		}
	}
	public void setRawStartDate(Date date) {
		setStartDate(BindAndValidateUtils.DATE_FORMAT_ISO8601_DATE.format(date));
		setStartTime(BindAndValidateUtils.DATE_FORMAT_ISO8601_TIME_SHRT.format(date));
	}
	
	public Date getRawEndDate() {
		try {
			return BindAndValidateUtils.DATE_FORMAT_ISO8601_SHRT.parse(getEndDate() + " " + getEndTime());
		}
		catch(ParseException e) {
			return null;
		}
	}
	public void setRawEndDate(Date date) {
		setEndDate(BindAndValidateUtils.DATE_FORMAT_ISO8601_DATE.format(date));
		setEndTime(BindAndValidateUtils.DATE_FORMAT_ISO8601_TIME_SHRT.format(date));
	}
	
	public int getId() {
		return id;
	}
	private void setId(int id) {
		this.id = id;
	}
	
	public int getProjectId() {
		return projectId;
	}
	private void setProjectId(int projectId) {
		this.projectId = projectId;
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
	
	public int getLaboratoryId() {
		return laboratoryId;
	}
	public void setLaboratoryId(int laboratoryId) {
		this.laboratoryId = laboratoryId;
	}
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
