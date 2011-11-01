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
public class InstrumentTechniqueOption extends InstrumentTechnique {

	private static final long serialVersionUID = 1L;
	
	private String instrumentName;
	private String instrumentLongName;
	private String techniqueName;
	private String techniqueLongName;
	
	public InstrumentTechniqueOption() {
		instrumentName = Instrument.DEFAULT_NAME;
		instrumentLongName = Instrument.DEFAULT_LONG_NAME;
		techniqueName = Technique.DEFAULT_NAME;
		techniqueLongName = Technique.DEFAULT_LONG_NAME;
	}
	
	public InstrumentTechniqueOption(InstrumentTechnique instrumentTechnique, Instrument instrument, Technique technique) {
		super(instrumentTechnique);
		
		if(!getInstrumentGid().equals(instrument.getGid())) {
			throw new IllegalArgumentException();
		}
		
		if(!getTechniqueGid().equals(technique.getGid())) {
			throw new IllegalArgumentException();
		}
		
		setInstrumentName(instrument.getName());
		setInstrumentLongName(instrument.getLongName());
		setTechniqueName(technique.getName());
		setTechniqueLongName(technique.getLongName());
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
