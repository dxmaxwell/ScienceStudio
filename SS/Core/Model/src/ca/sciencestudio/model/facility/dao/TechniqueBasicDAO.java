/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     TechniqueBasicDAO interface.
 *     
 */
package ca.sciencestudio.model.facility.dao;

import ca.sciencestudio.model.dao.ModelBasicDAO;
import ca.sciencestudio.model.facility.Technique;

/**
 * @author maxweld
 *
 */
public interface TechniqueBasicDAO extends ModelBasicDAO<Technique> {
	
	//public Technique getTechniqueByNameAndInstrumentId(String techniqueName, int instrumentId);
	//
	//public List<Technique> getTechniqueListByName(String techniqueName);
	//public List<Technique> getTechniqueListByInstrumentId(int instrumentId);
	//public List<Technique> getTechniqueListByLaboratoryId(int laboratoryId);
	//public List<Technique> getTechniqueListByNameAndInstrumentId(String techniqueName, int instrumentId);
}
