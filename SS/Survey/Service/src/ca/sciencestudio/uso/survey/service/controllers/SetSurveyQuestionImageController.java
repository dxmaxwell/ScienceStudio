package ca.sciencestudio.uso.survey.service.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ca.sciencestudio.uso.survey.model.SurveyQuestion;
import ca.sciencestudio.uso.survey.model.dao.SurveyQuestionDAO;


public class SetSurveyQuestionImageController implements Controller {

	private SurveyQuestionDAO surveyQuestionDAO;
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String questionIdParam = request.getParameter("questionId");
		String image = request.getParameter("image");
		
		int questionId = 0;
		
		try {
			if(questionIdParam != null) {
				questionId = Integer.parseInt(questionIdParam);
			}
		} catch(Exception e) {
			questionId = 0;
		}
		
		SurveyQuestion surveyQuestion = null;
		
		if (questionId != 0) {
			surveyQuestion = surveyQuestionDAO.getQuestionById(questionId);
			if (surveyQuestion != null) {
				if (image.equals("")) {
					surveyQuestion.setImage(null);
				} else {
					surveyQuestion.setImage(image);
				}
				
				surveyQuestionDAO.updateQuestion(surveyQuestion);
			}
		}
		
		return null;
	}

	public void setSurveyQuestionDAO(SurveyQuestionDAO surveyQuestionDAO) {
		this.surveyQuestionDAO = surveyQuestionDAO;
	}

}
