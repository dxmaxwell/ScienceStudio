package ca.sciencestudio.uso.survey.service.backers;

import java.util.Date;

public class SurveyParticipantListBacker {
	private int id;
	private String personName;
	private String beamline;
	private String description;
	private String status;
	private Date notificationDate;
	private Date remindDate1;
	private Date remindDate2;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPersonName(){
		return personName;
	}

	public void setPersonName(String personName){
		this.personName = personName;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
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

