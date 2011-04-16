package ca.sciencestudio.uso.survey.model;

public enum SurveyQuestionType {
	MULTICHOICE(0, "Multi-Choices"),
	MULTICHOICEANDOTHER(1, "Multi-Choises+Other"),
	PLAINTEXT(2, "PlainText");
	
	private int value;
	private String text;
	
	private SurveyQuestionType(int value, String text) {
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
