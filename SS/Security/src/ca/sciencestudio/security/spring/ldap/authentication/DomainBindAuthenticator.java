/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     	 DomainBindAuthenticator class.
 *     
 */
package ca.sciencestudio.security.spring.ldap.authentication;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.ldap.authentication.BindAuthenticator;

/**
 * @author maxweld
 *
 */
public class DomainBindAuthenticator extends BindAuthenticator {

	private boolean bindWithDomain = false;
	
	public DomainBindAuthenticator(BaseLdapPathContextSource contextSource) {
		super(contextSource);
	}

	@Override
	public DirContextOperations authenticate(Authentication auth) {
		if(!(auth instanceof DomainUsernamePasswordAuthenticationToken)) {
			return super.authenticate(auth);
		}
		
		DomainUsernamePasswordAuthenticationToken domainAuth = 
					(DomainUsernamePasswordAuthenticationToken) auth;
		
		boolean useNameWithDomain = domainAuth.isUseNameWithDomain();
		domainAuth.setUseNameWithDomain(bindWithDomain);
		DirContextOperations ctx = super.authenticate(domainAuth);
		domainAuth.setUseNameWithDomain(useNameWithDomain);
		return ctx;	
	}

	public boolean isBindWithDomain() {
		return bindWithDomain;
	}
	public void setBindWithDomain(boolean bindWithDomain) {
		this.bindWithDomain = bindWithDomain;
	}
}
