/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     LaboratoryBasicDAO interface.
 *     
 */
package ca.sciencestudio.model.facility.dao;

import java.util.List;

import ca.sciencestudio.model.dao.ModelBasicDAO;
import ca.sciencestudio.model.facility.Laboratory;

/**
 * @author maxweld
 *
 */
public interface LaboratoryBasicDAO extends ModelBasicDAO<Laboratory> {
	
	public List<Laboratory> getAllByFacilityGid(String facilityGid);
}
