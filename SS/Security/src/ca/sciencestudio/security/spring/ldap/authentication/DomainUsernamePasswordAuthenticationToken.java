/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     	 DomainUsernamePasswordAuthenticationToken class.
 *     
 */
package ca.sciencestudio.security.spring.ldap.authentication;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author maxweld
 *
 */
public class DomainUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

	public static final String USERNAME_DOMAIN_SEPERATOR = "@";
	
	private static final long serialVersionUID = 1L;

	private boolean useNameWithDomain = true;
	
	public DomainUsernamePasswordAuthenticationToken(Object details, Object principal, Object credentials) {
		super(principal, credentials);
		setDetails(details);
	}
	
	public DomainUsernamePasswordAuthenticationToken(Object details, Object principal, Object credentials,
														Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
		setDetails(details);
	}
	
	@Override
	public String getName() {
		if(useNameWithDomain) {
			return getNameWithDomain();
		} else {
			return getNameWithoutDomain();
		}
	} 
	
	public String getNameWithDomain() {
		String domain = getDomain();
		if((domain == null) || (domain.length() == 0)) {
			return super.getName();
		} else {
			return super.getName() + USERNAME_DOMAIN_SEPERATOR + domain;
		}
	}
	
	public String getNameWithoutDomain() {
		return super.getName();
	}
	
	public String getDomain() {
		Object details = getDetails();
		if(details == null) {
			return null;
		}
		
		return details.toString();
	}

	public boolean isUseNameWithDomain() {
		return useNameWithDomain;
	}
	public void setUseNameWithDomain(boolean useNameWithDomain) {
		this.useNameWithDomain = useNameWithDomain;
	}
}
