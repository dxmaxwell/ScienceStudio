/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     FacilityAuthzDAO interface.
 *     
 */
package ca.sciencestudio.model.facility.dao;

import java.util.List;

import ca.sciencestudio.model.facility.Facility;
import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.ModelAuthzDAO;

/**
 * @author maxweld
 * 
 */
public interface FacilityAuthzDAO extends ModelAuthzDAO<Facility> {
	
	public Data<Facility> get(String gid);
	
	public Data<List<Facility>> getAll();
}
