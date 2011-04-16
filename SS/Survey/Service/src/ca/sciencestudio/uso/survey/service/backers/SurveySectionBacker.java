package ca.sciencestudio.uso.survey.service.backers;

import java.util.ArrayList;
import java.util.List;


public class SurveySectionBacker {

	private int id;
	private String name;
	private String description;
	private List<SurveyQuestionBacker> questionBackers = new ArrayList<SurveyQuestionBacker>();
	private int questionCount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setQuestionBackers(List<SurveyQuestionBacker> questionBackers) {
	    this.questionBackers = questionBackers;
	}

	public List<SurveyQuestionBacker> getQuestionBackers() {
	    return questionBackers;
	}

	public int getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(int questionCount) {
		this.questionCount = questionCount;
	}

}

