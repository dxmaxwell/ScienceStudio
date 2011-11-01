/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AddResult class.
 *     
 */
package ca.sciencestudio.util.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.validation.Errors;


/**
 * @author maxweld
 *
 */
public class AddResult extends ValidationResult {

	private List<String> locations = new ArrayList<String>();

	public AddResult() {}
	
	public AddResult(String defaultMessage) {
		super(defaultMessage);
	}

	public AddResult(Collection<String> locations) {
		this.locations.addAll(locations);
	}
	
	public AddResult(Errors errors) {
		super(errors);
	}
	
	public List<String> getLocations() {
		return locations;
	}
	public void setLocations(List<String> locations) {
		this.locations = locations;
	}
}
