package ca.sciencestudio.uso.survey.model.dao.ibatis;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.sciencestudio.uso.survey.model.SurveyParticipantNotification;
import ca.sciencestudio.uso.survey.model.dao.SurveyParticipantNotificationDAO;


public class IbatisSurveyParticipantNotificationDAO extends SqlMapClientDaoSupport implements SurveyParticipantNotificationDAO {

	@SuppressWarnings("unchecked")
	public List<SurveyParticipantNotification> getNotificationList() {
		try {
			List objList = getSqlMapClientTemplate().queryForList("getNotificationList");
			logger.info("Get Survey Participant Notification Event List size: " + objList.size());
			return (List<SurveyParticipantNotification>) objList;
		} catch (Exception e) {
			logger.error("Getting Survey Participant Notification List has encountered problems:\n" + e.getMessage());
			return null;
		}
    }
}
