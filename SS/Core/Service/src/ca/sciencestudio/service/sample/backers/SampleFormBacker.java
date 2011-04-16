/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SampleBacker class.
 *     
 */
package ca.sciencestudio.service.sample.backers;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.sample.SampleHazard;
import ca.sciencestudio.model.sample.SampleState;
import ca.sciencestudio.model.sample.dao.SampleDAO;

/**
 * @author maxweld
 *
 */
public class SampleFormBacker implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private int projectId;
	
	private String name;
	private String description;
	
	private SampleState state;
	private String quantity;
	private String casNumber;
	private String otherHazards;
	
	private boolean corrosive;
	private boolean flammable;
	private boolean reactive;
	private boolean oxidizer;
	private boolean toxic;
	private boolean other;
	
	public SampleFormBacker(int projectId) {
		setId(0);
		setProjectId(projectId);
		
		setName("");
		setDescription("");
		
		setState(SampleState.UNKNOWN);
		setQuantity("");
		setCasNumber("");
		setOtherHazards("");
		
		clearHazards();
	}
	
	public SampleFormBacker(Sample sample) {
		setId(sample.getId());
		setProjectId(sample.getProjectId());
		
		setName(sample.getName());
		setDescription(sample.getDescription());
		
		setState(sample.getState());
		setQuantity(sample.getQuantity());
		setCasNumber(sample.getCasNumber());
		setOtherHazards(sample.getOtherHazards());
		
		Set<SampleHazard> hazards = sample.getHazards();
		setCorrosive(hazards.contains(SampleHazard.CORROSIVE));
		setFlammable(hazards.contains(SampleHazard.FLAMMABLE));
		setReactive(hazards.contains(SampleHazard.REACTIVE));
		setOxidizer(hazards.contains(SampleHazard.OXIDIZER));
		setToxic(hazards.contains(SampleHazard.TOXIC));
		setOther(hazards.contains(SampleHazard.OTHER));
	}

	public Sample createSample(SampleDAO sampleDAO) {
		Sample sample = sampleDAO.createSample();
		sample.setId(getId());
		sample.setProjectId(getProjectId());
		
		sample.setName(getName());
		sample.setDescription(getDescription());
		
		sample.setState(getState());
		sample.setQuantity(getQuantity());
		sample.setCasNumber(getCasNumber());
		sample.setOtherHazards(getOtherHazards());
		
		Set<SampleHazard> hazards = new HashSet<SampleHazard>();
		if(isCorrosive()) { hazards.add(SampleHazard.CORROSIVE); }
		if(isFlammable()) { hazards.add(SampleHazard.FLAMMABLE); }
		if(isReactive()) { hazards.add(SampleHazard.REACTIVE); }
		if(isOxidizer()) { hazards.add(SampleHazard.OXIDIZER); }
		if(isToxic()) { hazards.add(SampleHazard.TOXIC); }
		if(isOther()) { hazards.add(SampleHazard.OTHER); }
		sample.setHazards(hazards);
		
		return sample;
	}
	
	public void clearHazards() {
		setCorrosive(false);
		setFlammable(false);
		setReactive(false);
		setOxidizer(false);
		setToxic(false);
		setOther(false);
	}
	
	public int getId() {
		return id;
	}
	private void setId(int id) {
		this.id = id;
	}

	public int getProjectId() {
		return projectId;
	}
	private void setProjectId(int projectId) {
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

	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getCasNumber() {
		return casNumber;
	}
	public void setCasNumber(String casNumber) {
		this.casNumber = casNumber;
	}

	public SampleState getState() {
		return state;
	}
	public void setState(SampleState state) {
		this.state = state;
	}

	public String getOtherHazards() {
		return otherHazards;
	}
	public void setOtherHazards(String otherHazards) {
		this.otherHazards = otherHazards;
	}

	public boolean isCorrosive() {
		return corrosive;
	}
	public void setCorrosive(boolean corrosive) {
		this.corrosive = corrosive;
	}

	public boolean isReactive() {
		return reactive;
	}
	public void setReactive(boolean reactive) {
		this.reactive = reactive;
	}

	public boolean isFlammable() {
		return flammable;
	}
	public void setFlammable(boolean flammable) {
		this.flammable = flammable;
	}

	public boolean isOxidizer() {
		return oxidizer;
	}
	public void setOxidizer(boolean oxidizer) {
		this.oxidizer = oxidizer;
	}

	public boolean isToxic() {
		return toxic;
	}
	public void setToxic(boolean toxic) {
		this.toxic = toxic;
	}

	public boolean isOther() {
		return other;
	}
	public void setOther(boolean other) {
		this.other = other;
	}
}
