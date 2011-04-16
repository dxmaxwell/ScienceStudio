package ca.sciencestudio.uso.survey.service.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ca.sciencestudio.uso.survey.model.dao.SurveyParticipantDAO;


public class DeleteSurveyParticipantController implements Controller {
	
	private SurveyParticipantDAO surveyParticipantDAO;

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String surveyParticipantIdStr = request.getParameter("id");
		
		int surveyParticipantId = 0;
		
		try {
			if (surveyParticipantIdStr != null) {
				surveyParticipantId = Integer.parseInt(surveyParticipantIdStr);
			}
		} catch (NumberFormatException e) {
			surveyParticipantId = 0;
		}
		
		if (surveyParticipantId > 0) {
			surveyParticipantDAO.removeParticipant(surveyParticipantId);
		}
		
		return null;
	}

	public void setSurveyParticipantDAO(SurveyParticipantDAO surveyParticipantDAO) {
		this.surveyParticipantDAO = surveyParticipantDAO;
	}

}
