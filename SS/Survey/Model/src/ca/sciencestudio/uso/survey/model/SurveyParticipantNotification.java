package ca.sciencestudio.uso.survey.model;

import java.io.Serializable;


public class SurveyParticipantNotification implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int participantId;
	private int notificationType;

	public int getParticipantId() {
		return participantId;
	}

	public void setParticipantId(int participantId) {
		this.participantId = participantId;
	}
	
	public int getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(int notificationType) {
		this.notificationType = notificationType;
	}
}

