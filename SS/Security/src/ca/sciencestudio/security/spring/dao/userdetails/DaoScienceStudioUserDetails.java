/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     DaoScienceStudioUserDetails class.
 *     
 */
package ca.sciencestudio.security.spring.dao.userdetails;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import ca.sciencestudio.model.person.Account.Status;
import ca.sciencestudio.security.spring.core.userdetails.ScienceStudioUserDetails;

/**
 * @author maxweld
 * 
 *
 */
public class DaoScienceStudioUserDetails extends ScienceStudioUserDetails {
	
	private static final long serialVersionUID = 1L;
	
	public DaoScienceStudioUserDetails(ScienceStudioUserDetails userDetails) {
		super(userDetails);
	}

	public DaoScienceStudioUserDetails(String username, String password, Status status, String personGid, String authenticator, Collection<GrantedAuthority> authorities) {
		super(username, password, personGid, authenticator, authorities);
		
		switch(status) {
		case ACTIVE:
			enabled = true;
			accountNonLocked = true;
			accountNonExpired = true;
			credentialsNonExpired = true;
			break;
			
		case DISABLED:
			enabled = false;
			accountNonLocked = true;
			accountNonExpired = true;
			credentialsNonExpired = true;
			break;
			
		case EXPIRED:
			enabled = false;
			accountNonLocked = true;
			accountNonExpired = true;
			credentialsNonExpired = false;
			break;
		}
	}
}
