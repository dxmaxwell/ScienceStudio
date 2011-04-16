package ca.sciencestudio.uso.survey.model.dao;

import java.util.List;

import ca.sciencestudio.uso.survey.model.SurveyCategory;


public interface SurveyCategoryDAO 
{
	public SurveyCategory getSurveyCategoryById(int surveyCategoryId);
	public int addSurveyCategory(SurveyCategory surveyCategory);
	public void removeSurveyCategory(int surveyCategoryId);
	public void updateSurveyCategory(SurveyCategory surveyCategory);
	public List<SurveyCategory> getSurveyCategoryList();
}
