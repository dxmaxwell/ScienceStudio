/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     GenericResponse class.
 *     
 */
package ca.sciencestudio.util.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

/**
 * @author maxweld
 *
 *
 */
public class GenericResponse<T> {

	private static MessageSource MESSAGE_SOURCE;
	
	public static void setMessageSource(MessageSource messageSource) {
		MESSAGE_SOURCE = messageSource;
	}
	
	public static <T> GenericResponse<T> getInstance(T response) {
		
		return new GenericResponse<T>(response);
	}
	
	private T response;
	private boolean success;
	private Map<String,String> errors;
	private List<String> globalErrors; 
	
	public GenericResponse(T response) {
		this(response, true);
	}
	
	public GenericResponse(T response, boolean success) {
		this.success = success;
		this.response = response;
		this.errors = Collections.emptyMap();
		this.globalErrors = Collections.emptyList();
	}
	
	public GenericResponse(T response, Errors errors) {
		this(response, errors, Locale.getDefault());
	}
		
	public GenericResponse(T response, Errors errors, Locale locale) {
		this(response, !errors.hasErrors());
		List<String> objectErrors = new ArrayList<String>();
		Map<String,String> fieldErrors = new HashMap<String,String>();
		if(MESSAGE_SOURCE == null) {
			for(ObjectError error : errors.getGlobalErrors()) {
				objectErrors.add(error.getDefaultMessage());
			}
			for(FieldError error : errors.getFieldErrors()) {
				fieldErrors.put(error.getField(), error.getDefaultMessage());
			}
		}
		else {
			for(ObjectError error : errors.getGlobalErrors()) {
				objectErrors.add(MESSAGE_SOURCE.getMessage(error, locale));
			}
			for(FieldError error : errors.getFieldErrors()) {
				fieldErrors.put(error.getField(), MESSAGE_SOURCE.getMessage(error, locale));
			}
		}
		this.errors = Collections.unmodifiableMap(fieldErrors);
		this.globalErrors = Collections.unmodifiableList(objectErrors);
	}

	public boolean isSuccess() {
		return success;
	}

	public T getResponse() {
		return response;
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public List<String> getGlobalErrors() {
		return globalErrors;
	}
}
