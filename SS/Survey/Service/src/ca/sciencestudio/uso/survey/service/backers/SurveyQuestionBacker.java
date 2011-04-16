package ca.sciencestudio.uso.survey.service.backers;

import java.util.ArrayList;
import java.util.List;

import ca.sciencestudio.uso.survey.service.backers.SurveyChoiceBacker;


public class SurveyQuestionBacker {

	private int id;
	private String text;
	private int type;
	private int answerLength;
	private String image;
	private List<SurveyChoiceBacker> choiceBackers = new ArrayList<SurveyChoiceBacker>();
	private int choice;
	private String answer;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getAnswerLength() {
		return answerLength;
	}

	public void setAnswerLength(int answerLength) {
		this.answerLength = answerLength;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setChoiceBackers(List<SurveyChoiceBacker> choiceBackers) {
	    this.choiceBackers = choiceBackers;
	}

	public List<SurveyChoiceBacker> getChoiceBackers() {
	    return choiceBackers;
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

