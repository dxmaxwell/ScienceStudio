package ca.sciencestudio.uso.survey.model.dao;

import java.util.Date;
import java.util.List;

import ca.sciencestudio.uso.survey.model.SurveyParticipant;



public interface SurveyParticipantDAO 
{
	public SurveyParticipant getParticipantById(int participantId);
	public int addParticipant(SurveyParticipant participant);
	public void removeParticipant(int participantId);
	public void updateParticipant(SurveyParticipant participant);
	public List<SurveyParticipant> getParticipantList(int surveyId);
	public List<SurveyParticipant> getParticipantListByPersonId(int personId);
	public Date getMaxRemindDate(int surveyId);
}
