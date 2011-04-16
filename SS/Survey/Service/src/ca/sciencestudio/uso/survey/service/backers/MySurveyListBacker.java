package ca.sciencestudio.uso.survey.service.backers;

import java.util.Date;


public class MySurveyListBacker {
	private int surveyId;
	private String surveyName;
	private Date deadline;
	private boolean isValid;
	private int participantId;
	private String beamline;
	private String status;
	
	public int getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(int surveyId) {
		this.surveyId = surveyId;
	}

	public String getSurveyName() {
		return surveyName;
	}

	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date date) {
		this.deadline = date;
	}

	public boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(boolean isValid) {
		this.isValid = isValid;
	}
	
	public int getParticipantId() {
		return participantId;
	}

	public void setParticipantId(int participantId) {
		this.participantId = participantId;
	}

	public String getBeamline() {
		return beamline;
	}

	public void setBeamline(String beamline) {
		this.beamline = beamline;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}

