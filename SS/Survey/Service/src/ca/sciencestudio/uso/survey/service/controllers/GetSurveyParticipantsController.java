package ca.sciencestudio.uso.survey.service.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ca.sciencestudio.uso.survey.model.SurveyParticipant;
import ca.sciencestudio.uso.survey.model.SurveyParticipantStatus;
import ca.sciencestudio.uso.survey.model.dao.SurveyParticipantDAO;
import ca.sciencestudio.uso.survey.service.backers.SurveyParticipantListBacker;
import ca.lightsource.directory.model.Person;
import ca.lightsource.directory.model.dao.PersonDAO;


public class GetSurveyParticipantsController implements Controller {
	
	private SurveyParticipantDAO surveyParticipantDAO;
	private PersonDAO personDAO;
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Map<Object, Object> model = new HashMap<Object, Object>();
		int surveyId = 0;
		List<SurveyParticipant> surveyParticipants = null;
		List<SurveyParticipantListBacker> surveyParticipantListBackers = new ArrayList<SurveyParticipantListBacker>();
		
		//check passed parameter
		String objectId = request.getParameter("surveyId");
		try {
			if (objectId != null) {
				surveyId = Integer.parseInt(objectId);
			}
		} catch (NumberFormatException e) {
			surveyId = 0;
		}
		
		if (surveyId != 0) {
			surveyParticipants = surveyParticipantDAO.getParticipantList(surveyId);
			for (SurveyParticipant surveyParticipant : surveyParticipants) {
				SurveyParticipantListBacker backer = new SurveyParticipantListBacker();
				backer.setId(surveyParticipant.getId());
				
				Person person = personDAO.getPersonById(surveyParticipant.getPersonId());
				backer.setPersonName(person.getLastName() + ", " + person.getFirstName());
				
				backer.setBeamline(surveyParticipant.getBeamline());
				backer.setDescription(surveyParticipant.getDescription());
				
				int status = surveyParticipant.getStatus();
				SurveyParticipantStatus[] surveyParticipantStatusList = SurveyParticipantStatus.values();
				for (SurveyParticipantStatus surveyParticipantStatus : surveyParticipantStatusList) {
					if (surveyParticipantStatus.getValue() == status) {
						backer.setStatus(surveyParticipantStatus.getText());
						break;
					}
				}
				
				backer.setNotificationDate(surveyParticipant.getNotificationDate());
				backer.setRemindDate1(surveyParticipant.getRemindDate1());
				backer.setRemindDate2(surveyParticipant.getRemindDate2());
				
				surveyParticipantListBackers.add(backer);
			}
		}
		
		model.put("surveyParticipants", surveyParticipantListBackers);
		model.put("surveyId", surveyId);
		
		return new ModelAndView("surveyParticipants", model);
	}
	
	public void setSurveyParticipantDAO(SurveyParticipantDAO surveyParticipantDAO) {
		this.surveyParticipantDAO = surveyParticipantDAO;
	}
	
	public void setPersonDAO(PersonDAO personDAO) {
		this.personDAO = personDAO;
	}
}
