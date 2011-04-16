package ca.sciencestudio.uso.survey.model.dao;

import java.util.List;

import ca.sciencestudio.uso.survey.model.SurveyChoice;



public interface SurveyChoiceDAO 
{
	public SurveyChoice getChoiceById(int choiceId);
	public int addChoice(SurveyChoice choice);
	public void removeChoice(int choiceId);
	public void updateChoice(SurveyChoice choice);
	public List<SurveyChoice> getChoiceList(int choiceId);
	public int getMaxChoiceOrder(int questionId);
}
