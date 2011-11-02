/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     LdapScienceStudioUserDetails class.
 *     
 */
package ca.sciencestudio.security.spring.ldap.userdetails;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapUserDetails;

import ca.sciencestudio.security.spring.core.userdetails.ScienceStudioUserDetails;

/**
 * @author maxweld
 *
 */
public class LdapScienceStudioUserDetails extends ScienceStudioUserDetails implements LdapUserDetails {

	private static final long serialVersionUID = 1L;
	
	private String domainName;
	
	public LdapScienceStudioUserDetails(LdapScienceStudioUserDetails userDetails) {
		super(userDetails);
		this.domainName = userDetails.getDomainName();
	}

	public LdapScienceStudioUserDetails(String username, String domainName, String personGid, String authenticator, Collection<GrantedAuthority> authorities) {
		super(username, "LDAP_PASSWORD", personGid, authenticator, authorities);
		this.domainName = domainName;
	}

	public String getDomainName() {
		return domainName;
	}

	@Override
	public String getDn() {
		return domainName;
	}
}
