/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Sample class.
 *     
 */
package ca.sciencestudio.model.sample;

import java.util.Collections;
import java.util.Set;

import ca.sciencestudio.model.Model;
import ca.sciencestudio.model.utilities.GID;


/**
 * @author maxweld
 *
 */
public class Sample implements Model {
	
	private static final long serialVersionUID = 1L;
	
	public static final String GID_TYPE = "A";
	
	public static enum State { SOLID, LIQUID, GAS, MIXED }
	
	public static enum Hazard { CORROSIVE, REACTIVE, FLAMMABLE, OXIDIZER, TOXIC, OTHER }
	
	// Maximum field length should match database limits. //
	////////////////////////////////////////////////////////
	
	public static final String DEFAULT_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_PROJECT_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_NAME = "";
	public static final String DEFAULT_DESCRIPTION = "";
	public static final String DEFAULT_CAS_NUMBER = "";
	public static final String DEFAULT_STATE = "";
	public static final String DEFAULT_QUANTITY = "";
	public static final Set<String> DEFAULT_HAZARDS = Collections.emptySet();
	public static final String DEFAULT_OTHER_HAZARDS = "";
	
	private String gid = DEFAULT_GID;
	private String projectGid = DEFAULT_PROJECT_GID;
	private String name = DEFAULT_NAME;
	private String description = DEFAULT_DESCRIPTION;
	private String casNumber = DEFAULT_CAS_NUMBER;
	private String state = DEFAULT_STATE;
	private String quantity = DEFAULT_QUANTITY;
	private Set<String> hazards = DEFAULT_HAZARDS;
	private String otherHazards = DEFAULT_OTHER_HAZARDS;
	
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
