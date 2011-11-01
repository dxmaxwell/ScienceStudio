/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     FacilityAuthorityController class.
 *     
 */
package ca.sciencestudio.rest.service.authz.controllers;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.facility.Facility;
import ca.sciencestudio.model.facility.dao.FacilityBasicDAO;
import ca.sciencestudio.security.authz.accessors.FacilityAuthorityAccessor;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 * 
 *
 */
@Controller
public class FacilityAuthorityController extends AbstractAuthorityController {
	
	private static final String FACILITY_AUTHZ_PATH = "/facilities";
	
	private FacilityBasicDAO facilityBasicDAO;
	
	private FacilityAuthorityAccessor facilityAuthorityAccessor;
	
	@ResponseBody
	@RequestMapping(value = FACILITY_AUTHZ_PATH + "/{gid}*", method = RequestMethod.GET)
	public Object getAuthorities(@RequestParam String user, @PathVariable String gid, HttpServletResponse response) {
		
		if(!facilityBasicDAO.getGidFacility().equalsIgnoreCase(gid)) {
			try {
				Facility facility = facilityBasicDAO.get(gid);
				if((facility == null) || !facilityBasicDAO.getGidFacility().equalsIgnoreCase(facility.getName())) {
					response.setStatus(HttpStatus.NOT_FOUND.value());
					return EMPTY_AUTHORITIES;
				}
			}
			catch(ModelAccessException e) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return EMPTY_AUTHORITIES;
			}
		}
		
		try {
			return facilityAuthorityAccessor.getAuthorities(user);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return EMPTY_AUTHORITIES;
		}
	}

	public FacilityBasicDAO getFacilityBasicDAO() {
		return facilityBasicDAO;
	}
	public void setFacilityBasicDAO(FacilityBasicDAO facilityBasicDAO) {
		this.facilityBasicDAO = facilityBasicDAO;
	}

	public FacilityAuthorityAccessor getFacilityAuthorityAccessor() {
		return facilityAuthorityAccessor;
	}
	public void setFacilityAuthorityAccessor(FacilityAuthorityAccessor facilityAuthorityAccessor) {
		this.facilityAuthorityAccessor = facilityAuthorityAccessor;
	}
}
