/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     FacilityDAO interface.
 *     
 */
package ca.sciencestudio.model.facility.dao;

import java.util.List;

import ca.sciencestudio.model.facility.Facility;

/**
 * @author maxweld
 * 
 */
public interface FacilityDAO {
	public Facility createFacility();
	
	public int addFacility(Facility facility);
	public void editFacility(Facility facility);
	public void removeFacility(int facilityId);
	
	public Facility getFacilityById(int facilityId);
	public Facility getFacilityByName(String name);
	
	public List<Facility> getFacilityList();
	public List<Facility> getFacilityListByName(String name);
}
