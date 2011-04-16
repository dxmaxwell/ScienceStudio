package ca.sciencestudio.uso.survey.model;

import java.io.Serializable;
import java.util.Date;


public class SurveyParticipant implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private int surveyId;
	private int personId;
	private String beamline;
	private String description;
	private int status;
	private Date notificationDate;
	private Date remindDate1;
	private Date remindDate2;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(int surveyId) {
		this.surveyId = surveyId;
	}

	public int getPersonId(){
		return personId;
	}

	public void setPersonId(int personId){
		this.personId = personId;
	}

	public String getBeamline() {
		return beamline;
	}

	public void setBeamline(String beamline) {
		this.beamline = beamline;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getNotificationDate() {
		return notificationDate;
	}

	public void setNotificationDate(Date notificationDate) {
		this.notificationDate = notificationDate;
	}

	public Date getRemindDate1() {
		return remindDate1;
	}

	public void setRemindDate1(Date remindDate1) {
		this.remindDate1 = remindDate1;
	}

	public Date getRemindDate2() {
		return remindDate2;
	}

	public void setRemindDate2(Date remindDate2) {
		this.remindDate2 = remindDate2;
	}
	
}

