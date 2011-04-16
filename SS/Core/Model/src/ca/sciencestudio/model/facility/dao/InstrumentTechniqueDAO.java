/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     InstrumentTechniqueDAO class.
 *     
 */
package ca.sciencestudio.model.facility.dao;

import java.util.List;

import ca.sciencestudio.model.facility.InstrumentTechnique;

/**
 * @author maxweld
 */
public interface InstrumentTechniqueDAO {
	public InstrumentTechnique createInstrumentTechnique();
	
	public int addInstrumentTechnique(InstrumentTechnique instrumentTechnique);
	public void editInstrumentTechnique(InstrumentTechnique instrumentTechnique);
	public void removeInstrumentTechnique(int id);
	
	public InstrumentTechnique getInstrumentTechniqueById(int id);
	public InstrumentTechnique getInstrumentTechniqueByInstrumentIdAndTechniqueId(int instrumentId, int techniqueId);
	
	public List<InstrumentTechnique> getInstrumentTechniqueListByInstrumentId(int id);
	public List<InstrumentTechnique> getInstrumentTechniqueListByTechniqueId(int id);
	public List<InstrumentTechnique> getInstrumentTechniqueListByLaboratoryId(int id);
}
