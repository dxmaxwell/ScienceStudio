package ca.sciencestudio.uso.survey.model.dao.ibatis;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.sciencestudio.uso.survey.model.SurveyStatistic;
import ca.sciencestudio.uso.survey.model.dao.SurveyStatisticDAO;



public class IbatisSurveyStatisticDAO extends SqlMapClientDaoSupport implements SurveyStatisticDAO {

	public SurveyStatistic getExpectedParticipantCount(int surveyId) {
		try {
			Object obj = getSqlMapClientTemplate().queryForObject("getExpectedParticipantCount", new Integer(surveyId));
			logger.info("Get Expected Participant Count with Survey Id: " + surveyId);
			return (SurveyStatistic) obj;
		} catch (Exception e) {
			logger.error("Getting Expected Participant Count with Survey id: " + surveyId + " has encountered problems:\n" + e.getMessage());
			return null;
		}
	}

	public SurveyStatistic getAttendedParticipantCount(int surveyId) {
		try {
			Object obj = getSqlMapClientTemplate().queryForObject("getAttendedParticipantCount", new Integer(surveyId));
			logger.info("Get Attended Participant Count with Survey Id: " + surveyId);
			return (SurveyStatistic) obj;
		} catch (Exception e) {
			logger.error("Getting Attended Participant Count with Survey id: " + surveyId + " has encountered problems:\n" + e.getMessage());
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<SurveyStatistic> getSectionList(int surveyId) {
		try {
			List objList = getSqlMapClientTemplate().queryForList("getStatisticSectionList", new Integer(surveyId));
			logger.info("Get Statisitc Section List, size: " + objList.size());
			return (List<SurveyStatistic>) objList;
		} catch (Exception e) {
			logger.error("Getting Statisitic Section List with Survey id: " + surveyId + " has encountered problems:\n" + e.getMessage());
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<SurveyStatistic> getQuestionList(int sectionId) {
		try {
			List objList = getSqlMapClientTemplate().queryForList("getStatisticQuestionList", new Integer(sectionId));
			logger.info("Get Statisitc Question List, size: " + objList.size());
			return (List<SurveyStatistic>) objList;
		} catch (Exception e) {
			logger.error("Getting Statisitic Question List with Section id: " + sectionId + " has encountered problems:\n" + e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<SurveyStatistic> getChoiceList(int questionId) {
		try {
			List objList = getSqlMapClientTemplate().queryForList("getStatisticChoiceList", new Integer(questionId));
			logger.info("Get Statisitc Question Choice List, size: " + objList.size());
			return (List<SurveyStatistic>) objList;
		} catch (Exception e) {
			logger.error("Getting Statisitic Question Choice List with Question id: " + questionId + " has encountered problems:\n" + e.getMessage());
			return null;
		}
	}

	public SurveyStatistic getOtherAnswerCount(int questionId) {
		try {
			Object obj = getSqlMapClientTemplate().queryForObject("getStatisticOtherAnswerCount", new Integer(questionId));
			logger.info("Get Statisitc Question Other Answer Count with Question id: " + questionId);
			return (SurveyStatistic) obj;
		} catch (Exception e) {
			logger.error("Getting Statisitic Question List with Question id: " + questionId + " has encountered problems:\n" + e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<SurveyStatistic> getAllOtherAnswerList(int questionId) {
		try {
			List objList = getSqlMapClientTemplate().queryForList("getStatisticAllOtherAnswerList", new Integer(questionId));
			logger.info("Get Statisitc Question List, size: " + objList.size());
			return (List<SurveyStatistic>) objList;
		} catch (Exception e) {
			logger.error("Getting Statisitic Question List with Question id: " + questionId + " has encountered problems:\n" + e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<SurveyStatistic> getFirstSixAnswerList(int questionId) {
		try {
			List objList = getSqlMapClientTemplate().queryForList("getStatisticAllAnswerList", new Integer(questionId), 0, 6);
			logger.info("Get Statisitc Question First Six Answer List, size: " + objList.size());
			return (List<SurveyStatistic>) objList;
		} catch (Exception e) {
			logger.error("Getting Statisitic Question First Six Answer List with Question id: " + questionId + " has encountered problems:\n" + e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<SurveyStatistic> getAllAnswerList(int questionId) {
		try {
			List objList = getSqlMapClientTemplate().queryForList("getStatisticAllAnswerList", new Integer(questionId));
			logger.info("Get Statisitc Question All Answer List, size: " + objList.size());
			return (List<SurveyStatistic>) objList;
		} catch (Exception e) {
			logger.error("Getting Statisitic Question All Answer List with Question id: " + questionId + " has encountered problems:\n" + e.getMessage());
			return null;
		}
	}
}
