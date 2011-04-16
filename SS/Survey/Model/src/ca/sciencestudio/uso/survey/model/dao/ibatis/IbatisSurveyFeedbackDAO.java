package ca.sciencestudio.uso.survey.model.dao.ibatis;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.sciencestudio.uso.survey.model.SurveyFeedback;
import ca.sciencestudio.uso.survey.model.dao.SurveyFeedbackDAO;



public class IbatisSurveyFeedbackDAO extends SqlMapClientDaoSupport implements SurveyFeedbackDAO {

	public SurveyFeedback getFeedback(int questionId, int participantId) {
		try {
			SurveyFeedback feedback = new SurveyFeedback();
			feedback.setQuestionId(questionId);
			feedback.setParticipantId(participantId);
			Object obj = getSqlMapClientTemplate().queryForObject("getSurveyFeedback", feedback);
			logger.info("Get Survey Feedback with QuestionId: " + questionId + " and ParticipantId: " + participantId);
			return (SurveyFeedback) obj;
		} catch (Exception e) {
			logger.error("Getting Survey Feedback with QuestionId: " + questionId + " and ParticipantId: " + participantId
					+ " has encountered problems:\n" + e.getMessage());
			return null;
		}
	}
	
	public int addFeedback(SurveyFeedback feedback) {
		try {
			Integer id = (Integer) getSqlMapClientTemplate().insert("addSurveyFeedback", feedback);
			logger.info("Add new Survey Feedback with id: " + id);
			return id;
		} catch (Exception e) {
			logger.error("Adding new Survey Feedback has encountered problems:\n" + e.getMessage());
			return 0;
		}
	}
    
	public void updateFeedback(SurveyFeedback feedback) {
		try {
			int rowsAffected = getSqlMapClientTemplate().update("updateSurveyFeedback", feedback);
			logger.info("Update Survey Feedback, rows affected: " + rowsAffected);
		} catch (Exception e) {
			logger.error("Updating Survey Feedback with id: " + feedback.getId() + " has encountered problems:\n" + e.getMessage());
		}
		return;
	}

	public void removeFeedback(int feedbackId) {
		try {
			int rowsAffected = getSqlMapClientTemplate().delete("removeSurveyFeedback", new Integer(feedbackId));
			logger.info("Remove Survey Feedback, rows affected: " + rowsAffected);
		} catch (Exception e) {
			logger.error("Removing Survey Feedback with id: " + feedbackId + " has encountered problems:\n" + e.getMessage());
		}
		return;
	}
	
}
