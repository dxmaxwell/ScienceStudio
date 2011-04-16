package ca.sciencestudio.uso.survey.service.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ca.sciencestudio.uso.survey.model.SurveyCategory;

public class SurveyCategoryValidator implements Validator {

	@SuppressWarnings("unchecked")
	public boolean supports(Class cls) {
		return SurveyCategory.class.isAssignableFrom(cls);
	}

	public void validate(Object object, Errors errors) {
		SurveyCategory surveyCategory = (SurveyCategory) object;
		
		if (surveyCategory.getName() == null || surveyCategory.getName().equals("")) {
			errors.rejectValue("name", surveyCategory.getClass().getName() + ".name", "Survey Category name is required!");
		}
		
	}

}
