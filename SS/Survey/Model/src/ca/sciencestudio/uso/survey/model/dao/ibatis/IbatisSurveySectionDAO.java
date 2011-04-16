package ca.sciencestudio.uso.survey.model.dao.ibatis;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.sciencestudio.uso.survey.model.SurveySection;
import ca.sciencestudio.uso.survey.model.dao.SurveySectionDAO;


public class IbatisSurveySectionDAO extends SqlMapClientDaoSupport implements SurveySectionDAO {

	public SurveySection getSectionById(int sectionId) {
		try {
			Object obj = getSqlMapClientTemplate().queryForObject("getSurveySectionById", new Integer(sectionId));
			logger.info("Get Survey Section with id: " + sectionId);
			return (SurveySection) obj;
		} catch (Exception e) {
			logger.error("Getting Survey Section with id: " + sectionId + " has encountered problems:\n" + e.getMessage());
			return null;
		}
	}
	
	public int addSection(SurveySection section) {
		try {
			Integer id = (Integer) getSqlMapClientTemplate().insert("addSurveySection", section);
			logger.info("Add new Survey Section with id: " + id);
			return id;
		} catch (Exception e) {
			logger.error("Adding new Survey Section has encountered problems:\n" + e.getMessage());
			return 0;
		}
	}
    
	public void removeSection(int sectionId) {
		try {
			int rowsAffected = getSqlMapClientTemplate().delete("removeSurveySection", new Integer(sectionId));
			logger.info("Remove Survey Section, rows affected: " + rowsAffected);
		} catch (Exception e) {
			logger.error("Removing Survey Section with id: " + sectionId + " has encountered problems:\n" + e.getMessage());
		}
		return;
	}
	
	public void updateSection(SurveySection section) {
		try {
			int rowsAffected = getSqlMapClientTemplate().update("updateSurveySection", section);
			logger.info("Update Survey Section, rows affected: " + rowsAffected);
		} catch (Exception e) {
			logger.error("Updating Survey Section with id: " + section.getId() + " has encountered problems:\n" + e.getMessage());
		}
		return;
	}
	
	@SuppressWarnings("unchecked")
	public List<SurveySection> getSectionList(int surveyId) {
		try {
			List objList = getSqlMapClientTemplate().queryForList("getSurveySectionList", new Integer(surveyId));
			logger.info("Get Survey Section List, size: " + objList.size());
			return (List<SurveySection>) objList;
		} catch (Exception e) {
			logger.error("Getting Survey Section List with Survey id: " + surveyId + " has encountered problems:\n" + e.getMessage());
			return null;
		}
    }

	public int getMaxSectionOrder(int surveyId) {
		try {
			Integer order = (Integer) getSqlMapClientTemplate().queryForObject("getSurveyMaxSectionOrder", new Integer(surveyId));
			if (order == null) order = 0;
			order++;
			logger.info("Get Survey Maximum Section Order: " + order + " with survey id: " + surveyId);
			return order;
		} catch (Exception e) {
			logger.error("Getting Survey Maximum Section Order with survey id: " + surveyId + " has encountered problems:\n" + e.getMessage());
			return 0;
		}
	}
}
