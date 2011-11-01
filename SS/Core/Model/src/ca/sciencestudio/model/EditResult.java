/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     EditResult class.
 *     
 */
package ca.sciencestudio.model;

import org.springframework.validation.Errors;

import ca.sciencestudio.util.rest.ValidationResult;

/**
 * @author maxweld
 * 
 *
 */
public class EditResult extends ValidationResult {

	public EditResult() {
		super();
	}

	public EditResult(Errors errors) {
		super(errors);
	}
	
	// reserved for future use //
}
