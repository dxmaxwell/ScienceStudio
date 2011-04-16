package ca.sciencestudio.uso.survey.service.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ca.sciencestudio.uso.survey.model.dao.SurveyQuestionDAO;


public class DuplicateSurveyQuestionController implements Controller {
	
	private SurveyQuestionDAO surveyQuestionDAO;

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String surveyQuestionIdStr = request.getParameter("questionId");
		String surveySectionIdStr = request.getParameter("sectionId");
		
		int surveyQuestionId = 0;
		int surveySectionId = 0;
		
		try {
			if(surveyQuestionIdStr != null) {
				surveyQuestionId = Integer.parseInt(surveyQuestionIdStr);
			}
		} catch(NumberFormatException e) {
			surveyQuestionId = 0;
		}

		try {
			if(surveySectionIdStr != null) {
				surveySectionId = Integer.parseInt(surveySectionIdStr);
			}
		} catch(NumberFormatException e) {
			surveySectionId = 0;
		}
		
		if (surveyQuestionId > 0) {
			surveyQuestionDAO.duplicateQuestion(surveyQuestionId, surveySectionId);
		}
		
		return null;
	}

	public void setSurveyQuestionDAO(SurveyQuestionDAO surveyQuestionDAO) {
		this.surveyQuestionDAO = surveyQuestionDAO;
	}

}
