/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     LdapScienceStudioUserDetails class.
 *     
 */
package ca.sciencestudio.security.spring.ldap.userdetails;

import org.springframework.security.ldap.userdetails.LdapUserDetails;

import ca.sciencestudio.security.spring.core.userdetails.ScienceStudioUserDetails;

/**
 * @author maxweld
 *
 */
public class LdapScienceStudioUserDetails extends ScienceStudioUserDetails implements LdapUserDetails {

	private static final long serialVersionUID = 1L;
	
	private String dn;
	
	public LdapScienceStudioUserDetails(String dn, ScienceStudioUserDetails userDetails) {
		super(userDetails);
		this.dn = dn;
	}

	@Override
	public String getDn() {
		return dn;
	}
}
