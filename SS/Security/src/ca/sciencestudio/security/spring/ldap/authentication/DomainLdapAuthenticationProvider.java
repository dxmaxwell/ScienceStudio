/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     	 DomainLdapAuthenticationProvider class.
 *  
 *  Use of LDAP for authentication has been depreciated in favour of CAS.
 *  
 */
package ca.sciencestudio.security.spring.ldap.authentication;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;

/**
 * @author maxweld
 *
 */
public class DomainLdapAuthenticationProvider extends LdapAuthenticationProvider {
	
	protected Log logger = LogFactory.getLog(getClass());
	
	public String domain = "";

	public DomainLdapAuthenticationProvider(String domain, LdapAuthenticator authenticator) {
		super(authenticator);
		this.domain = domain;
	}
	
	public DomainLdapAuthenticationProvider(String domain, LdapAuthenticator authenticator, LdapAuthoritiesPopulator authoritiesPopulator) {
		super(authenticator, authoritiesPopulator);
		this.domain = domain;
	}

	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		if(!domain.equals(((DomainUsernamePasswordAuthenticationToken)auth).getDomain())) {
			String unsupportedDomain = ((DomainUsernamePasswordAuthenticationToken)auth).getDomain();
			String msg = "Authentication provider for domain [" + domain + "], does not support [" + unsupportedDomain + "]";
			AuthenticationException e = new ProviderNotFoundException(msg);
			logger.debug(msg);
			throw e;
		}
		
		logger.debug("Authentication provider supports domain  [" + domain + "]");
		return super.authenticate(auth);
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return DomainUsernamePasswordAuthenticationToken.class.isAssignableFrom(clazz);
	}

	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
}
