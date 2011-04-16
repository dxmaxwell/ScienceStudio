package ca.sciencestudio.uso.survey.model.dao.ibatis;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.sciencestudio.uso.survey.model.SurveyChoice;
import ca.sciencestudio.uso.survey.model.dao.SurveyChoiceDAO;



public class IbatisSurveyChoiceDAO extends SqlMapClientDaoSupport implements SurveyChoiceDAO {

	public SurveyChoice getChoiceById(int choiceId) {
		try {
			Object obj = getSqlMapClientTemplate().queryForObject("getSurveyChoiceById", new Integer(choiceId));
			logger.info("Get Survey Choice with id: " + choiceId);
			return (SurveyChoice) obj;
		} catch (Exception e) {
			logger.error("Getting Choice with id: " + choiceId + " has encountered problems:\n" + e.getMessage());
			return null;
		}
	}
	
	public int addChoice(SurveyChoice choice) {
		try {
			Integer id = (Integer) getSqlMapClientTemplate().insert("addSurveyChoice", choice);
			logger.info("Add new Survey Choice with id: " + id);
			return id;
		} catch (Exception e) {
			logger.error("Adding new Survey Choice has encountered problems:\n" + e.getMessage());
			return 0;
		}
	}
    
	public void removeChoice(int choiceId) {
		try {
			int rowsAffected = getSqlMapClientTemplate().delete("removeSurveyChoice", new Integer(choiceId));
			logger.info("Remove Survey Choice, rows affected: " + rowsAffected);
		} catch (Exception e) {
			logger.error("Removing Survey Choice with id: " + choiceId + " has encountered problems:\n" + e.getMessage());
		}
		return;
	}
	
	public void updateChoice(SurveyChoice choice) {
		try {
			int rowsAffected = getSqlMapClientTemplate().update("updateSurveyChoice", choice);
			logger.info("Update Survey Choice, rows affected: " + rowsAffected);
		} catch (Exception e) {
			logger.error("Updating Survey Choice with id: " + choice.getId() + " has encountered problems:\n" + e.getMessage());
		}
		return;
	}
	
	@SuppressWarnings("unchecked")
	public List<SurveyChoice> getChoiceList(int questionId) {
		try {
			List objList = getSqlMapClientTemplate().queryForList("getSurveyChoiceList", new Integer(questionId));
			logger.info("Get Survey Choice List, size: " + objList.size());
			return (List<SurveyChoice>) objList;
		} catch (Exception e) {
			logger.error("Getting Survey Choice List with Question id: " + questionId + " has encountered problems:\n" + e.getMessage());
			return null;
		}
    }

	public int getMaxChoiceOrder(int questionId) {
		try {
			Integer order = (Integer) getSqlMapClientTemplate().queryForObject("getSurveyMaxChoiceOrder", new Integer(questionId));
			if (order == null) order = 0;
			order++;
			logger.info("Get Survey Maximum Choice Order: " + order + " with question id: " + questionId);
			return order;
		} catch (Exception e) {
			logger.error("Getting Survey Maximum Choice Order with question id: " + questionId + " has encountered problems:\n" + e.getMessage());
			return 0;
		}
	}
}
