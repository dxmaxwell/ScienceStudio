/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     CasScienceStudioUserDetails class.
 *     
 */
package ca.sciencestudio.security.spring.cas.userdetails;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;

import ca.sciencestudio.security.spring.core.userdetails.ScienceStudioUserDetails;

/**
 * @author maxweld
 *
 */
public class CasScienceStudioUserDetails extends ScienceStudioUserDetails {

	private static final long serialVersionUID = 1L;
	
	private Date validFromDate;
	private Date validUntilDate;
	
	public CasScienceStudioUserDetails(CasScienceStudioUserDetails userDetails) {
		super(userDetails);
		validFromDate = userDetails.getValidFromDate();
		validUntilDate = userDetails.getValidUntilDate();
	}

	public CasScienceStudioUserDetails(String username, String personGid, String authenticator, 
				Date validFromDate, Date validUntilDate, Collection<GrantedAuthority> authorities) {
		super(username, "CAS_PASSWORD", personGid, authenticator, authorities);
		this.validFromDate = validFromDate;
		this.validUntilDate = validUntilDate;
	}

	public Date getValidFromDate() {
		return validFromDate;
	}

	public Date getValidUntilDate() {
		return validUntilDate;
	}
}
