/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     InstrumentAuthzDAO class.
 *     
 */
package ca.sciencestudio.model.facility.dao;

import ca.sciencestudio.model.dao.ModelAuthzDAO;
import ca.sciencestudio.model.facility.Instrument;

/**
 * @author maxweld
 */
public interface InstrumentAuthzDAO extends ModelAuthzDAO<Instrument> {

	//public Instrument getByNameAndLaboratoryId(String name, int laboratoryId);
	//public List<Instrument> getAllByName(String name);
	//public List<Instrument> getAllByLaboratoryId(int laboratoryId);
	//public List<Instrument> getAllByNameAndLaboratoryId(String name, int laboratoryId);
}
