/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     LdapScienceStudioUserDetailsService class.
 *     
 */
package ca.sciencestudio.security.spring.ldap.userdetails;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.model.person.dao.PersonAuthzDAO;
import ca.sciencestudio.security.spring.core.userdetails.AccountNotFoundException;
import ca.sciencestudio.security.spring.ldap.authentication.LdapContextAuthenticationToken;

/**
 * @author maxweld
 *
 */
public class LdapScienceStudioUserDetailsService implements AuthenticationUserDetailsService {
	
	private PersonAuthzDAO personAuthzDAO;
	
	private Collection<GrantedAuthority> defaultAuthorities = Collections.emptySet();
	
	protected Log logger = LogFactory.getLog(getClass());
	
	@Override
	public UserDetails loadUserDetails(Authentication token) throws UsernameNotFoundException {
		
		if(!(token instanceof LdapContextAuthenticationToken)) {
			UsernameNotFoundException e = new UsernameNotFoundException("Token does not contain an LDAP context. Expecting LdapContextAuthenticationToken.");
			logger.warn(e.getMessage());
			throw e;
		}
		
		String username = token.getName();
		
		String authenticator = ((LdapContextAuthenticationToken)token).getAuthenticator(); 
		
		DirContextOperations ldapContext = ((LdapContextAuthenticationToken)token).getLdapContext();
		
		Person person = personAuthzDAO.getByUsername(username, authenticator).get();
		if(person == null) {
			throw new AccountNotFoundException("Account with username '" + username + "@" + authenticator + "' not found.");
		}
		
		String personGid = person.getGid();
		String domainName = ldapContext.getDn().toString();
		Set<GrantedAuthority> authorities =  new HashSet<GrantedAuthority>(defaultAuthorities);
		
		if(logger.isDebugEnabled()) {
			logger.debug("Granted authorities for: " + personGid + ", are: " + authorities);
		}
		
		return new LdapScienceStudioUserDetails(username, domainName, personGid, authenticator, authorities);
	}

	public PersonAuthzDAO getPersonAuthzDAO() {
		return personAuthzDAO;
	}
	public void setPersonAuthzDAO(PersonAuthzDAO personAuthzDAO) {
		this.personAuthzDAO = personAuthzDAO;
	}

	public Collection<GrantedAuthority> getDefaultAuthorities() {
		return defaultAuthorities;
	}
	public void setDefaultAuthorities(Collection<GrantedAuthority> defaultAuthorities) {
		this.defaultAuthorities = defaultAuthorities;
	}
}
