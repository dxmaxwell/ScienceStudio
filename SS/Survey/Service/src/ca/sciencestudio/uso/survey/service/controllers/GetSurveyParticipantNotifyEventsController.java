package ca.sciencestudio.uso.survey.service.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ca.sciencestudio.uso.survey.model.SurveyParticipantNotifyEvent;
import ca.sciencestudio.uso.survey.model.dao.SurveyParticipantNotifyEventDAO;


public class GetSurveyParticipantNotifyEventsController implements Controller {
	
	private SurveyParticipantNotifyEventDAO surveyParticipantNotifyEventDAO;
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Map<Object, Object> model = new HashMap<Object, Object>();
		int surveyParticipantId = 0;
		List<SurveyParticipantNotifyEvent> surveyParticipantNotifyEvents = null;
		
		//check passed parameter
		String objectId = request.getParameter("surveyParticipantId");
		try {
			if (objectId != null) {
				surveyParticipantId = Integer.parseInt(objectId);
			}
		} catch (NumberFormatException e) {
			surveyParticipantId = 0;
		}
		
		if (surveyParticipantId != 0) {
			surveyParticipantNotifyEvents = surveyParticipantNotifyEventDAO.getNotifyEventListByParticipantId(surveyParticipantId);
		}
		
		model.put("surveyParticipantNotifyEvents", surveyParticipantNotifyEvents);
		
		return new ModelAndView("surveyParticipantNotifyEvents", model);
	}
	
	public void setSurveyParticipantNotifyEventDAO(SurveyParticipantNotifyEventDAO surveyParticipantNotifyEventDAO) {
		this.surveyParticipantNotifyEventDAO = surveyParticipantNotifyEventDAO;
	}
}
