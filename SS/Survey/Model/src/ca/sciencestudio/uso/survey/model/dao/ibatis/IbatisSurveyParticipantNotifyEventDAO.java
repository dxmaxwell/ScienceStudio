package ca.sciencestudio.uso.survey.model.dao.ibatis;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.sciencestudio.uso.survey.model.SurveyParticipantNotifyEvent;
import ca.sciencestudio.uso.survey.model.dao.SurveyParticipantNotifyEventDAO;


public class IbatisSurveyParticipantNotifyEventDAO extends SqlMapClientDaoSupport implements SurveyParticipantNotifyEventDAO {

	public int addParticipantNotifyEvent(SurveyParticipantNotifyEvent surveParticipantNotifyHistory) {
		try {
			Integer id = (Integer) getSqlMapClientTemplate().insert("addSurveyParticipantNotifyEvent", surveParticipantNotifyHistory);
			logger.info("Add new Survey Participant Notify Event with id: " + id);
			return id;
		} catch (Exception e) {
			logger.error("Adding new Survey Participant Notify Event has encountered problems:\n" + e.getMessage());
			return 0;
		}
	}
    
	@SuppressWarnings("unchecked")
	public List<SurveyParticipantNotifyEvent> getNotifyEventListByParticipantId(int participantId) {
		try {
			List objList = getSqlMapClientTemplate().queryForList("getNotifyEventListByParticipantId", new Integer(participantId));
			logger.info("Get Survey Participant Notify Event List by Participant id: " + participantId + ", size: " + objList.size());
			return (List<SurveyParticipantNotifyEvent>) objList;
		} catch (Exception e) {
			logger.error("Getting Survey Participant Notify Event List by Participant id: " + participantId + " has encountered problems:\n" + e.getMessage());
			return null;
		}
    }
}
