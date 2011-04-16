package ca.sciencestudio.uso.survey.model.dao;

import java.util.List;

import ca.sciencestudio.uso.survey.model.SurveyStatistic;



public interface SurveyStatisticDAO
{
	public SurveyStatistic getExpectedParticipantCount(int surveyId);
	public SurveyStatistic getAttendedParticipantCount(int surveyId);
	public List<SurveyStatistic> getSectionList(int surveyId);
	public List<SurveyStatistic> getQuestionList(int sectionId);
	public List<SurveyStatistic> getChoiceList(int questionId);
	public SurveyStatistic getOtherAnswerCount(int questionId);
	public List<SurveyStatistic> getAllOtherAnswerList(int questionId);
	public List<SurveyStatistic> getFirstSixAnswerList(int questionId);
	public List<SurveyStatistic> getAllAnswerList(int questionId);
}
