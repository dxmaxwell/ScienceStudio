package ca.sciencestudio.uso.survey.service.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;


public class GetSurveyParticipantController implements Controller {
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String surveyIdStr = request.getParameter("surveyId");
		String surveyParticipantIdStr = request.getParameter("surveyParticipantId");
		String activeTab = request.getParameter("activeTab");
		
		int surveyId = 0;
		int surveyParticipantId = 0;
		Map<Object, Object> model = new HashMap<Object, Object>();
		
		try {
			if(surveyIdStr != null) {
				surveyId = Integer.parseInt(surveyIdStr);
			}
		} catch (NumberFormatException e) {
			surveyId = 0;
		}

		try {
			if(surveyParticipantIdStr != null) {
				surveyParticipantId = Integer.parseInt(surveyParticipantIdStr);
			}
		} catch (NumberFormatException e) {
			surveyParticipantId = 0;
		}
		
		model.put("surveyId", surveyId);
		model.put("surveyParticipantId", surveyParticipantId);
		
		if (activeTab == null) {
			model.put("activeTab", "details");
		} else {
			model.put("activeTab", activeTab);
		}
		
		return new ModelAndView("surveyParticipant", model);
	}

}
