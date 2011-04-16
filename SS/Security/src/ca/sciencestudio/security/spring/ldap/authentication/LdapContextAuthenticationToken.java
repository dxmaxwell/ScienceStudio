/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     LdapContextAuthenticationToken class.
 *     
 */
package ca.sciencestudio.security.spring.ldap.authentication;

import java.util.Collection;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author maxweld
 *
 */
public class LdapContextAuthenticationToken implements Authentication {

	private static final long serialVersionUID = 1L;
	
	private String principal;
	private DirContextOperations details;
	private Collection<GrantedAuthority> authorities;
	
	public LdapContextAuthenticationToken(DirContextOperations ldapContext, String username, Collection<GrantedAuthority> authorities) {
		this.principal = username;
		this.details = ldapContext;
		this.authorities = authorities;
	}
	
	public DirContextOperations getLdapContext() {
		return details;
	}
	
	@Override
	public String getName() {
		return getPrincipal().toString();
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}
	
	@Override
	public Object getCredentials() {
		return "";
	}
	
	@Override
	public Object getDetails() {
		return details;
	}
	
	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}

	@Override
	public void setAuthenticated(boolean authc) throws IllegalArgumentException {
		if(!authc) {
			throw new IllegalArgumentException("LdapContextAuthenticationToken is always authenticated.");
		}
	}
}
