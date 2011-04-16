package ca.sciencestudio.uso.survey.service.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import ca.sciencestudio.uso.survey.model.SurveyQuestion;
import ca.sciencestudio.uso.survey.model.dao.SurveyQuestionDAO;
import ca.sciencestudio.uso.survey.model.SurveySection;
import ca.sciencestudio.uso.survey.model.dao.SurveySectionDAO;
import ca.sciencestudio.uso.survey.model.SurveyQuestionType;


public class SurveyQuestionFormController extends SimpleFormController {
	
	private SurveyQuestionDAO surveyQuestionDAO;
	private SurveySectionDAO surveySectionDAO;
	private int surveySectionId = 0;
	
	protected ModelAndView onSubmit(Object command) throws Exception {
		SurveyQuestion surveyQuestion = (SurveyQuestion) command;
		
		int surveyQuestionId = surveyQuestion.getId();
		if (surveyQuestion.getType() == 0) surveyQuestion.setAnswerLength(0);
		if (surveyQuestionId != 0) {
			surveyQuestionDAO.updateQuestion(surveyQuestion);
		} else {
			surveyQuestionId = surveyQuestionDAO.addQuestion(surveyQuestion);
		}
		
		return new ModelAndView("redirect:surveyQuestion?surveyQuestionId=" + surveyQuestionId + "&surveySectionId=" + surveySectionId + "&activeTab=details");
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		String surveySectionIdParam = request.getParameter("surveySectionId");
		String surveyQuestionIdParam = request.getParameter("surveyQuestionId");
		
		try {
			if (surveySectionIdParam != null) {
				surveySectionId = Integer.parseInt(surveySectionIdParam);
			}
		} catch (Exception e) {
			surveySectionId = 0;
		}

		int surveyQuestionId = 0;
		try {
			if (surveyQuestionIdParam != null) {
				surveyQuestionId = Integer.parseInt(surveyQuestionIdParam);
			}
		} catch (Exception e) {
			surveyQuestionId = 0;
		}
		
		SurveyQuestion surveyQuestion = null;
		
		if(surveyQuestionId != 0) {
			surveyQuestion = surveyQuestionDAO.getQuestionById(surveyQuestionId);
		} else {
			surveyQuestion = new SurveyQuestion();
			if (surveySectionId != 0) {
				surveyQuestion.setSectionId(surveySectionId);
			}
			surveyQuestion.setOrder(surveyQuestionDAO.getMaxQuestionOrder(surveySectionId));
		}
		
		return surveyQuestion;
	}
	
	protected Map<Object,Object> referenceData(HttpServletRequest request) throws Exception {
		
		String surveyQuestionIdParam = request.getParameter("surveyQuestionId");
		int surveyQuestionId = 0;
		
		Map<Object, Object> model = new HashMap<Object, Object>();

		try {
			if(surveyQuestionIdParam != null) {
				surveyQuestionId = Integer.parseInt(surveyQuestionIdParam);
			}
		} catch (Exception e) {
			surveyQuestionId = 0;
		}
		
		if (surveyQuestionId !=0) {
			model.put("titleMsg", "Editing survey question");
		} else {
			model.put("titleMsg", "New survey question");
		}
		
		SurveySection surveySection = surveySectionDAO.getSectionById(surveySectionId);
		int surveyId = surveySection.getSurveyId();
		
		List<SurveySection> surveySections = surveySectionDAO.getSectionList(surveyId);
		model.put("surveySections", surveySections);
		
		List<SurveyQuestionType> questionTypes = Arrays.asList(SurveyQuestionType.values());
		model.put("questionTypes", questionTypes);
		
		return model;
	}

	public void setSurveySectionDAO(SurveySectionDAO surveySectionDAO) {
		this.surveySectionDAO = surveySectionDAO;
	}

	public void setSurveyQuestionDAO(SurveyQuestionDAO surveyQuestionDAO) {
		this.surveyQuestionDAO = surveyQuestionDAO;
	}
	
}
