/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     LdapDelegatingScienceStudioUserDetailsService class.
 *     
 */
package ca.sciencestudio.security.spring.ldap.userdetails;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;

import ca.sciencestudio.security.spring.core.userdetails.ScienceStudioUserDetails;
import ca.sciencestudio.security.spring.core.userdetails.ScienceStudioUserDetailsService;
import ca.sciencestudio.security.spring.ldap.authentication.LdapContextAuthenticationToken;

/**
 * @author maxweld
 *
 */
public class LdapDelegatingScienceStudioUserDetailsService implements AuthenticationUserDetailsService {

	private ScienceStudioUserDetailsService scienceStudioUserDetailsService;
	
	protected Log logger = LogFactory.getLog(getClass());
	
	@Override
	public UserDetails loadUserDetails(Authentication token) throws UsernameNotFoundException {
			
		DirContextOperations ldapContext;
		if(token instanceof LdapContextAuthenticationToken) {
			ldapContext = ((LdapContextAuthenticationToken)token).getLdapContext();
		}
		else {
			UsernameNotFoundException e = new UsernameNotFoundException("Token does not contain an LDAP context. Expecting LdapContextAuthenticationToken.");
			logger.warn(e.getMessage());
			throw e;
		}
		
		ScienceStudioUserDetails userDetails = loadScienceStudioUserDetails(token);
		return new LdapScienceStudioUserDetails(ldapContext.getDn().toString(), userDetails);
	}
	

	protected ScienceStudioUserDetails loadScienceStudioUserDetails(Authentication token) {
		return (ScienceStudioUserDetails) scienceStudioUserDetailsService.loadUserDetails(token);
	}
	
	public ScienceStudioUserDetailsService getScienceStudioUserDetailsService() {
		return scienceStudioUserDetailsService;
	}
	public void setScienceStudioUserDetailsService(ScienceStudioUserDetailsService scienceStudioUserDetailsService) {
		this.scienceStudioUserDetailsService = scienceStudioUserDetailsService;
	}
}
