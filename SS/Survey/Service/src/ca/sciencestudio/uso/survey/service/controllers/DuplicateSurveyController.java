package ca.sciencestudio.uso.survey.service.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ca.sciencestudio.uso.survey.model.dao.SurveyDAO;


public class DuplicateSurveyController implements Controller {
	
	private SurveyDAO surveyDAO;

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String surveyIdStr = request.getParameter("id");
		
		int surveyId = 0;
		
		try {
			if(surveyIdStr != null) {
				surveyId = Integer.parseInt(surveyIdStr);
			}
		} catch(NumberFormatException e) {
			surveyId = 0;
		}
		
		if(surveyId > 0) {
			surveyDAO.duplicateSurvey(surveyId);
		}
		
		return null;
	}

	public void setSurveyDAO(SurveyDAO surveyDAO) {
		this.surveyDAO = surveyDAO;
	}

}
