/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     TechniqueAuthzDAO interface.
 *     
 */
package ca.sciencestudio.model.facility.dao;

import java.util.List;

import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.ModelAuthzDAO;
import ca.sciencestudio.model.facility.Technique;

/**
 * @author maxweld
 *
 */
public interface TechniqueAuthzDAO extends ModelAuthzDAO<Technique> {
	
	public Data<Technique> get(String gid);
	
	public Data<List<Technique>> getAll();
	public Data<List<Technique>> getAllByLaboratoryGid(String laboratoryGid);
	public Data<List<Technique>> getAllByInstrumentGid(String instrumentGid);
}
