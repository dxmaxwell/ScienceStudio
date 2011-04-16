/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     InstrumentTechniqueOption class.
 *     
 */
package ca.sciencestudio.service.session.backers;

import ca.sciencestudio.model.facility.Instrument;
import ca.sciencestudio.model.facility.InstrumentTechnique;
import ca.sciencestudio.model.facility.Technique;

/**
 * @author maxweld
 *
 */
public class InstrumentTechniqueOption {

	private int id;
	private int instrumentId;
	private int techniqueId;
	private String instrumentName;
	private String instrumentLongName;
	private String techniqueName;
	private String techniqueLongName;
	
	public InstrumentTechniqueOption() {
		setId(0);
		setInstrumentId(0);
		setTechniqueId(0);
		setInstrumentName("");
		setInstrumentLongName("");
		setTechniqueName("");
		setTechniqueLongName("");
	}
	
	public InstrumentTechniqueOption(InstrumentTechnique insttech, Instrument inst, Technique  tech) {
		if(insttech.getInstrumentId() != inst.getId()) {
			throw new IllegalArgumentException();
		}
		
		if(insttech.getTechniqueId() != tech.getId()) {
			throw new IllegalArgumentException();
		}
			
		setId(insttech.getId());
		setInstrumentId(inst.getId());
		setTechniqueId(tech.getId());
		setInstrumentName(inst.getName());
		setInstrumentLongName(inst.getLongName());
		setTechniqueName(tech.getName());
		setTechniqueLongName(tech.getLongName());
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getInstrumentId() {
		return instrumentId;
	}
	public void setInstrumentId(int instrumentId) {
		this.instrumentId = instrumentId;
	}
	
	public int getTechniqueId() {
		return techniqueId;
	}
	public void setTechniqueId(int techniqueId) {
		this.techniqueId = techniqueId;
	}
	
	public String getInstrumentName() {
		return instrumentName;
	}
	public void setInstrumentName(String instrumentName) {
		this.instrumentName = instrumentName;
	}
	
	public String getInstrumentLongName() {
		return instrumentLongName;
	}
	public void setInstrumentLongName(String instrumentLongName) {
		this.instrumentLongName = instrumentLongName;
	}
	
	public String getTechniqueName() {
		return techniqueName;
	}
	public void setTechniqueName(String techniqueName) {
		this.techniqueName = techniqueName;
	}
	
	public String getTechniqueLongName() {
		return techniqueLongName;
	}
	public void setTechniqueLongName(String techniqueLongName) {
		this.techniqueLongName = techniqueLongName;
	}
}
