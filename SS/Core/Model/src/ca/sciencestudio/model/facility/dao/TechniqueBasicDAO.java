/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     TechniqueBasicDAO interface.
 *     
 */
package ca.sciencestudio.model.facility.dao;

import java.util.List;

import ca.sciencestudio.model.dao.ModelBasicDAO;
import ca.sciencestudio.model.facility.Technique;

/**
 * @author maxweld
 *
 */
public interface TechniqueBasicDAO extends ModelBasicDAO<Technique> {
	
	public List<Technique> getAllByLaboratoryGid(String laboratoryGid);
	public List<Technique> getAllByInstrumentGid(String instrumentGid);
}
