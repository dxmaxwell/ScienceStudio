package ca.sciencestudio.uso.survey.model.dao;

import ca.sciencestudio.uso.survey.model.SurveyFeedback;

public interface SurveyFeedbackDAO
{
	public SurveyFeedback getFeedback(int questionId, int participantId);
	public int addFeedback(SurveyFeedback feedback);
	public void updateFeedback(SurveyFeedback feedback);
	public void removeFeedback(int feedbackId);
}
