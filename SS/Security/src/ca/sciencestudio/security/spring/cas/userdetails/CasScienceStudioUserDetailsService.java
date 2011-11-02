/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     CasScienceStudioUserDetailsService class.
 *     
 */
package ca.sciencestudio.security.spring.cas.userdetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.security.cas.userdetails.AbstractCasAssertionUserDetailsService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.model.person.dao.PersonAuthzDAO;
import ca.sciencestudio.security.cas.client.validation.ScienceStudioDelegatingAssertion;
import ca.sciencestudio.security.spring.core.userdetails.PersonNotFoundException;

/**
 * @author maxweld
 * 
 *
 */
public class CasScienceStudioUserDetailsService extends AbstractCasAssertionUserDetailsService {

	private PersonAuthzDAO personAuthzDAO;
	
	private Collection<GrantedAuthority> defaultAuthorities = Collections.emptySet();
	
	protected Log logger = LogFactory.getLog(getClass());
	
	@Override
	protected UserDetails loadUserDetails(Assertion assertion) {
		
		if(!(assertion instanceof ScienceStudioDelegatingAssertion)) {
			PersonNotFoundException e = new PersonNotFoundException("Assertion is not ScienceStudioDelegatingAssertion: cannot get facility name.");
			logger.warn(e.getMessage());
			throw e;
		}
		
		String username = assertion.getPrincipal().getName();
		
		String authenticator = ((ScienceStudioDelegatingAssertion)assertion).getAuthenticator();
		
		Person person;
		try {
			person = personAuthzDAO.getByUsername(username, authenticator).get();
		} 
		catch(Exception e) {
			throw new PersonNotFoundException("Error while getting Person with username: " + username + ", and authc: " + authenticator, e);
		}
		
		if(person == null) {
			throw new PersonNotFoundException("Person not found for username: " + username + ", and authenticator: " + authenticator);
		}
		
		Date validFromDate = assertion.getValidFromDate();
		Date validUntilDate = assertion.getValidUntilDate();
		Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>(defaultAuthorities);
		
		if(logger.isDebugEnabled()) {
			logger.debug("Granted authorities for: " + person.getGid() + ", are: " + authorities);
		}
		
		return new CasScienceStudioUserDetails(username, person.getGid(), authenticator, validFromDate, validUntilDate, authorities);
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
