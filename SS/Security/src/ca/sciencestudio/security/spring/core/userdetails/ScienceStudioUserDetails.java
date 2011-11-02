/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ScienceStudioUserDetails abstract class.
 *     
 */
package ca.sciencestudio.security.spring.core.userdetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author maxweldd
 *
 */
public abstract class ScienceStudioUserDetails implements UserDetails, CredentialsContainer, Serializable {

	private static final long serialVersionUID = 1L;
	
	protected String username;
	protected String password;
	protected String personGid;
	protected String authenticator;
	protected boolean enabled;
	protected boolean accountNonLocked;
	protected boolean accountNonExpired;
	protected boolean credentialsNonExpired;
	protected Collection<GrantedAuthority> authorities;

	public ScienceStudioUserDetails(ScienceStudioUserDetails userDetails) {
		this.username = userDetails.getUsername();
		this.password = userDetails.getPassword();
		this.personGid = userDetails.getPersonGid();
		this.authenticator = userDetails.getAuthenticator();
		this.enabled = userDetails.isEnabled();
		this.accountNonLocked = userDetails.isAccountNonLocked();
		this.accountNonExpired = userDetails.isAccountNonExpired();
		this.credentialsNonExpired = userDetails.isCredentialsNonExpired();
		this.authorities = new HashSet<GrantedAuthority>(userDetails.getAuthorities());
	}
	
	public ScienceStudioUserDetails(String username, String password, String personGid, String authenticator, Collection<GrantedAuthority> authorities) {
		this.username = username; 
		this.password = password;
		this.personGid = personGid;
		this.authenticator = authenticator;
		this.enabled = true;
		this.accountNonLocked = true;
		this.accountNonExpired = true;
		this.credentialsNonExpired = true;
		this.authorities = new HashSet<GrantedAuthority>(authorities);
	}

	@Override
	public void eraseCredentials() {
		password = "ERASED_CREDENTIALS";
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}
	
	public String getPersonGid() {
		return personGid;
	}

	public String getAuthenticator() {
		return authenticator;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}
	
	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}
}
