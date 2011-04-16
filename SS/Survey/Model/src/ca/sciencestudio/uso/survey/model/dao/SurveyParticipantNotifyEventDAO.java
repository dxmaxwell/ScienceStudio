package ca.sciencestudio.uso.survey.model.dao;

import java.util.List;

import ca.sciencestudio.uso.survey.model.SurveyParticipantNotifyEvent;


public interface SurveyParticipantNotifyEventDAO 
{
	public int addParticipantNotifyEvent(SurveyParticipantNotifyEvent surveyParticipantNotifyEvent);
	public List<SurveyParticipantNotifyEvent> getNotifyEventListByParticipantId(int participantId);
}
