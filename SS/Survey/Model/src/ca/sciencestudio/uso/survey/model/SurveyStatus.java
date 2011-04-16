package ca.sciencestudio.uso.survey.model;

public enum SurveyStatus {
	CREATED(0, "Created"),
	STARTED(1, "Started");
	
	private int value;
	private String text;
	
	private SurveyStatus(int value, String text) {
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
