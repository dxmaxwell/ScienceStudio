package ca.sciencestudio.uso.survey.service.validators;

import java.util.Date;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ca.sciencestudio.uso.survey.model.Survey;
import ca.sciencestudio.uso.survey.model.dao.SurveyParticipantDAO;


public class SurveyValidator implements Validator {

	private SurveyParticipantDAO surveyParticipantDAO;
	
	@SuppressWarnings("unchecked")
	public boolean supports(Class cls) {
		return Survey.class.isAssignableFrom(cls);
	}

	public void validate(Object object, Errors errors) {
		Survey survey = (Survey) object;
		
		if (survey.getName() == null || survey.getName().equals("")) {
			errors.rejectValue("name", survey.getClass().getName() + ".name", "Survey name is required!");
		}
		
		if (survey.getDeadline() == null) {
			errors.rejectValue("deadline", survey.getClass().getName() + ".deadline", "Survey deadline is required!");
		}
		
		Date maxRemindDate = surveyParticipantDAO.getMaxRemindDate(survey.getId());
		if (survey.getDeadline() != null && maxRemindDate != null && !survey.getDeadline().after(maxRemindDate)) {
			errors.rejectValue("deadline", survey.getClass().getName() + ".deadline", "Survey deadline must be after all Remind Date of Participants!");
		}
		
	}

	public void setSurveyParticipantDAO(SurveyParticipantDAO surveyParticipantDAO) {
		this.surveyParticipantDAO = surveyParticipantDAO;
	}
}
