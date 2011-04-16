package ca.sciencestudio.uso.survey.service.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ca.sciencestudio.uso.survey.model.SurveySection;


public class SurveySectionValidator implements Validator {

	@SuppressWarnings("unchecked")
	public boolean supports(Class cls) {
		return SurveySection.class.isAssignableFrom(cls);
	}

	public void validate(Object object, Errors errors) {
		SurveySection surveySection = (SurveySection) object;
		
		if (surveySection.getName() == null || surveySection.getName().equals("")) {
			errors.rejectValue("name", surveySection.getClass().getName() + ".name", "Survey section name is required!");
		}
		
		if (surveySection.getOrder() <= 0) {
			errors.rejectValue("order", surveySection.getClass().getName() + ".order", "Survey section order  must be a positive integer!");
		}
		
	}

}
