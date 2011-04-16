package ca.sciencestudio.uso.survey.service.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ca.sciencestudio.uso.survey.model.SurveyChoice;
import ca.sciencestudio.uso.survey.model.dao.SurveyChoiceDAO;


public class SetSurveyChoiceImageController implements Controller {

	private SurveyChoiceDAO surveyChoiceDAO;
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String choiceIdParam = request.getParameter("choiceId");
		String image = request.getParameter("image");
		
		int choiceId = 0;
		
		try {
			if(choiceIdParam != null) {
				choiceId = Integer.parseInt(choiceIdParam);
			}
		} catch(Exception e) {
			choiceId = 0;
		}
		
		SurveyChoice surveyChoice = null;
		
		if (choiceId != 0) {
			surveyChoice = surveyChoiceDAO.getChoiceById(choiceId);
			if (surveyChoice != null) {
				if (image.equals("")) {
					surveyChoice.setImage(null);
				} else {
					surveyChoice.setImage(image);
				}
				
				surveyChoiceDAO.updateChoice(surveyChoice);
			}
		}
		
		return null;
	}

	public void setSurveyChoiceDAO(SurveyChoiceDAO surveyChoiceDAO) {
		this.surveyChoiceDAO = surveyChoiceDAO;
	}

}
