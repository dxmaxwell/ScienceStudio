package ca.sciencestudio.uso.survey.model;

public enum SurveyParticipantStatus {
	CREATED(0, "Created"),
	STARTED(1, "Started"),
	SUBMITTED(2, "Submitted");
	
	private int value;
	private String text;
	
	private SurveyParticipantStatus(int value, String text) {
		this.value = value;
		this.text = text;
	}
	
	public int getValue() {
		return value;
	}
	
	public String getText() {
		return text;
	}
}
