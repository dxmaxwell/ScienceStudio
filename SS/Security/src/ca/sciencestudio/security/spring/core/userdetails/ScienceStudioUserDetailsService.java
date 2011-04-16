/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ScienceStudioUserDetailsService class.
 *     
 */
package ca.sciencestudio.security.spring.core.userdetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ca.sciencestudio.login.model.Account;
import ca.sciencestudio.login.model.LoginGroup;
import ca.sciencestudio.login.model.LoginRole;
import ca.sciencestudio.login.model.dao.AccountDAO;
import ca.sciencestudio.login.model.dao.LoginGroupDAO;
import ca.sciencestudio.login.model.dao.LoginRoleDAO;
import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.model.person.dao.PersonDAO;
import ca.sciencestudio.model.project.ProjectPerson;
import ca.sciencestudio.model.project.dao.ProjectPersonDAO;
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.UnknownPerson;

import ca.sciencestudio.security.spring.core.userdetails.PersonNotFoundException;
import ca.sciencestudio.security.spring.core.userdetails.AccountNotFoundException;

/**
 * @author maxweld
 *
 */
public class ScienceStudioUserDetailsService implements UserDetailsService, AuthenticationUserDetailsService {

	private AccountDAO accountDAO;
	private LoginRoleDAO loginRoleDAO;
	private LoginGroupDAO loginGroupDAO;
	
	private PersonDAO personDAO;
	private ProjectPersonDAO projectPersonDAO;
	
	protected Log logger = LogFactory.getLog(getClass());
	
	@Override
	public UserDetails loadUserDetails(Authentication token) throws UsernameNotFoundException {
		return loadUserByUsername(token.getName());
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Account account = accountDAO.getAccountByUsername(username);		
		
		if(account == null) {
			throw new AccountNotFoundException("Account with username '" + username + "' not found.");
		}
		
		String personUid = account.getPersonUid();
		
		Set<GrantedAuthority> grantedAuthoritySet =  new HashSet<GrantedAuthority>();
		
		List<LoginRole> loginRoleList = loginRoleDAO.getLoginRoleListByPersonUid(personUid);
		for(LoginRole loginRole : loginRoleList) {
			addGrantedAuthorities(grantedAuthoritySet, loginRole);
		}
		
		List<LoginGroup> loginGroupList = loginGroupDAO.getLoginGroupListByPersonUid(personUid);
		for(LoginGroup loginGroup : loginGroupList) {
			addGrantedAuthorities(grantedAuthoritySet, loginGroup);
		}
		
		List<ProjectPerson> projectPersonList = projectPersonDAO.getProjectPersonListByPersonUid(personUid);
		for(ProjectPerson projectPerson : projectPersonList) {
			addGrantedAuthorities(grantedAuthoritySet, projectPerson);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Granted authorities for: " + personUid + ", are: " + grantedAuthoritySet);
		}
		
		Person person = personDAO.getPersonByUid(personUid);
		if(person == null) {
			ScienceStudioUserDetails userDetails = new ScienceStudioUserDetails(account, new UnknownPerson(personUid), grantedAuthoritySet);
			throw new PersonNotFoundException("Person with UID '" + personUid + "' not found.", userDetails);
		}
		
		return new ScienceStudioUserDetails(account, person, grantedAuthoritySet);
	}

	
	protected void addGrantedAuthorities(Collection<GrantedAuthority> grantedAuthorities, LoginRole loginRole) {
		grantedAuthorities.add(new GrantedAuthorityImpl(AuthorityUtil.buildRoleAuthority(loginRole).toString()));
	}
	
	protected void addGrantedAuthorities(Collection<GrantedAuthority> grantedAuthorities, LoginGroup loginGroup) {
		grantedAuthorities.add(new GrantedAuthorityImpl(AuthorityUtil.buildGroupAuthority(loginGroup).toString()));
	}
	
	protected void addGrantedAuthorities(Collection<GrantedAuthority> grantedAuthorities, ProjectPerson projectPerson) {
		grantedAuthorities.add(new GrantedAuthorityImpl(AuthorityUtil.buildProjectRoleAuthority(projectPerson).toString()));
		grantedAuthorities.add(new GrantedAuthorityImpl(AuthorityUtil.buildProjectGroupAuthority(projectPerson).toString()));
	}
	
	public AccountDAO getAccountDAO() {
		return accountDAO;
	}
	public void setAccountDAO(AccountDAO accountDAO) {
		this.accountDAO = accountDAO;
	}

	public LoginRoleDAO getLoginRoleDAO() {
		return loginRoleDAO;
	}
	public void setLoginRoleDAO(LoginRoleDAO loginRoleDAO) {
		this.loginRoleDAO = loginRoleDAO;
	}

	public LoginGroupDAO getLoginGroupDAO() {
		return loginGroupDAO;
	}
	public void setLoginGroupDAO(LoginGroupDAO loginGroupDAO) {
		this.loginGroupDAO = loginGroupDAO;
	}

	public PersonDAO getPersonDAO() {
		return personDAO;
	}
	public void setPersonDAO(PersonDAO personDAO) {
		this.personDAO = personDAO;
	}

	public ProjectPersonDAO getProjectPersonDAO() {
		return projectPersonDAO;
	}
	public void setProjectPersonDAO(ProjectPersonDAO projectPersonDAO) {
		this.projectPersonDAO = projectPersonDAO;
	}
}
