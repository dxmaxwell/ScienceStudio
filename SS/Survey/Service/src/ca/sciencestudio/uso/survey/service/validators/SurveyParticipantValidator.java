package ca.sciencestudio.uso.survey.service.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ca.sciencestudio.uso.survey.model.SurveyParticipant;
import ca.sciencestudio.uso.survey.model.Survey;
import ca.sciencestudio.uso.survey.model.dao.SurveyDAO;


public class SurveyParticipantValidator implements Validator {

	private SurveyDAO surveyDAO;
	
	@SuppressWarnings("unchecked")
	public boolean supports(Class cls) {
		return SurveyParticipant.class.isAssignableFrom(cls);
	}

	public void validate(Object object, Errors errors) {
		SurveyParticipant surveyParticipant = (SurveyParticipant) object;
		
		if (surveyParticipant.getPersonId() <= 0) {
			errors.rejectValue("personId", surveyParticipant.getClass().getName() + ".personId", "Survey participant person is required!");
		}
		
		if (surveyParticipant.getNotificationDate() == null) {
			errors.rejectValue("notificationDate", surveyParticipant.getClass().getName() + ".notificationDate", "Survey Participant Notification Date is required!");
		}
		
		if (surveyParticipant.getRemindDate1() == null) {
			errors.rejectValue("remindDate1", surveyParticipant.getClass().getName() + ".remindDate1", "Survey Participant First Remind Date is required!");
		}
		
		if (surveyParticipant.getRemindDate2() == null) {
			errors.rejectValue("remindDate2", surveyParticipant.getClass().getName() + ".remindDate2", "Survey Participant Second Remind Date is required!");
		}
		
		if (surveyParticipant.getNotificationDate() != null && surveyParticipant.getRemindDate1() != null
				&& !surveyParticipant.getNotificationDate().before(surveyParticipant.getRemindDate1())) {
			errors.rejectValue("notificationDate", surveyParticipant.getClass().getName() + ".notificationDate", "Survey Participant Notification Date must be before First Remind Date!");
		}
		
		if (surveyParticipant.getRemindDate1() != null && surveyParticipant.getRemindDate2() != null
				&& !surveyParticipant.getRemindDate1().before(surveyParticipant.getRemindDate2())) {
			errors.rejectValue("remindDate1", surveyParticipant.getClass().getName() + ".remindDate1", "Survey Participant First Remind Date must be before Second Remind Date!");
		}
		
		Survey survey = surveyDAO.getSurveyById(surveyParticipant.getSurveyId());
		
		if (surveyParticipant.getNotificationDate() != null && !surveyParticipant.getNotificationDate().before(survey.getDeadline())) {
			errors.rejectValue("notificationDate", surveyParticipant.getClass().getName() + ".notificationDate", "Survey Participant Notification Date must be before Survey Deadline!");
		}
		
		if (surveyParticipant.getRemindDate1() != null && !surveyParticipant.getRemindDate1().before(survey.getDeadline())) {
			errors.rejectValue("remindDate1", surveyParticipant.getClass().getName() + ".remindDate1", "Survey Participant First Remind Date must be before Survey Deadline!");
		}
		
		if (surveyParticipant.getRemindDate2() != null && !surveyParticipant.getRemindDate2().before(survey.getDeadline())) {
			errors.rejectValue("remindDate2", surveyParticipant.getClass().getName() + ".remindDate2", "Survey Participant Second Remind Date must be before Survey Deadline!");
		}
	}

	public void setSurveyDAO(SurveyDAO surveyDAO) {
		this.surveyDAO = surveyDAO;
	}
}
