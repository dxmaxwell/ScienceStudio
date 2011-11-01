/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     InstrumentAuthzDAO class.
 *     
 */
package ca.sciencestudio.model.facility.dao;

import java.util.List;

import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.ModelAuthzDAO;
import ca.sciencestudio.model.facility.Instrument;

/**
 * @author maxweld
 */
public interface InstrumentAuthzDAO extends ModelAuthzDAO<Instrument> {

	public Data<List<Instrument>> getAll();
	public Data<List<Instrument>> getAllByLaboratoryGid(String laboratoryGid);
	//public Instrument getByNameAndLaboratoryId(String name, int laboratoryId);
	//public List<Instrument> getAllByName(String name);
	//public List<Instrument> getAllByNameAndLaboratoryId(String name, int laboratoryId);
}
