package ca.sciencestudio.uso.survey.model.dao;

import java.util.List;

import ca.sciencestudio.uso.survey.model.SurveySection;



public interface SurveySectionDAO 
{
	public SurveySection getSectionById(int sectionId);
	public int addSection(SurveySection section);
	public void removeSection(int sectionId);
	public void updateSection(SurveySection section);
	public List<SurveySection> getSectionList(int surveyId);
	public int getMaxSectionOrder(int surveyId);
}
