/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     CasDelegatingScienceStudioUserDetailsService class.
 *     
 */
package ca.sciencestudio.security.spring.cas.userdetails;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.cas.authentication.CasAuthenticationToken;

import ca.sciencestudio.security.spring.core.userdetails.ScienceStudioUserDetails;
import ca.sciencestudio.security.spring.core.userdetails.ScienceStudioUserDetailsService;
import ca.sciencestudio.security.spring.ldap.authentication.DomainUsernamePasswordAuthenticationToken;

/**
 * @author maxweld
 *
 */
public class CasDelegatingScienceStudioUserDetailsService implements AuthenticationUserDetailsService {

	private String domain = "";
	private boolean useNameWithDomain = false;
	
	private ScienceStudioUserDetailsService scienceStudioUserDetailsService;
	private AuthenticationUserDetailsService casAsserionUserDetailsService;
	
	protected Log logger = LogFactory.getLog(getClass());
	
	@Override
	public UserDetails loadUserDetails(Authentication token) throws UsernameNotFoundException {
		
		Assertion assertion;
		if(token instanceof CasAuthenticationToken) {
			assertion = ((CasAuthenticationToken)token).getAssertion();
		}
		else if(token instanceof CasAssertionAuthenticationToken) {
			assertion = ((CasAssertionAuthenticationToken)token).getAssertion();
		}
		else {
			UsernameNotFoundException e = new UsernameNotFoundException("Token does not contain a JASIG assertion. Expecting CasAuthenticationToken or CasAssertionAuthenticationToken.");
			logger.warn(e.getMessage());
			throw e;
		}
		
		DomainUsernamePasswordAuthenticationToken domainToken = 
				new DomainUsernamePasswordAuthenticationToken(domain, token.getPrincipal(), token.getCredentials());
		
		domainToken.setUseNameWithDomain(useNameWithDomain);
		
		ScienceStudioUserDetails userDetails = loadScienceStudioUserDetails(domainToken);
		
		if(casAsserionUserDetailsService != null) {
			UserDetails casAssersionUserDetails = casAsserionUserDetailsService.loadUserDetails(token);
			userDetails.getAuthorities().addAll(casAssersionUserDetails.getAuthorities());
		}
		
		Date validFromDate = assertion.getValidFromDate();
		Date validUntilDate = assertion.getValidUntilDate();
		return new CasScienceStudioUserDetails(validFromDate, validUntilDate, userDetails);
	}
	
	protected ScienceStudioUserDetails loadScienceStudioUserDetails(Authentication token) {
		return (ScienceStudioUserDetails) scienceStudioUserDetailsService.loadUserDetails(token);
	}
	
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}

	public boolean isUseNameWithDomain() {
		return useNameWithDomain;
	}
	public void setUseNameWithDomain(boolean useNameWithDomain) {
		this.useNameWithDomain = useNameWithDomain;
	}

	public ScienceStudioUserDetailsService getScienceStudioUserDetailsService() {
		return scienceStudioUserDetailsService;
	}
	public void setScienceStudioUserDetailsService(ScienceStudioUserDetailsService scienceStudioUserDetailsService) {
		this.scienceStudioUserDetailsService = scienceStudioUserDetailsService;
	}

	public AuthenticationUserDetailsService getCasAsserionUserDetailsService() {
		return casAsserionUserDetailsService;
	}
	public void setCasAsserionUserDetailsService(AuthenticationUserDetailsService casAsserionUserDetailsService) {
		this.casAsserionUserDetailsService = casAsserionUserDetailsService;
	}
}
