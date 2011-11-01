/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     InstrumentBasicDAO class.
 *     
 */
package ca.sciencestudio.model.facility.dao;

import java.util.List;

import ca.sciencestudio.model.dao.ModelBasicDAO;
import ca.sciencestudio.model.facility.Instrument;

/**
 * @author maxweld
 */
public interface InstrumentBasicDAO extends ModelBasicDAO<Instrument> {

	public List<Instrument> getAllByLaboratoryGid(String laboratoryGid);
	//public Instrument getByNameAndLaboratoryId(String name, int laboratoryId);
	//public List<Instrument> getAllByName(String name);
	//public List<Instrument> getAllByNameAndLaboratoryId(String name, int laboratoryId);
}
