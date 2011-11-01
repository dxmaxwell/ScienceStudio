/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     FacilityAuthzDAO interface.
 *     
 */
package ca.sciencestudio.model.facility.dao.rest;

import ca.sciencestudio.model.dao.rest.AbstractRestModelAuthzDAO;
import ca.sciencestudio.model.facility.Facility;
import ca.sciencestudio.model.facility.dao.FacilityAuthzDAO;
import ca.sciencestudio.model.facility.dao.rest.support.RestFacility;

/**
 * @author maxweld
 *
 */
public class RestFacilityAuthzDAO extends AbstractRestModelAuthzDAO<Facility> implements FacilityAuthzDAO {

	public static final String FACILITY_MODEL_PATH = "/facilities";
	
	@Override
	protected Object toRestModel(Facility facility) {
		RestFacility restFacility = new RestFacility();
		restFacility.setName(facility.getName());
		restFacility.setLongName(facility.getLongName());
		restFacility.setDescription(facility.getDescription());
		restFacility.setPhoneNumber(facility.getPhoneNumber());
		restFacility.setEmailAddress(facility.getEmailAddress());
		restFacility.setLocation(facility.getLocation());
		restFacility.setAuthcUrl(facility.getAuthcUrl());
		restFacility.setHomeUrl(facility.getHomeUrl());
		return restFacility;
	}

	@Override
	protected String getModelPath() {
		return FACILITY_MODEL_PATH;
	}

	@Override
	protected Class<Facility> getModelClass() {
		return Facility.class;
	}

	@Override
	protected Class<Facility[]> getModelArrayClass() {
		return Facility[].class;
	}
}
