package ca.sciencestudio.uso.survey.service.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;


public class GetSurveyChoiceController implements Controller {
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String surveyQuestionIdStr = request.getParameter("surveyQuestionId");
		String surveyChoiceIdStr = request.getParameter("surveyChoiceId");
		String activeTab = request.getParameter("activeTab");
		
		int surveyQuestionId = 0;
		int surveyChoiceId = 0;
		
		Map<Object, Object> model = new HashMap<Object, Object>();
		
		try {
			if (surveyChoiceIdStr != null) {
				surveyChoiceId = Integer.parseInt(surveyChoiceIdStr);
			}
		} catch (NumberFormatException e) {
			surveyChoiceId = 0;
		}

		try {
			if (surveyQuestionIdStr != null) {
				surveyQuestionId = Integer.parseInt(surveyQuestionIdStr);
			}
		} catch (NumberFormatException e) {
			surveyQuestionId = 0;
		}
		
		model.put("surveyQuestionId", surveyQuestionId);
		model.put("surveyChoiceId", surveyChoiceId);
		
		if (activeTab == null) {
			model.put("activeTab", "details");
		} else {
			model.put("activeTab", activeTab);
		}
		
		return new ModelAndView("surveyChoice", model);
	}

}
