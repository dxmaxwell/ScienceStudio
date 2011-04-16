package ca.sciencestudio.uso.survey.model.dao.ibatis;

import java.util.Date;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.sciencestudio.uso.survey.model.SurveyParticipant;
import ca.sciencestudio.uso.survey.model.dao.SurveyParticipantDAO;


public class IbatisSurveyParticipantDAO extends SqlMapClientDaoSupport implements SurveyParticipantDAO {

	public SurveyParticipant getParticipantById(int participantId) {
		try {
			Object obj = getSqlMapClientTemplate().queryForObject("getSurveyParticipantById", participantId);
			logger.info("Get Survey Participant with id: " + participantId);
			return (SurveyParticipant) obj;
		} catch (Exception e) {
			logger.error("Getting Survey Participant with id: " + participantId + " has encountered problems:\n" + e.getMessage());
			return null;
		}
	}

	public int addParticipant(SurveyParticipant participant) {
		try {
			Integer id = (Integer) getSqlMapClientTemplate().insert("addSurveyParticipant", participant);
			logger.info("Add new Survey Participant with id: " + id);
			return id;
		} catch (Exception e) {
			logger.error("Adding new Survey Participant has encountered problems:\n" + e.getMessage());
			return 0;
		}
	}
    
	public void removeParticipant(int participantId) {
		try {
			int rowsAffected = getSqlMapClientTemplate().delete("removeSurveyParticipant", participantId);
			logger.info("Remove Survey Participant, rows affected: " + rowsAffected);
		} catch (Exception e) {
			logger.error("Removing Survey Participant with id: " + participantId + " has encountered problems:\n" + e.getMessage());
		}
		return;
	}
	
	public void updateParticipant(SurveyParticipant participant) {
		try {
			int rowsAffected = getSqlMapClientTemplate().update("updateSurveyParticipant", participant);
			logger.info("Update Survey Participant, rows affected: " + rowsAffected);
		} catch (Exception e) {
			logger.error("Updating Survey Participant with id: " + participant.getId() + " has encountered problems:\n" + e.getMessage());
		}
		return;
	}
	
	@SuppressWarnings("unchecked")
	public List<SurveyParticipant> getParticipantList(int surveyId) {
		try {
			List objList = getSqlMapClientTemplate().queryForList("getSurveyParticipantList", new Integer(surveyId));
			logger.info("Get Survey Participant List, size: " + objList.size());
			return (List<SurveyParticipant>) objList;
		} catch (Exception e) {
			logger.error("Getting Survey Participant List with Survey id: " + surveyId + " has encountered problems:\n" + e.getMessage());
			return null;
		}
    }
	
	@SuppressWarnings("unchecked")
	public List<SurveyParticipant> getParticipantListByPersonId(int personId) {
		try {List objList = getSqlMapClientTemplate().queryForList("getSurveyParticipantListByPersonId", new Integer(personId));
		logger.info("Get Survey Participant List by Person: " + personId + ", size: " + objList.size());
		return (List<SurveyParticipant>) objList;
		} catch (Exception e) {
			logger.error("Getting Survey Participant List with Person id: " + personId + " has encountered problems:\n" + e.getMessage());
			return null;
		}
	}

	public Date getMaxRemindDate(int surveyId) {
		try {
			Date date = (Date) getSqlMapClientTemplate().queryForObject("getSurveyMaxRemindDate", new Integer(surveyId));
			logger.info("Get Survey Maximum Remind Date with survey id: " + surveyId);
			return date;
		} catch (Exception e) {
			logger.error("Getting Survey Maximum Remind Date with survey id: " + surveyId + " has encountered problems:\n" + e.getMessage());
			return null;
		}
	}
}
