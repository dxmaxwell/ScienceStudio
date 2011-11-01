/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     LaboratoryBasicDAO interface.
 *     
 */
package ca.sciencestudio.model.facility.dao;

import ca.sciencestudio.model.dao.ModelBasicDAO;
import ca.sciencestudio.model.facility.Laboratory;

/**
 * @author maxweld
 *
 */
public interface LaboratoryBasicDAO extends ModelBasicDAO<Laboratory> {
	
	//public Laboratory getLaboratoryByNameAndFacilityId(String laboratoryName, int facilityId);
	//public List<Laboratory> getLaboratoryListByName(String laboratoryName);
	//public List<Laboratory> getLaboratoryListByFacilityId(int facilityId);
	//public List<Laboratory> getLaboratoryListByNameAndFacilityId(String laboratoryName, int facilityId);
}
