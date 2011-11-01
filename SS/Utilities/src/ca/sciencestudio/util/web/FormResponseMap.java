/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     FormResponseMap class.
 *     
 */
package ca.sciencestudio.util.web;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import ca.sciencestudio.util.rest.ValidationError;
import ca.sciencestudio.util.rest.ValidationResult;

/**
 * @author maxweld
 * 
 *
 */
public class FormResponseMap extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;
	
	private static final String ERRORS_KEY = "errors";
	private static final String SUCCESS_KEY = "success";
	private static final String MESSAGE_KEY = "message";
	
	public static MessageSource messageSource;
	
	public static MessageSource getMessageSource() {
		return messageSource;
	}
	public static void setMessageSource(MessageSource messageSource) {
		FormResponseMap.messageSource = messageSource;
	}

	public FormResponseMap() {
		super();
	}
	
	public FormResponseMap(boolean success) {
		setSuccess(success);
	}
	
	public FormResponseMap(boolean success, String message) {
		setSuccess(success);
		setMessage(message);
	}
	
	public FormResponseMap(boolean success, String code, String defaultMessage) {
		setSuccess(success);
		setMessage(code, defaultMessage);
	}
	
	public FormResponseMap(boolean success, String code, String defaultMessage, Locale locale) {
		setSuccess(success);
		setMessage(code, defaultMessage, locale);
	}
	
	public FormResponseMap(Errors errors) {
		putErrors(errors);
	}
	
	public FormResponseMap(Errors errors, Locale locale) {
		putErrors(errors, locale);
	}
	
	public FormResponseMap(ValidationResult results) {
		putResults(results);
	}
	
	public FormResponseMap(ValidationResult results, Locale locale) {
		putResults(results, locale);
	}

	public boolean isSuccess() {
		return Boolean.TRUE.equals(get(SUCCESS_KEY));
	}
	public void setSuccess(boolean success) {
		put(SUCCESS_KEY, success);
	}
	
	public String getMessage() {
		return String.valueOf(get(MESSAGE_KEY));
	}
	public void setMessage(String message) {
		put(MESSAGE_KEY, message);
	}
	public void setMessage(String code, String defaultMessage) {
		setMessage(code, defaultMessage, Locale.getDefault());
	}
	public void setMessage(String code, String defaultMessage, Locale locale) {
		if(getMessageSource() == null) {
			setMessage(defaultMessage);
		} else {
			setMessage(getMessageSource().getMessage(code, null, defaultMessage, locale));
		}
	}
	
	public void putErrors(Errors errors) {
		putErrors(errors, Locale.getDefault());
	}

	public void putErrors(Errors errors, Locale locale) {
		setSuccess(!errors.hasErrors());
		
		Map<String,String> errorMap = getErrorMap();
		
		for(FieldError error : errors.getFieldErrors()) {
			if(getMessageSource() == null) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			} else {
				errorMap.put(error.getField(), getMessageSource().getMessage(error, locale));
			}
		}
		
		for(ObjectError error : errors.getGlobalErrors()) {
			if(getMessageSource() == null) {
				setMessage(error.getDefaultMessage());
			} else {
				setMessage(getMessageSource().getMessage(error, locale));
			}
		}
	}
	
	public void putResults(ValidationResult results) {
		putResults(results, Locale.getDefault());
	}
	
	public void putResults(ValidationResult results, Locale locale) {
		setSuccess(!results.hasErrors());
		
		Map<String,String> errorMap = getErrorMap();
		
		for(Map.Entry<String, ValidationError> entry : results.getFieldErrors().entrySet()) {
			if(getMessageSource() == null) {
				errorMap.put(entry.getKey(), entry.getValue().getDefaultMessage());
			} else {
				errorMap.put(entry.getKey(), getMessageSource().getMessage(entry.getValue(), locale));
			}
		}
		
		for(ValidationError error : results.getGlobalErrors()) {
			if(getMessageSource() == null) {
				setMessage(error.getDefaultMessage());
			} else {
				setMessage(getMessageSource().getMessage(error, locale));
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	protected Map<String,String> getErrorMap() {
		Map<String,String> errorMap = null;
		Object obj = get(ERRORS_KEY);
		if(obj instanceof Map) {
			return (Map<String,String>)obj;
		}
		errorMap = new HashMap<String,String>();
		put(ERRORS_KEY, errorMap);
		return errorMap;
	}
}
