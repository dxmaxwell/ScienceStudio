/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     LdapDelegatingScienceStudioUserDetailsService class.
 *     
 */
package ca.sciencestudio.security.spring.ldap.userdetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;

import ca.sciencestudio.login.model.Account;
import ca.sciencestudio.login.model.LoginGroup;
import ca.sciencestudio.login.model.LoginGroupMember;
import ca.sciencestudio.login.model.dao.AccountDAO;
import ca.sciencestudio.login.model.dao.LoginGroupDAO;
import ca.sciencestudio.login.model.dao.LoginGroupMemberDAO;
import ca.sciencestudio.security.spring.core.userdetails.AccountNotFoundException;
import ca.sciencestudio.security.spring.core.userdetails.PersonNotFoundException;
import ca.sciencestudio.security.spring.core.userdetails.ScienceStudioUserDetails;
import ca.sciencestudio.security.spring.core.userdetails.ScienceStudioUserDetailsService;
import ca.sciencestudio.security.spring.ldap.authentication.LdapContextAuthenticationToken;

/**
 * @author maxweld
 *
 */
public class LdapDelegatingScienceStudioUserDetailsService implements AuthenticationUserDetailsService {

	private AccountDAO accountDAO;
	private LoginGroupDAO loginGroupDAO;
	private LoginGroupMemberDAO loginGroupMemberDAO;
	
	private ScienceStudioUserDetailsService scienceStudioUserDetailsService;
	
	private Collection<String> defaultLoginGroupNames = Collections.emptySet();
	
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
		
		ScienceStudioUserDetails userDetails = null;
		
		// First attempt to get user details. // 
		try {
			userDetails = loadScienceStudioUserDetails(token);
		}
		catch(AccountNotFoundException e) {
			addNewAccount(token.getName(), token.getName());
		}
		catch(PersonNotFoundException e) {
			userDetails = (ScienceStudioUserDetails) e.getExtraInformation();
		}
	
		if(userDetails == null) {	
			// Second attempt to get user details. //
			try {
				userDetails = loadScienceStudioUserDetails(token);
			}
			catch(PersonNotFoundException e) {
				userDetails = (ScienceStudioUserDetails) e.getExtraInformation();
			}
		}
		
		String dn = ldapContext.getDn().toString();
		return new LdapScienceStudioUserDetails(dn, userDetails);
	}
	
	protected void addNewAccount(String username, String personUid) {
		Account account = accountDAO.createAccount();
		account.setUsername(username);
		account.setPassword("LdapPassword");
		account.setPersonUid(personUid);
		account.setStatus(Account.Status.ACTIVE);
		accountDAO.addAccount(account);
		
		List<LoginGroup> loginGroupList = loginGroupDAO.getLoginGroupList();
		for(LoginGroup loginGroup : loginGroupList) {
			
			if(defaultLoginGroupNames.contains(loginGroup.getName())) {
				LoginGroupMember loginGroupMember = loginGroupMemberDAO.createLoginGroupMember();
				loginGroupMember.setLoginGroupId(loginGroup.getId());
				loginGroupMember.setPersonUid(personUid);			
				loginGroupMemberDAO.addLoginGroupMember(loginGroupMember);
			}
		}
	}
	
	protected ScienceStudioUserDetails loadScienceStudioUserDetails(Authentication token) {
		return (ScienceStudioUserDetails) scienceStudioUserDetailsService.loadUserDetails(token);
	}
	
	public void setDefaultLoginGroupNamesConfig(String defaultLoginGrougNamesConfig) {
		if(defaultLoginGrougNamesConfig != null) {
			String[] splitDefaultLoginGroupNamesConfig = defaultLoginGrougNamesConfig.split("[&:;,]+");
			setDefaultLoginGroupNames(Arrays.asList(splitDefaultLoginGroupNamesConfig));
		}
	}
	
	public AccountDAO getAccountDAO() {
		return accountDAO;
	}
	public void setAccountDAO(AccountDAO accountDAO) {
		this.accountDAO = accountDAO;
	}

	public LoginGroupDAO getLoginGroupDAO() {
		return loginGroupDAO;
	}
	public void setLoginGroupDAO(LoginGroupDAO loginGroupDAO) {
		this.loginGroupDAO = loginGroupDAO;
	}

	public LoginGroupMemberDAO getLoginGroupMemberDAO() {
		return loginGroupMemberDAO;
	}
	public void setLoginGroupMemberDAO(LoginGroupMemberDAO loginGroupMemberDAO) {
		this.loginGroupMemberDAO = loginGroupMemberDAO;
	}

	public Collection<String> getDefaultLoginGroupNames() {
		return defaultLoginGroupNames;
	}
	public void setDefaultLoginGroupNames(Collection<String> defaultLoginGroupNames) {
		this.defaultLoginGroupNames = defaultLoginGroupNames;
	}

	public ScienceStudioUserDetailsService getScienceStudioUserDetailsService() {
		return scienceStudioUserDetailsService;
	}
	public void setScienceStudioUserDetailsService(ScienceStudioUserDetailsService scienceStudioUserDetailsService) {
		this.scienceStudioUserDetailsService = scienceStudioUserDetailsService;
	}
}
