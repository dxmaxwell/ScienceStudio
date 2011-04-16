/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ScienceStudioUserDetails class.
 *     
 */
package ca.sciencestudio.security.spring.core.userdetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import ca.sciencestudio.login.model.Account;
import ca.sciencestudio.model.person.Person;

/**
 * @author maxweld
 *
 */
public class ScienceStudioUserDetails implements UserDetails, CredentialsContainer, Serializable {

	private static final long serialVersionUID = 1L;
	
	private Person person;

	private String username;
	private String password;
	private boolean enabled;
	private boolean accountNonLocked;
	private boolean accountNonExpired;
	private boolean credentialsNonExpired;
	private Collection<GrantedAuthority> authorities; 

	public ScienceStudioUserDetails(ScienceStudioUserDetails userDetails) {
		this.person = userDetails.getPerson();
		this.username = userDetails.getUsername();
		this.password = userDetails.getPassword();
		this.enabled = userDetails.isEnabled();
		this.accountNonLocked = userDetails.isAccountNonLocked();
		this.accountNonExpired = userDetails.isAccountNonExpired();
		this.credentialsNonExpired = userDetails.isCredentialsNonExpired();
		this.authorities = new HashSet<GrantedAuthority>(userDetails.getAuthorities());
	}
	
	public ScienceStudioUserDetails(Account account, Person person, Collection<GrantedAuthority> authorities) {
		username = account.getUsername();
		password = account.getPassword();
		
		switch(account.getStatus()) {
			case ACTIVE:
				enabled = true;
				accountNonLocked = true;
				accountNonExpired = true;
				credentialsNonExpired = true;
				break;
				
			case UNKNOWN:
			case DISABLED:
			
				enabled = false;
				accountNonLocked = true;
				accountNonExpired = true;
				credentialsNonExpired = true;
				break;
				
			case EXPIRED:
				enabled = false;
				accountNonLocked = true;
				accountNonExpired = true;
				credentialsNonExpired = false;
				break;
		}
		
		if(person == null) {
			throw new NullPointerException();
		}
		this.person = person;
		
		this.authorities = new HashSet<GrantedAuthority>(authorities);
	}

	@Override
	public void eraseCredentials() {
		password = "";
	}
	
	public Person getPerson() {
		return person;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
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
