/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     LaboratoryDAO interface.
 *     
 */
package ca.sciencestudio.model.facility.dao;

import java.util.List;

import ca.sciencestudio.model.facility.Laboratory;


/**
 * @author maxweld
 *
 */
public interface LaboratoryDAO {
	public Laboratory createLaboratory();
	
	public int addLaboratory(Laboratory laboratory);
	public void editLaboratory(Laboratory laboratory);
	public void removeLaboratory(int laboratoryId);
	
	public Laboratory getLaboratoryById(int laboratoryId);
	public Laboratory getLaboratoryByNameAndFacilityId(String laboratoryName, int facilityId);
	public List<Laboratory> getLaboratoryList();
	public List<Laboratory> getLaboratoryListByName(String laboratoryName);
	public List<Laboratory> getLaboratoryListByFacilityId(int facilityId);
	public List<Laboratory> getLaboratoryListByNameAndFacilityId(String laboratoryName, int facilityId);
}
