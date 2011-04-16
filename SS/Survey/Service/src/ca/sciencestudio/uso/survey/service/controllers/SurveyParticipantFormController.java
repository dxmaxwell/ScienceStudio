package ca.sciencestudio.uso.survey.service.controllers;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import ca.sciencestudio.uso.survey.model.SurveyParticipant;
import ca.sciencestudio.uso.survey.model.dao.SurveyParticipantDAO;
import ca.lightsource.directory.model.Person;
import ca.lightsource.directory.model.dao.PersonDAO;
import ca.sciencestudio.uso.survey.model.SurveyParticipantStatus;


public class SurveyParticipantFormController extends SimpleFormController {
	
	private SurveyParticipantDAO surveyParticipantDAO;
	private PersonDAO personDAO;
	
	protected ModelAndView onSubmit(Object command) throws Exception {
		SurveyParticipant surveyParticipant = (SurveyParticipant) command;
		
		int surveyParticipantId = surveyParticipant.getId();
		if (surveyParticipantId != 0) {
			surveyParticipantDAO.updateParticipant(surveyParticipant);
		} else {
			surveyParticipantId = surveyParticipantDAO.addParticipant(surveyParticipant);
		}
		
		int surveyId = surveyParticipant.getSurveyId();
		return new ModelAndView("redirect:surveyParticipant?surveyParticipantId=" + surveyParticipantId + "&surveyId=" + surveyId + "&activeTab=details");
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		String surveyIdParam = request.getParameter("surveyId");
		String surveyParticipantIdParam = request.getParameter("surveyParticipantId");
		
		int surveyId = 0;
		try {
			if (surveyIdParam != null) {
				surveyId = Integer.parseInt(surveyIdParam);
			}
		} catch (Exception e) {
			surveyId = 0;
		}
		
		int surveyParticipantId = 0;
		try {
			if (surveyParticipantIdParam != null) {
				surveyParticipantId = Integer.parseInt(surveyParticipantIdParam);
			}
		} catch (Exception e) {
			surveyParticipantId = 0;
		}
		
		SurveyParticipant surveyParticipant = null;
		
		if(surveyParticipantId != 0) {
			surveyParticipant = surveyParticipantDAO.getParticipantById(surveyParticipantId);
		} else {
			surveyParticipant = new SurveyParticipant();
			if (surveyId != 0) {
				surveyParticipant.setSurveyId(surveyId);
			}
			surveyParticipant.setStatus(0);
		}
		
		return surveyParticipant;
	}
	
	protected Map<Object,Object> referenceData(HttpServletRequest request) throws Exception {
		
		String surveyIdParam = request.getParameter("surveyId");
		String surveyParticipantIdParam = request.getParameter("surveyParticipantId");
		int surveyId = 0;
		int surveyParticipantId = 0;
		
		Map<Object, Object> model = new HashMap<Object, Object>();

		try {
			if(surveyIdParam != null) {
				surveyId = Integer.parseInt(surveyIdParam);
			}
		} catch (Exception e) {
			surveyId = 0;
		}
		
		try {
			if(surveyParticipantIdParam != null) {
				surveyParticipantId = Integer.parseInt(surveyParticipantIdParam);
			}
		} catch (Exception e) {
			surveyParticipantId = 0;
		}
		
		if (surveyParticipantId !=0) {
			model.put("titleMsg", "Editing survey participant");
		} else {
			model.put("titleMsg", "New survey participant");
		}

		List<Person> people = personDAO.getPersonList();
		model.put("people", people);
		
		List<SurveyParticipantStatus> surveyParticipantStatusList = Arrays.asList(SurveyParticipantStatus.values());
		model.put("surveyParticipantStatusList", surveyParticipantStatusList);
		
		model.put("surveyId", surveyId);
		
		model.put("surveyParticipantId", surveyParticipantId);
		
		return model;
	}
	
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		df.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(df, true));
	}

	public void setSurveyParticipantDAO(SurveyParticipantDAO surveyParticipantDAO) {
		this.surveyParticipantDAO = surveyParticipantDAO;
	}

	public void setPersonDAO(PersonDAO personDAO) {
		this.personDAO = personDAO;
	}
	
}
