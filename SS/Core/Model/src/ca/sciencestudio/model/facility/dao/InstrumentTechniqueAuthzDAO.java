/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     InstrumentTechniqueAuthzDAO class.
 *     
 */
package ca.sciencestudio.model.facility.dao;

import java.util.List;

import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.ModelAuthzDAO;
import ca.sciencestudio.model.facility.InstrumentTechnique;

/**
 * @author maxweld
 */
public interface InstrumentTechniqueAuthzDAO extends ModelAuthzDAO<InstrumentTechnique> {
	
	public Data<List<InstrumentTechnique>> getAll();
	public Data<List<InstrumentTechnique>> getAllByLaboratoryGid(String laboratoryGid);
}
