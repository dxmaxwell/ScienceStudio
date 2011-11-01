/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     FacilityBasicDAO interface.
 *     
 */
package ca.sciencestudio.model.facility.dao;

import ca.sciencestudio.model.facility.Facility;
import ca.sciencestudio.model.dao.ModelBasicDAO;

/**
 * @author maxweld
 * 
 */
public interface FacilityBasicDAO extends ModelBasicDAO<Facility> {
	
	public Facility getByName(String name);
}
