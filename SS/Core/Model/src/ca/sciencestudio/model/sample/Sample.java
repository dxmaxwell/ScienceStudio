/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Sample class.
 *     
 */
package ca.sciencestudio.model.sample;

import java.util.Set;
import java.util.HashSet;

import ca.sciencestudio.model.Model;
import ca.sciencestudio.model.sample.validators.SampleValidator;

/**
 * @author maxweld
 *
 */
public class Sample implements Model {
	
	private static final long serialVersionUID = 1L;
	
	public static final String GID_TYPE = "A";
	
	public static enum State { SOLID, LIQUID, GAS, MIXED }
	
	public static enum Hazard { CORROSIVE, REACTIVE, FLAMMABLE, OXIDIZER, TOXIC, OTHER }
	
	private String gid;
	private String projectGid;
	private String name;
	private String description;
	private State state;
	private String quantity;
	private String casNumber;
	private Set<Hazard> hazards;
	private String otherHazards;
	
	public Sample() {
		gid = SampleValidator.DEFAULT_GID;
		projectGid = SampleValidator.DEFAULT_PROJECT_GID;
		name = SampleValidator.DEFAULT_NAME;
		description = SampleValidator.DEFAULT_DESCRIPTION;
		state = SampleValidator.DEFAULT_STATE;
		quantity =SampleValidator.DEFAULT_QUANTITY;
		casNumber = SampleValidator.DEFAULT_CAS_NUMBER;
		hazards = new HashSet<Hazard>(SampleValidator.DEFAULT_HAZARDS);
		otherHazards = SampleValidator.DEFAULT_OTHER_HAZARDS;
	}
	
	public Sample(Sample sample) {
		gid = sample.getGid();
		projectGid = sample.getProjectGid();
		name = sample.getName();
		description = sample.getDescription();
		state = sample.getState();
		quantity = sample.getQuantity();
		casNumber = sample.getCasNumber();
		hazards = sample.getHazards();
		otherHazards = sample.getOtherHazards();
		
	}
	
	@Override
	public String getGid() {
		return gid;
	}
	@Override
	public void setGid(String gid) {
		this.gid = gid;
	}
	
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
