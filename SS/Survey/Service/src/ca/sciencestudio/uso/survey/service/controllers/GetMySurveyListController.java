package ca.sciencestudio.uso.survey.service.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsecurity.SecurityUtils;
import org.jsecurity.subject.Subject;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ca.lightsource.directory.model.Person;
import ca.lightsource.directory.model.dao.PersonDAO;
import ca.sciencestudio.uso.survey.model.Survey;
import ca.sciencestudio.uso.survey.model.dao.SurveyDAO;
import ca.sciencestudio.uso.survey.model.SurveyParticipant;
import ca.sciencestudio.uso.survey.model.dao.SurveyParticipantDAO;
import ca.sciencestudio.uso.survey.service.backers.MySurveyListBacker;

public class GetMySurveyListController implements Controller {
	
	private PersonDAO personDAO;
	private SurveyDAO surveyDAO;
	private SurveyParticipantDAO surveyParticipantDAO;
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Map<Object, Object> model = new HashMap<Object, Object>();
		
		Subject subject = SecurityUtils.getSubject();
		String username = subject.getPrincipal().toString();
		Person person = personDAO.getPersonByDirectoryKey(username);
		
		List<MySurveyListBacker> backers = new ArrayList<MySurveyListBacker>();
		
		List<SurveyParticipant> participants = surveyParticipantDAO.getParticipantListByPersonId(person.getId());
		
		for(SurveyParticipant participant : participants) {
			MySurveyListBacker backer = new MySurveyListBacker();
			
			backer.setSurveyId(participant.getSurveyId());
			backer.setParticipantId(participant.getId());
			backer.setBeamline(participant.getBeamline());
			switch (participant.getStatus()) {
				case 0: backer.setStatus("Not Started"); break;
				case 1: backer.setStatus("Started"); break;
				case 2: backer.setStatus("Submitted"); break;
			}
			
			Survey survey = surveyDAO.getSurveyById(participant.getSurveyId());
			backer.setSurveyName(survey.getName());
			backer.setDeadline(survey.getDeadline());
			
			Date now = new Date();
			if (survey.getDeadline().before(now)) {
				backer.setIsValid(false);
			} else {
				backer.setIsValid(true);
			}
			
			backers.add(backer);
		}
		
		model.put("mySurveyList", backers);
		
		return new ModelAndView("mySurvey", model);
	}

	public void setPersonDAO(PersonDAO personDAO) {
		this.personDAO = personDAO;
	}

	public void setSurveyDAO(SurveyDAO surveyDAO) {
		this.surveyDAO = surveyDAO;
	}

	public void setSurveyParticipantDAO(SurveyParticipantDAO surveyParticipantDAO) {
		this.surveyParticipantDAO = surveyParticipantDAO;
	}

}
