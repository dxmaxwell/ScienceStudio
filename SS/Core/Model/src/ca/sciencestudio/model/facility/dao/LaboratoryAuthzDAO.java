/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     LaboratoryAuthzDAO interface.
 *     
 */
package ca.sciencestudio.model.facility.dao;

import java.util.List;

import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.ModelAuthzDAO;
import ca.sciencestudio.model.facility.Laboratory;

/**
 * @author maxweld
 *
 */
public interface LaboratoryAuthzDAO extends ModelAuthzDAO<Laboratory> {
	
	public Data<Laboratory> get(String gid);
	
	public Data<List<Laboratory>> getAll(); 
	public Data<List<Laboratory>> getAllByFacilityGid(String facilityGid);
}
