package ca.sciencestudio.uso.survey.service.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ca.sciencestudio.uso.survey.model.SurveyChoice;
import ca.sciencestudio.uso.survey.model.dao.SurveyChoiceDAO;


public class GetSurveyChoicesController implements Controller {
	
	private SurveyChoiceDAO surveyChoiceDAO;
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Map<Object, Object> model = new HashMap<Object, Object>();
		int surveyQuestionId = 0;
		List<SurveyChoice> surveyChoices = null;
		
		//check passed parameter
		String objectId = request.getParameter("surveyQuestionId");
		try {
			if (objectId != null) {
				surveyQuestionId = Integer.parseInt(objectId);
			}
		} catch (NumberFormatException e) {
			surveyQuestionId = 0;
		}
		
		if (surveyQuestionId != 0)
			surveyChoices = surveyChoiceDAO.getChoiceList(surveyQuestionId);
		
		model.put("surveyChoices", surveyChoices);
		model.put("surveyQuestionId", surveyQuestionId);
		
		return new ModelAndView("surveyChoices", model);
	}
	
	public void setSurveyChoiceDAO(SurveyChoiceDAO surveyChoiceDAO) {
		this.surveyChoiceDAO = surveyChoiceDAO;
	}
}
