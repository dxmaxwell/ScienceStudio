/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SampleFormBacker class.
 *     
 */
package ca.sciencestudio.service.sample.backers;

import java.util.Set;

import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.util.rest.ValidationResult;

/**
 * @author maxweld
 *
 */
public class SampleFormBacker extends Sample {

	private static final long serialVersionUID = 1L;
	
	private boolean corrosive;
	private boolean flammable;
	private boolean reactive;
	private boolean oxidizer;
	private boolean toxic;
	private boolean other;
	
	public static ValidationResult transformResult(ValidationResult result) {
		return result;
	}
	
	public SampleFormBacker() {
		setCorrosive(false);
		setFlammable(false);
		setReactive(false);
		setOxidizer(false);
		setToxic(false);
		setOther(false);
	}
	
	public SampleFormBacker(Sample sample) {
		super(sample);
		Set<Hazard> hazards = getHazards();
		setCorrosive(hazards.contains(Hazard.CORROSIVE));
		setFlammable(hazards.contains(Hazard.FLAMMABLE));
		setReactive(hazards.contains(Hazard.REACTIVE));
		setOxidizer(hazards.contains(Hazard.OXIDIZER));
		setToxic(hazards.contains(Hazard.TOXIC));
		setOther(hazards.contains(Hazard.OTHER));
	}
	
	public void clearHazards() {
		setCorrosive(false);
		setFlammable(false);
		setReactive(false);
		setOxidizer(false);
		setToxic(false);
		setOther(false);
	}

	public boolean isCorrosive() {
		return corrosive;
	}
	public void setCorrosive(boolean corrosive) {
		if(corrosive) {
			getHazards().add(Hazard.CORROSIVE);
		} else {
			getHazards().remove(Hazard.CORROSIVE);
		}
		this.corrosive = corrosive;
	}

	public boolean isReactive() {
		return reactive;
	}
	public void setReactive(boolean reactive) {
		if(reactive) {
			getHazards().add(Hazard.REACTIVE);
		} else {
			getHazards().remove(Hazard.REACTIVE);
		}
		this.reactive = reactive;
	}

	public boolean isFlammable() {
		return flammable;
	}
	public void setFlammable(boolean flammable) {
		if(flammable) {
			getHazards().add(Hazard.FLAMMABLE);
		} else {
			getHazards().remove(Hazard.FLAMMABLE);
		}
		this.flammable = flammable;
	}

	public boolean isOxidizer() {
		return oxidizer;
	}
	public void setOxidizer(boolean oxidizer) {
		if(oxidizer) {
			getHazards().add(Hazard.OXIDIZER);
		} else {
			getHazards().remove(Hazard.OXIDIZER);
		}
		this.oxidizer = oxidizer;
	}

	public boolean isToxic() {
		return toxic;
	}
	public void setToxic(boolean toxic) {
		if(toxic) {
			getHazards().add(Hazard.TOXIC);
		} else {
			getHazards().remove(Hazard.TOXIC);
		}
		this.toxic = toxic;
	}

	public boolean isOther() {
		return other;
	}
	public void setOther(boolean other) {
		if(other) {
			getHazards().add(Hazard.OTHER);
		} else {
			getHazards().remove(Hazard.OTHER);
		}
		this.other = other;
	}
}
