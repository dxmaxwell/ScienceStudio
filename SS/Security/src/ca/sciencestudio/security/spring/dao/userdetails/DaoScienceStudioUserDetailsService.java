/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     DaoScienceStudioUserDetailsService class.
 *     
 */
package ca.sciencestudio.security.spring.dao.userdetails;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ca.sciencestudio.model.person.Account;
import ca.sciencestudio.model.person.dao.AccountBasicDAO;
import ca.sciencestudio.security.spring.core.userdetails.AccountNotFoundException;

/**
 * @author maxweld
 * 
 *
 */
public class DaoScienceStudioUserDetailsService implements AuthenticationUserDetailsService {

	private AccountBasicDAO accountBasicDAO;
	
	private Collection<GrantedAuthority> defaultAuthorities = Collections.emptySet();
	
	protected Log logger = LogFactory.getLog(getClass());
	
	
	@Override
	public UserDetails loadUserDetails(Authentication token) throws UsernameNotFoundException {
		
		String username = token.getName();
		
		Account account = accountBasicDAO.getByUsername(username);
		if(account == null) {
			throw new AccountNotFoundException("Account with username '" + username + "' not found.");
		}
		
		String password = account.getPassword();
		String personGid = account.getPersonGid();
		Account.Status status = account.getStatus();
		String authenticator = accountBasicDAO.getGidFacility();
		Set<GrantedAuthority> authorities =  new HashSet<GrantedAuthority>(defaultAuthorities);
		
		if(logger.isDebugEnabled()) {
			logger.debug("Granted authorities for: " + personGid + ", are: " + authorities);
		}
		
		return new DaoScienceStudioUserDetails(username, password, status, personGid, authenticator, authorities);
	}

	public AccountBasicDAO getAccountBasicDAO() {
		return accountBasicDAO;
	}
	public void setAccountBasicDAO(AccountBasicDAO accountBasicDAO) {
		this.accountBasicDAO = accountBasicDAO;
	}

	public Collection<GrantedAuthority> getDefaultAuthorities() {
		return defaultAuthorities;
	}
	public void setDefaultAuthorities(Collection<GrantedAuthority> defaultAuthorities) {
		this.defaultAuthorities = defaultAuthorities;
	}
}
