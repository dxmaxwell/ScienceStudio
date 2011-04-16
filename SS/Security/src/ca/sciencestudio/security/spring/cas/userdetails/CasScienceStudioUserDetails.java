/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     CasScienceStudioUserDetails class.
 *     
 */
package ca.sciencestudio.security.spring.cas.userdetails;

import java.util.Date;

import ca.sciencestudio.security.spring.core.userdetails.ScienceStudioUserDetails;

/**
 * @author maxweld
 *
 */
public class CasScienceStudioUserDetails extends ScienceStudioUserDetails {

	private static final long serialVersionUID = 1L;
	
	private Date validFromDate;
	private Date validUntilDate;
	
	public CasScienceStudioUserDetails(Date validFromDate, Date validUntilDate, ScienceStudioUserDetails userDetails) {
		super(userDetails);
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
