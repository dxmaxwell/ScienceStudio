/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      LaboratorySessionBacker class.	     
 */
package ca.sciencestudio.nanofab.admin.service.backers;

import java.text.ParseException;
import java.util.Date;

import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.SessionDAO;
import ca.sciencestudio.util.web.BindAndValidateUtils;

/**
 * @author maxweld
 *
 */
public class LaboratorySessionBacker {
	
	private static final String DEFAULT_STATUS = "UNKNOWN";
	
	private int sessionId;
	private int projectId;
	private int laboratoryId;
	private String projectName;
	private String sessionName;
	private String description;
	private String proposal;
	private String startDate;
	private String startTime;
	private String endDate;
	private String endTime;
	private String status;
	
	public LaboratorySessionBacker(Project project, Session session) {
		if(project.getId() != session.getProjectId()) { 
			throw new IllegalArgumentException();
		}
		
		setSessionId(session.getId());
		setProjectId(project.getId());
		setLaboratoryId(session.getLaboratoryId());
		setSessionName(session.getName());		
		setProjectName(project.getName());
		setDescription(session.getDescription());
		setProposal(session.getProposal());
		setRawStartDate(session.getStartDate());
		setRawEndDate(session.getEndDate());
		setStatus(DEFAULT_STATUS);	
	}
	
	public Session createSession(SessionDAO sessionDAO) {
		Session session = sessionDAO.createSession();
		session.setId(getSessionId());
		session.setProjectId(getProjectId());
		session.setLaboratoryId(getLaboratoryId());
		session.setName(getSessionName());
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
	
	public int getSessionId() {
		return sessionId;
	}
	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}
	
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	
	public int getLaboratoryId() {
		return laboratoryId;
	}
	public void setLaboratoryId(int laboratoryId) {
		this.laboratoryId = laboratoryId;
	}
	
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public String getSessionName() {
		return sessionName;
	}
	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
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
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
