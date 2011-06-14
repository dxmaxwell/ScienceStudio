/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     GeneralResponse class.
 *     
 */
package ca.sciencestudio.util.web;

import java.util.Locale;
import java.util.Collections;

import org.springframework.validation.Errors;

/**
 * @author maxweld
 * 
 *
 */
public class GeneralResponse extends GenericResponse<Object> {

	public GeneralResponse(Object response) {
		super(response);
	}
	
	public GeneralResponse(Errors errors) {
		super(Collections.emptyMap(), errors);
	}
	
	public GeneralResponse(Errors errors, Locale locale) {
		super(Collections.emptyMap(), errors, locale);
	}
	
	public GeneralResponse(Object response, boolean success) {
		super(response, success);
	}

	public GeneralResponse(Object response, Errors errors) {
		super(response, errors);
	}
	
	public GeneralResponse(Object response, Errors errors, Locale locale) {
		super(response, errors, locale);
	}	
}
