package ca.sciencestudio.uso.survey.service.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ca.sciencestudio.uso.survey.model.SurveyChoice;


public class SurveyChoiceValidator implements Validator {

	@SuppressWarnings("unchecked")
	public boolean supports(Class cls) {
		return SurveyChoice.class.isAssignableFrom(cls);
	}

	public void validate(Object object, Errors errors) {
		SurveyChoice surveyChoice = (SurveyChoice) object;
		
		if (surveyChoice.getText() == null || surveyChoice.getText().equals("")) {
			errors.rejectValue("text", surveyChoice.getClass().getName() + ".text", "Survey choice text is required!");
		}
		
		if (surveyChoice.getOrder() <= 0) {
			errors.rejectValue("order", surveyChoice.getClass().getName() + ".order", "Survey choice order must be a positive integer!");
		}
		
	}

}
