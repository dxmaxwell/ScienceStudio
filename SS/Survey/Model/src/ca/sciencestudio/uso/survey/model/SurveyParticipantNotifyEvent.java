package ca.sciencestudio.uso.survey.model;

import java.io.Serializable;
import java.util.Date;


public class SurveyParticipantNotifyEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private int participantId;
	private int personId;
	private String event;
	private Date recordTime;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getParticipantId() {
		return participantId;
	}

	public void setParticipantId(int participantId) {
		this.participantId = participantId;
	}
	
	public int getPersonId() {
		return personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}

	public String getEvent(){
		return event;
	}

	public void setEvent(String event){
		this.event = event;
	}
	
	public Date getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}	
}

