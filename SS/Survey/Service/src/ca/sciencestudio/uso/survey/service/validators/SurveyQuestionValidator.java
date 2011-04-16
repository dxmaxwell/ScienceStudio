package ca.sciencestudio.uso.survey.service.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ca.sciencestudio.uso.survey.model.SurveyQuestion;


public class SurveyQuestionValidator implements Validator {

	@SuppressWarnings("unchecked")
	public boolean supports(Class cls) {
		return SurveyQuestion.class.isAssignableFrom(cls);
	}

	public void validate(Object object, Errors errors) {
		SurveyQuestion surveyQuestion = (SurveyQuestion) object;
		
		if (surveyQuestion.getText() == null || surveyQuestion.getText().equals("")) {
			errors.rejectValue("text", surveyQuestion.getClass().getName() + ".text", "Survey question text is required!");
		}
		
		if (surveyQuestion.getOrder() <= 0) {
			errors.rejectValue("order", surveyQuestion.getClass().getName() + ".order", "Survey question order must be a positive integer!");
		}
		
	}

}
