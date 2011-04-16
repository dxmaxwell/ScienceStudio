package ca.sciencestudio.uso.survey.service.backers;

import java.util.ArrayList;
import java.util.List;

import ca.sciencestudio.uso.survey.model.SurveyStatistic;


public class SurveyStatisticDetailBacker {

	private int type;
	private int numberer;
	private int sectionId;
	private int questionId;
	private int choiceId;
	private String text;
	private String description;
	private int count;
	private double percentage;
	private List<SurveyStatistic> statisticDetailList = new ArrayList<SurveyStatistic>();
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public int getNumberer() {
		return numberer;
	}
	
	public void setNumberer(int numberer) {
		this.numberer = numberer;
	}
	
	public int getSectionId() {
		return sectionId;
	}
	
	public void setSectionId(int sectionId) {
		this.sectionId = sectionId;
	}
	
	public int getQuestionId() {
		return questionId;
	}
	
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	
	public int getChoiceId() {
		return choiceId;
	}
	
	public void setChoiceId(int choiceId) {
		this.choiceId = choiceId;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public double getPercentage() {
		return percentage;
	}
	
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
	
	
	public void setStatisticDetailList(List<SurveyStatistic> statisticDetailList) {
	    this.statisticDetailList = statisticDetailList;
	}

	public List<SurveyStatistic> getStatisticDetailList() {
	    return statisticDetailList;
	}	
}
