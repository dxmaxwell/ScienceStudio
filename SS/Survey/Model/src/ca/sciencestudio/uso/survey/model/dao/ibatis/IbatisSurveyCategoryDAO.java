package ca.sciencestudio.uso.survey.model.dao.ibatis;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;


import ca.sciencestudio.uso.survey.model.dao.SurveyCategoryDAO;
import ca.sciencestudio.uso.survey.model.SurveyCategory;

public class IbatisSurveyCategoryDAO extends SqlMapClientDaoSupport implements SurveyCategoryDAO {

	public SurveyCategory getSurveyCategoryById(int surveyCategoryId) {
		try {
			Object obj = getSqlMapClientTemplate().queryForObject("getSurveyCategoryById", new Integer(surveyCategoryId));
			logger.info("Get Survey Category with id: " + surveyCategoryId);
			return (SurveyCategory) obj;
		} catch (Exception e) {
			logger.error("Getting Survey Category with id: " + surveyCategoryId + " has encountered problems:\n" + e.getMessage());
			return null;
		}
	}
	
	public int addSurveyCategory(SurveyCategory surveyCategory) {
		try {
			Integer id = (Integer) getSqlMapClientTemplate().insert("addSurveyCategory", surveyCategory);
			logger.info("Add new Survey Category with id: " + id);
			return id;
		} catch (Exception e) {
			logger.error("Adding new Survey Category has encountered problems:\n" + e.getMessage());
			return 0;
		}
	}
    
	public void removeSurveyCategory(int surveyCategoryId) {
		try {
			int rowsAffected = getSqlMapClientTemplate().delete("removeSurveyCategory", new Integer(surveyCategoryId));
			logger.info("Remove Survey Category, rows affected: " + rowsAffected);
		} catch (Exception e) {
			logger.error("Removing Survey Category with id: " + surveyCategoryId + " has encountered problems:\n" + e.getMessage());
		}
		return;
	}
	
	public void updateSurveyCategory(SurveyCategory surveyCategory) {
		try {
			int rowsAffected = getSqlMapClientTemplate().update("updateSurveyCategory", surveyCategory);
			logger.info("Update Survey Category, rows affected: " + rowsAffected);
		} catch (Exception e) {
			logger.error("Updating Survey Category with id: " + surveyCategory.getId() + " has encountered problems:\n" + e.getMessage());
		}
		return;
	}
	
	@SuppressWarnings("unchecked")
	public List<SurveyCategory> getSurveyCategoryList() {
		try {
			List objList = getSqlMapClientTemplate().queryForList("getSurveyCategoryList");
			logger.info("Get Survey Category List, size: " + objList.size());
			return (List<SurveyCategory>) objList;
		} catch (Exception e) {
			logger.error("Getting Survey Category List has encountered problems:\n" + e.getMessage());
			return null;
		}
    }
}
