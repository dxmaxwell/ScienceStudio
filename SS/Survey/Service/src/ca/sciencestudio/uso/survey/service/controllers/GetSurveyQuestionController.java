package ca.sciencestudio.uso.survey.service.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ca.sciencestudio.uso.survey.model.SurveyQuestion;
import ca.sciencestudio.uso.survey.model.dao.SurveyQuestionDAO;


public class GetSurveyQuestionController implements Controller {

	private SurveyQuestionDAO surveyQuestionDAO;
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String surveySectionIdStr = request.getParameter("surveySectionId");
		String surveyQuestionIdStr = request.getParameter("surveyQuestionId");
		String activeTab = request.getParameter("activeTab");
		
		int surveySectionId = 0;
		int surveyQuestionId = 0;
		int surveyQuestionType = 0;
		
		Map<Object, Object> model = new HashMap<Object, Object>();
		
		try {
			if (surveySectionIdStr != null) {
				surveySectionId = Integer.parseInt(surveySectionIdStr);
			}
		} catch (NumberFormatException e) {
			surveySectionId = 0;
		}

		try {
			if (surveyQuestionIdStr != null) {
				surveyQuestionId = Integer.parseInt(surveyQuestionIdStr);
				if(surveyQuestionId != 0) {
					SurveyQuestion surveyQuestion = surveyQuestionDAO.getQuestionById(surveyQuestionId);
					surveyQuestionType = surveyQuestion.getType();
				}
			}
		} catch (NumberFormatException e) {
			surveyQuestionId = 0;
		}
		
		model.put("surveySectionId", surveySectionId);
		model.put("surveyQuestionId", surveyQuestionId);
		model.put("surveyQuestionType", surveyQuestionType);
		
		if (activeTab == null) {
			model.put("activeTab", "details");
		} else {
			model.put("activeTab", activeTab);
		}
		
		return new ModelAndView("surveyQuestion", model);
	}

	public void setSurveyQuestionDAO(SurveyQuestionDAO surveyQuestionDAO) {
		this.surveyQuestionDAO = surveyQuestionDAO;
	}

}
