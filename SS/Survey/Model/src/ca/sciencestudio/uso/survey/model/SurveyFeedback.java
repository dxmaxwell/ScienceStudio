package ca.sciencestudio.uso.survey.model;

import java.io.Serializable;


public class SurveyFeedback implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private int questionId;
	private int participantId;
	private int choice;
	private String answer;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getQuestionId(){
		return questionId;
	}
	
	public void setQuestionId(int questionId){
		this.questionId = questionId;
	}
	
	public int getParticipantId(){
		return participantId;
	}
	
	public void setParticipantId(int participantId){
		this.participantId = participantId;
	}

	public int getChoice() {
		return choice;
	}

	public void setChoice(int choice) {
		this.choice = choice;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

}

