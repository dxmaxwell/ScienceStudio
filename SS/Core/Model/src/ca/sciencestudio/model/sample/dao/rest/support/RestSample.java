/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestSample class.
 *     
 */
package ca.sciencestudio.model.sample.dao.rest.support;

import java.util.Set;

import ca.sciencestudio.model.sample.Sample;

/**
 * @author maxweld
 *
 */
public class RestSample {

	private int projectId = Sample.DEFAULT_PROJECT_ID;
	private String name = Sample.DEFAULT_NAME;
	private String description = Sample.DEFAULT_DESCRIPTION;
	private String casNumber = Sample.DEFAULT_CAS_NUMBER;
	private String state = Sample.DEFAULT_STATE;
	private String quantity = Sample.DEFAULT_QUANTITY;
	private Set<String> hazards = Sample.DEFAULT_HAZARDS;
	private String otherHazards = Sample.DEFAULT_OTHER_HAZARDS;
	
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
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
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	
	public Set<String> getHazards() {
		return hazards;
	}
	public void setHazards(Set<String> hazards) {
		this.hazards = hazards;
	}
	
	public String getOtherHazards() {
		return otherHazards;
	}
	public void setOtherHazards(String otherHazards) {
		this.otherHazards = otherHazards;
	}
}
