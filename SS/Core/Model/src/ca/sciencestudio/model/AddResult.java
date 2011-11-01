/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AddResult class.
 *     
 */
package ca.sciencestudio.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.validation.Errors;

import ca.sciencestudio.util.rest.ValidationResult;

/**
 * @author maxweld
 *
 */
public class AddResult extends ValidationResult {

	private List<String> locations;

	public AddResult() {
		locations = new ArrayList<String>();
	}
	
	public AddResult(String location) {
		locations = new ArrayList<String>(Collections.singletonList(location));		
	}
	
	public AddResult(Errors errors) {
		super(errors);
		locations = new ArrayList<String>();
	}
	
	public List<String> getLocations() {
		return locations;
	}
	public void setLocations(List<String> locations) {
		this.locations = locations;
	}
}
