/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ValidationResult class.
 *     
 */
package ca.sciencestudio.util.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

/**
 * @author maxweld
 * 
 *
 */
public class ValidationResult {

	private List<ValidationError> globalErrors;
	private Map<String,ValidationError> fieldErrors;
	
	public ValidationResult() {
		globalErrors = new ArrayList<ValidationError>();
		fieldErrors = new HashMap<String,ValidationError>();
	}
	
	public ValidationResult(Errors errors) {
		globalErrors = new ArrayList<ValidationError>();
		for(ObjectError error : errors.getGlobalErrors()) {
			globalErrors.add(new ValidationError(error));
		}
		fieldErrors = new HashMap<String,ValidationError>();
		for(FieldError error : errors.getFieldErrors()) {
			fieldErrors.put(error.getField(), new ValidationError(error));
		}
	}
	
	public boolean hasErrors() {
		return hasGlobalErrors() || hasFieldErrors();
	}
	
	public boolean hasGlobalErrors() {
		return !globalErrors.isEmpty();
	}
	
	public boolean hasFieldErrors() {
		return !fieldErrors.isEmpty();
	}
	
	public boolean hasFieldErrors(String field) {
		return fieldErrors.containsKey(field);
	}
	
	public List<ValidationError> getGlobalErrors() {
		return globalErrors;
	}
	public void setGlobalErrors(List<ValidationError> globalErrors) {
		this.globalErrors = globalErrors;
	}

	public Map<String, ValidationError> getFieldErrors() {
		return fieldErrors;
	}
	public void setFieldErrors(Map<String, ValidationError> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}
}
