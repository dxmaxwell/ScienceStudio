package ca.sciencestudio.uso.survey.model.dao;

import java.util.List;

import ca.sciencestudio.uso.survey.model.SurveyQuestion;



public interface SurveyQuestionDAO
{
	public SurveyQuestion getQuestionById(int questionId);
	public int addQuestion(SurveyQuestion question);
	public void removeQuestion(int questionId);
	public int duplicateQuestion(int questionId, int sectionId);
	public void updateQuestion(SurveyQuestion question);
	public List<SurveyQuestion> getQuestionListBySectionId(int sectionId);
	public int getMaxQuestionOrder(int sectionId);
	public List<SurveyQuestion> getAllQuestionList();
}
