package ca.sciencestudio.uso.survey.service.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ca.sciencestudio.uso.survey.model.dao.SurveyQuestionDAO;


public class DeleteSurveyQuestionController implements Controller {
	
	private SurveyQuestionDAO surveyQuestionDAO;

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String surveyQuestionIdStr = request.getParameter("id");
		
		int surveyQuestionId = 0;
		
		try {
			if (surveyQuestionIdStr != null) {
				surveyQuestionId = Integer.parseInt(surveyQuestionIdStr);
			}
		} catch (NumberFormatException e) {
			surveyQuestionId = 0;
		}
		
		if (surveyQuestionId > 0) {
			surveyQuestionDAO.removeQuestion(surveyQuestionId);
		}
		
		return null;
	}

	public void setSurveyQuestionDAO(SurveyQuestionDAO surveyQuestionDAO) {
		this.surveyQuestionDAO = surveyQuestionDAO;
	}

}
