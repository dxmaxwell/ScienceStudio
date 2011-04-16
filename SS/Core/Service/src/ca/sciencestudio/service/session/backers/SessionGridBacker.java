/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *  	SessionGridBacker class.
 *
 */
package ca.sciencestudio.service.session.backers;

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
public class SessionGridBacker {

	private int sessionId;
	private int projectId;
	private int laboratoryId;
	private String projectName;
	private String sessionName;
	private String description;
	private String proposal;
	private String startDate;
	private String endDate;
	
	public SessionGridBacker(Project project, Session session) {
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
			return BindAndValidateUtils.DATE_FORMAT_ISO8601_SHRT.parse(getStartDate());
		}
		catch(ParseException e) {
			return null;
		}
	}
	public void setRawStartDate(Date date) {
		setStartDate(BindAndValidateUtils.DATE_FORMAT_ISO8601_SHRT.format(date));
	}
	
	public Date getRawEndDate() {
		try {
			return BindAndValidateUtils.DATE_FORMAT_ISO8601_SHRT.parse(getEndDate());
		}
		catch(ParseException e) {
			return null;
		}
	}
	public void setRawEndDate(Date date) {
		setEndDate(BindAndValidateUtils.DATE_FORMAT_ISO8601_SHRT.format(date));
	}
	
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	
	public int getSessionId() {
		return sessionId;
	}
	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
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
	
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getLaboratoryId() {
		return laboratoryId;
	}
	public void setLaboratoryId(int laboratoryId) {
		this.laboratoryId = laboratoryId;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
