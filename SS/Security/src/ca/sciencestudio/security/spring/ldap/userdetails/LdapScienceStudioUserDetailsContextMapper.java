/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ScienceStudioUserDetails class.
 *     
 */
package ca.sciencestudio.security.spring.ldap.userdetails;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

import ca.sciencestudio.security.spring.ldap.authentication.LdapContextAuthenticationToken;


/**
 * @author maxweld
 *
 */
public class LdapScienceStudioUserDetailsContextMapper implements UserDetailsContextMapper {
	
	private String authenticator;
	
	private AuthenticationUserDetailsService ldapContextUserDetailsService;
	
	protected Log logger = LogFactory.getLog(getClass());
	
	@Override
	public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<GrantedAuthority> authorities) {
		return ldapContextUserDetailsService.loadUserDetails(new LdapContextAuthenticationToken(ctx, username, authenticator, authorities));
	}

	@Override
	public void mapUserToContext(UserDetails userDetails, DirContextAdapter ctx) {
		// Not supported. User details are not stored in LDAP server. //
	}

	public String getAuthenticator() {
		return authenticator;
	}
	public void setAuthenticator(String authenticator) {
		this.authenticator = authenticator;
	}

	public AuthenticationUserDetailsService getLdapContextUserDetailsService() {
		return ldapContextUserDetailsService;
	}
	public void setLdapContextUserDetailsService(AuthenticationUserDetailsService ldapContextUserDetailsService) {
		this.ldapContextUserDetailsService = ldapContextUserDetailsService;
	}
}
