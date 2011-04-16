package ca.sciencestudio.uso.survey.model.dao;

import java.util.List;

import ca.sciencestudio.uso.survey.model.Survey;



public interface SurveyDAO 
{
	public Survey getSurveyById(int surveyId);
	public int addSurvey(Survey survey);
	public int duplicateSurvey(int surveyId);
	public void removeSurvey(int surveyId);
	public void updateSurvey(Survey survey);
	public List<Survey> getSurveyListByCategory(int surveyCategoryId);
}
