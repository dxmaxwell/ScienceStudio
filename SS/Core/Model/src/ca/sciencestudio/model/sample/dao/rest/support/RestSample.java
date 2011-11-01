/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestSample class.
 *     
 */
package ca.sciencestudio.model.sample.dao.rest.support;

import java.util.Set;
import java.util.HashSet;

import ca.sciencestudio.model.sample.Sample.State;
import ca.sciencestudio.model.sample.Sample.Hazard;
import ca.sciencestudio.model.sample.validators.SampleValidator;

/**
 * @author maxweld
 *
 */
public class RestSample {

	private String projectGid = SampleValidator.DEFAULT_PROJECT_GID;
	private String name = SampleValidator.DEFAULT_NAME;
	private String description = SampleValidator.DEFAULT_DESCRIPTION;
	private State state = SampleValidator.DEFAULT_STATE;
	private String quantity = SampleValidator.DEFAULT_QUANTITY;
	private String casNumber = SampleValidator.DEFAULT_CAS_NUMBER;
	private Set<Hazard> hazards = new HashSet<Hazard>(SampleValidator.DEFAULT_HAZARDS);
	private String otherHazards = SampleValidator.DEFAULT_OTHER_HAZARDS;
	
	public String getProjectGid() {
		return projectGid;
	}
	public void setProjectGid(String projectGid) {
		this.projectGid = projectGid;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getCasNumber() {
		return casNumber;
	}
	public void setCasNumber(String casNumber) {
		this.casNumber = casNumber;
	}
	
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	
	public Set<Hazard> getHazards() {
		return hazards;
	}
	public void setHazards(Set<Hazard> hazards) {
		this.hazards = hazards;
	}
	
	public String getOtherHazards() {
		return otherHazards;
	}
	public void setOtherHazards(String otherHazards) {
		this.otherHazards = otherHazards;
	}
}
