/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     EditResult class.
 *     
 */
package ca.sciencestudio.util.rest;

import org.springframework.validation.Errors;


/**
 * @author maxweld
 * 
 *
 */
public class EditResult extends ValidationResult {

	public EditResult() {
		super();
	}

	public EditResult(String defaultMessage) {
		super(defaultMessage);
	}

	public EditResult(Errors errors) {
		super(errors);
	}
	
	// reserved for future use //
}
