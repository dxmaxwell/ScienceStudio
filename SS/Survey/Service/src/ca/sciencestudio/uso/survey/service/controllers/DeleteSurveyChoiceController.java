package ca.sciencestudio.uso.survey.service.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ca.sciencestudio.uso.survey.model.dao.SurveyChoiceDAO;


public class DeleteSurveyChoiceController implements Controller {
	
	private SurveyChoiceDAO surveyChoiceDAO;

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String surveyChoiceIdStr = request.getParameter("id");
		
		int surveyChoiceId = 0;
		
		try {
			if (surveyChoiceIdStr != null) {
				surveyChoiceId = Integer.parseInt(surveyChoiceIdStr);
			}
		} catch (NumberFormatException e) {
			surveyChoiceId = 0;
		}
		
		if (surveyChoiceId > 0) {
			surveyChoiceDAO.removeChoice(surveyChoiceId);
		}
		
		return null;
	}

	public void setSurveyChoiceDAO(SurveyChoiceDAO surveyChoiceDAO) {
		this.surveyChoiceDAO = surveyChoiceDAO;
	}

}
