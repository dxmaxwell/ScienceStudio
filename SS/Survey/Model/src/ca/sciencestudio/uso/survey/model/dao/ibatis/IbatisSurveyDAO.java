package ca.sciencestudio.uso.survey.model.dao.ibatis;

import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.sciencestudio.uso.survey.model.SurveyChoice;
import ca.sciencestudio.uso.survey.model.SurveyQuestion;
import ca.sciencestudio.uso.survey.model.SurveySection;
import ca.sciencestudio.uso.survey.model.Survey;
import ca.sciencestudio.uso.survey.model.dao.SurveyDAO;

import com.ibatis.sqlmap.client.SqlMapExecutor;


public class IbatisSurveyDAO extends SqlMapClientDaoSupport implements SurveyDAO {

	public Survey getSurveyById(int surveyId) {
		try {
			Object obj = getSqlMapClientTemplate().queryForObject("getSurveyById", new Integer(surveyId));
			logger.info("Get Survey with id: " + surveyId);
			return (Survey) obj;
		} catch (Exception e) {
			logger.error("Getting Survey with id: " + surveyId + " has encountered problems:\n" + e.getMessage());
			return null;
		}
	}
	
	public int addSurvey(Survey survey) {
		try {
			Integer id = (Integer) getSqlMapClientTemplate().insert("addSurvey", survey);
			logger.info("Add new Survey with id: " + id);
			return id;
		} catch (Exception e) {
			logger.error("Adding new Survey has encountered problems:\n" + e.getMessage());
			return 0;
		}
	}
    
	@SuppressWarnings("unchecked")
	public int duplicateSurvey(final int oldSurveyId) throws DataAccessException {
		try {
			Object obj = getSqlMapClientTemplate().execute(
					new SqlMapClientCallback() {
						public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
							//executor.startBatch();
							Survey survey = (Survey) executor.queryForObject("getSurveyById", oldSurveyId);
							survey.setName("Copy of " + survey.getName());
							survey.setStatus(0);
							Integer newSurveyId = (Integer) executor.insert("addSurvey", survey);
							survey.setId(newSurveyId);
							logger.info("Add new Survey with id: " + newSurveyId);
							List sectionList = executor.queryForList("getSurveySectionList", new Integer(oldSurveyId));
							logger.info("Get Survey Section List, size: " + sectionList.size());;
							for (SurveySection section: (List<SurveySection>) sectionList) {
								int oldSectionId = section.getId();
								SurveySection newSection = new SurveySection();
								newSection.setSurveyId(newSurveyId);
								newSection.setName(section.getName());
								newSection.setOrder(section.getOrder());
								newSection.setDescription(section.getDescription());
								Integer newSectionId = (Integer) executor.insert("addSurveySection", newSection);
								logger.info("Add new Survey Section with id: " + newSectionId);
								List questionList = executor.queryForList("getSurveyQuestionListBySectionId", new Integer(oldSectionId));
								logger.info("Get Survey Question List, size: " + questionList.size());;
								for (SurveyQuestion question: (List<SurveyQuestion>) questionList) {
									int oldQuestionId = question.getId();
									SurveyQuestion newQuestion = new SurveyQuestion();
									newQuestion.setSectionId(newSectionId);
									newQuestion.setOrder(question.getOrder());
									newQuestion.setText(question.getText());
									newQuestion.setType(question.getType());
									newQuestion.setAnswerLength(question.getAnswerLength());
									newQuestion.setImage(question.getImage());
									Integer newQuestionId = (Integer) executor.insert("addSurveyQuestion", newQuestion);
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
								}
							}
							//executor.executeBatch();
							return survey;
						}
					}
			);
			return ((Survey) obj).getId();
		} catch (Exception e) {
			logger.error("Duplicating Survey from id: " + oldSurveyId + " has encountered problems:\n" + e.getMessage());
			return 0;
		}
	}

	public void removeSurvey(int surveyId)
	{
		try {
			int rowsAffected = getSqlMapClientTemplate().delete("removeSurvey", new Integer(surveyId));
			logger.info("Remove Survey, rows affected: " + rowsAffected);
		} catch (Exception e) {
			logger.error("Removing Survey with id: " + surveyId + " has encountered problems:\n" + e.getMessage());
		}
		return;
	}
	
	public void updateSurvey(Survey survey) {
		try {
			int rowsAffected = getSqlMapClientTemplate().update("updateSurvey", survey);
			logger.info("Update Survey, rows affected: " + rowsAffected);
		} catch (Exception e) {
			logger.error("Updating Survey with id: " + survey.getId() + " has encountered problems:\n" + e.getMessage());
		}
		return;
	}
	
	@SuppressWarnings("unchecked")
	public List<Survey> getSurveyListByCategory(int surveyCategoryId) {
		try {
			List objList = getSqlMapClientTemplate().queryForList("getSurveyListByCategory", new Integer(surveyCategoryId));
			logger.info("Get Survey List, size: " + objList.size());
			return (List<Survey>) objList;
		} catch (Exception e) {
			logger.error("Getting Survey List with Category id: " + surveyCategoryId + " has encountered problems:\n" + e.getMessage());
			return null;
		}
    }
}
