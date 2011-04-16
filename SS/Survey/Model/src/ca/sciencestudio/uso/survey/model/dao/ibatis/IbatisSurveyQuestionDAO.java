package ca.sciencestudio.uso.survey.model.dao.ibatis;

import java.sql.SQLException;
import java.util.List;

import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ibatis.sqlmap.client.SqlMapExecutor;

import ca.sciencestudio.uso.survey.model.SurveyChoice;
import ca.sciencestudio.uso.survey.model.SurveyQuestion;
import ca.sciencestudio.uso.survey.model.dao.SurveyQuestionDAO;


public class IbatisSurveyQuestionDAO extends SqlMapClientDaoSupport implements SurveyQuestionDAO {

	public SurveyQuestion getQuestionById(int questionId) {
		try {
			Object obj = getSqlMapClientTemplate().queryForObject("getSurveyQuestionById", new Integer(questionId));
			logger.info("Get Survey Question with id: " + questionId);
			return (SurveyQuestion) obj;
		} catch (Exception e) {
			logger.error("Getting Survey Question with id: " + questionId + " has encountered problems:\n" + e.getMessage());
			return null;
		}
	}
	
	public int addQuestion(SurveyQuestion question) {
		try {
			Integer id = (Integer) getSqlMapClientTemplate().insert("addSurveyQuestion", question);
			logger.info("Add new Survey Question with id: " + id);
			return id;
		} catch (Exception e) {
			logger.error("Adding new Survey Question has encountered problems:\n" + e.getMessage());
			return 0;
		}
	}
    
	public void removeQuestion(int questionId) {
		try {
			int rowsAffected = getSqlMapClientTemplate().delete("removeSurveyQuestion", new Integer(questionId));
			logger.info("Remove Survey Question, rows affected: " + rowsAffected);
		} catch (Exception e) {
			logger.error("Removing Survey Question with id: " + questionId + " has encountered problems:\n" + e.getMessage());
		}
		return;
	}

	public int duplicateQuestion(final int oldQuestionId, final int sectionId) {
		try {
			Object obj = getSqlMapClientTemplate().execute(
					new SqlMapClientCallback() {
						@SuppressWarnings("unchecked")
						public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
							//executor.startBatch();
							SurveyQuestion surveyQuestion = (SurveyQuestion) executor.queryForObject("getSurveyQuestionById", oldQuestionId);
							surveyQuestion.setOrder(0);
							surveyQuestion.setSectionId(sectionId);
							Integer newQuestionId = (Integer) executor.insert("addSurveyQuestion", surveyQuestion);
							surveyQuestion.setId(newQuestionId);
							logger.info("Add new Survey Question with id: " + newQuestionId);
							List choiceList = executor.queryForList("getSurveyChoiceList", new Integer(oldQuestionId));
							logger.info("Get Survey Choice List, size: " + choiceList.size());;
							for (SurveyChoice choice: (List<SurveyChoice>) choiceList) {
								SurveyChoice newChoice = new SurveyChoice();
								newChoice.setQuestionId(newQuestionId);
								newChoice.setText(choice.getText());
								newChoice.setImage(choice.getImage());
								newChoice.setOrder(choice.getOrder());
								Integer choiceId = (Integer) executor.insert("addSurveyChoice", newChoice);
								logger.info("Add new Survey Choice with id: " + choiceId);
							}
							//executor.executeBatch();
							return surveyQuestion;
						}
					}
			);
			return ((SurveyQuestion) obj).getId();
		} catch (Exception e) {
			logger.error("Duplicating Survey Question from id: " + oldQuestionId + " has encountered problems:\n" + e.getMessage());
			return 0;
		}
	}
	
	public void updateQuestion(SurveyQuestion question) {
		try {
			int rowsAffected = getSqlMapClientTemplate().update("updateSurveyQuestion", question);
			logger.info("Update Survey Question, rows affected: " + rowsAffected);
		} catch (Exception e) {
			logger.error("Updating Survey Question with id: " + question.getId() + " has encountered problems:\n" + e.getMessage());
		}
		return;
	}
	
	@SuppressWarnings("unchecked")
	public List<SurveyQuestion> getQuestionListBySectionId(int sectionId) {
		try {
			List objList = getSqlMapClientTemplate().queryForList("getSurveyQuestionListBySectionId", new Integer(sectionId));
			logger.info("Get Survey Question List, size: " + objList.size());
			return (List<SurveyQuestion>) objList;
		} catch (Exception e) {
			logger.error("Getting Survey Question List with Section id: " + sectionId + " has encountered problems:\n" + e.getMessage());
			return null;
		}
    }

	public int getMaxQuestionOrder(int sectionId) {
		try {
			Integer order = (Integer) getSqlMapClientTemplate().queryForObject("getSurveyMaxQuestionOrder", new Integer(sectionId));
			if (order == null) order = 0;
			order++;
			logger.info("Get Survey Maximum Question Order: " + order + " with section id: " + sectionId);
			return order;
		} catch (Exception e) {
			logger.error("Getting Survey Maximum Question Order with section id: " + sectionId + " has encountered problems:\n" + e.getMessage());
			return 0;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<SurveyQuestion> getAllQuestionList() {
		try {
			List objList = getSqlMapClientTemplate().queryForList("getAllSurveyQuestionList");
			logger.info("Get all Survey Question List, size: " + objList.size());
			return (List<SurveyQuestion>) objList;
		} catch (Exception e) {
			logger.error("Getting all Survey Question List has encountered problems:\n" + e.getMessage());
			return null;
		}
    }
}
