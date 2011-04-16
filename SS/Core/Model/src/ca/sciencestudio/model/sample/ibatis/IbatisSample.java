/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SampleHazard class.
 *     
 */
package ca.sciencestudio.model.sample.ibatis;

import java.util.HashSet;
import java.util.Set;

import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.sample.SampleHazard;
import ca.sciencestudio.model.sample.SampleState;

/**
 * @author maxweld
 *
 */
public class IbatisSample implements Sample {
	
	private static final long serialVersionUID = 1L;
	private static final String HAZARDS_STRING_SEPARATOR = ",";
	
	private int id;
	private int projectId;
	private String name;
	private String description;
	private String quantity;
	private String casNumber;
	private SampleState state;
	private String otherHazards;
	private Set<SampleHazard> hazards;
	
	public String getStateString() {
		return state.name();
	}
	
	public void setStateString(String stateString) {
		try {
			state = SampleState.valueOf(stateString);
		}
		catch(IllegalArgumentException e) {
			state = SampleState.UNKNOWN;
		}
	}
	
	public String getHazardsString() {
		StringBuffer buffer = new StringBuffer();
		
		boolean first = true;
		for(SampleHazard hazard : hazards) {
			if(first) {
				first = false;
			} else {
				buffer.append(HAZARDS_STRING_SEPARATOR);
			}
			buffer.append(hazard.name());
		}
		
		return buffer.toString();
	}
	
	public void setHazardsString(String hazardsString) {
		String[] splitHazardsString = hazardsString.split(HAZARDS_STRING_SEPARATOR);
		hazards = new HashSet<SampleHazard>();
		
		for(String hazardString : splitHazardsString) {
			try {
				hazards.add(SampleHazard.valueOf(hazardString));
			}
			catch (IllegalArgumentException e) {
				hazards.add(SampleHazard.UNKNOWN);
			}
		}
	}
	
	@Override
	public int getId() {
		return id;
	}
	@Override
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public int getProjectId() {
		return projectId;
	}
	@Override
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	
	@Override
	public String getName() {
		return name;
	}
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	@Override
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String getQuantity() {
		return quantity;
	}
	@Override
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	
	@Override
	public String getCasNumber() {
		return casNumber;
	}
	@Override
	public void setCasNumber(String casNumber) {
		this.casNumber = casNumber;
	}
	
	@Override
	public SampleState getState() {
		return state;
	}
	@Override
	public void setState(SampleState state) {
		this.state = state;
	}

	@Override
	public String getOtherHazards() {
		return otherHazards;
	}
	@Override
	public void setOtherHazards(String otherHazards) {
		this.otherHazards = otherHazards;
	}

	@Override
	public Set<SampleHazard> getHazards() {
		return hazards;
	}
	@Override
	public void setHazards(Set<SampleHazard> hazards) {
		this.hazards = hazards;
	}
}
