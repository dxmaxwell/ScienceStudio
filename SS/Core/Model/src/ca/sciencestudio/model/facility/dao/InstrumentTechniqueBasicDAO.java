/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     InstrumentTechniqueBasicDAO class.
 *     
 */
package ca.sciencestudio.model.facility.dao;

import java.util.List;

import ca.sciencestudio.model.dao.ModelBasicDAO;
import ca.sciencestudio.model.facility.InstrumentTechnique;

/**
 * @author maxweld
 */
public interface InstrumentTechniqueBasicDAO extends ModelBasicDAO<InstrumentTechnique> {
	
	public List<InstrumentTechnique> getAllByLaboratoryGid(String laboratoryGid);
	//public InstrumentTechnique getInstrumentTechniqueByInstrumentIdAndTechniqueId(int instrumentId, int techniqueId);
	//public List<InstrumentTechnique> getInstrumentTechniqueListByInstrumentId(int id);
	//public List<InstrumentTechnique> getInstrumentTechniqueListByTechniqueId(int id);
}
