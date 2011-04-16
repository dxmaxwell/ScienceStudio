/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     InstrumentDAO class.
 *     
 */
package ca.sciencestudio.model.facility.dao;

import java.util.List;

import ca.sciencestudio.model.facility.Instrument;


/**
 * @author maxweld
 */
public interface InstrumentDAO {
	public Instrument createInstrument();
	
	public int addInstrument(Instrument instrument);
	public void editInstrument(Instrument instrument);
	public void removeInstrument(int id);
	
	public Instrument getInstrumentById(int id);
	public Instrument getInstrumentByNameAndLaboratoryId(String instrumentName, int laboratoryId);
	public List<Instrument> getInstrumentListByName(String name);
	public List<Instrument> getInstrumentListByLaboratoryId(int laboratoryId);
	public List<Instrument> getInstrumentListByNameAndLaboratoryId(String instrumentName, int laboratoryId);
}
