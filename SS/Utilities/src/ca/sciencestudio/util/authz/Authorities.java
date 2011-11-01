/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    Authorities class. 
 *     
 */
package ca.sciencestudio.util.authz;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author maxweld
 * 
 *
 */
public class Authorities extends HashSet<String> {

	private static final long serialVersionUID = 1L;

	private static final String SESSION_AUTHORITY_PREFIX = "SESSION_";
	private static final String PROJECT_AUTHORITY_PREFIX = "PROJECT_";
	private static final String FACILITY_AUTHORITY_PREFIX = "FACILITY_";
	
	public Authorities() {
		super();
	}

	public Authorities(Collection<? extends String> c) {
		super(c);
	}
	
	public static String getSessionAuthority(String role) {
		return SESSION_AUTHORITY_PREFIX + role;
	}
	
	public static String getProjectAuthority(String role) {
		return PROJECT_AUTHORITY_PREFIX + role;
	}
	
	public static String getFacilityAuthority(String role) {
		return FACILITY_AUTHORITY_PREFIX + role;
	}
	
	public boolean addSessionAuthority(String role) {
		return add(getSessionAuthority(role));
	}
	
	public boolean addProjectAuthority(String role) {
		return add(getProjectAuthority(role));
	}
	
	public boolean addFacilityAuthority(String role) {
		return add(getFacilityAuthority(role));
	}
	
	public boolean containsSessionAuthority() {
		return containsAuthorityWithPrefix(SESSION_AUTHORITY_PREFIX);
	}
	
	public boolean containsProjectAuthority() {
		return containsAuthorityWithPrefix(PROJECT_AUTHORITY_PREFIX);
	}
	
	public boolean containsFacilityAuthority() {
		return containsAuthorityWithPrefix(FACILITY_AUTHORITY_PREFIX);
	}
	
	public boolean containsAuthorityWithPrefix(String prefix) {
		for(String authority : this) {
			if(authority.startsWith(prefix)) {
				return true;
			}
		}
		return false;
	}
	
	public Authorities getSessionAuthorities() {
		return getAuthoritiesWithPrefix(SESSION_AUTHORITY_PREFIX);
	}
	
	public Authorities getProjectAuthorities() {
		return getAuthoritiesWithPrefix(PROJECT_AUTHORITY_PREFIX);
	}
	
	public Authorities getFacilityAuthorities() {
		return getAuthoritiesWithPrefix(FACILITY_AUTHORITY_PREFIX);
	}
	
	public Authorities getAuthoritiesWithPrefix(String prefix) {
		Authorities authorities = new Authorities();
		for(String auth : this) {
			if(auth.startsWith(prefix)) {
				authorities.add(auth);
			}
		}
		return authorities;
	}
	
	@Override
	public boolean contains(Object obj) {
		return super.contains(obj.toString());
	}
	
	public boolean containsAll(Object... authorities) {
		return containsAll(Arrays.asList(authorities));
	}
	
	@Override
	public boolean containsAll(Collection<?> objs) {
		for(Object obj : objs) {
			if(!contains(obj)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean containsAny(Object... authorities) {
		return containsAny(Arrays.asList(authorities));
	}
	
	public boolean containsAny(Collection<?> authorities) {
		for(Object authority : authorities) {
			if(contains(authority)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean containsNone(Object... authorities) {
		return !containsAny(authorities);
	}
	
	public boolean containsNone(Collection<?> authorities) {
		return !containsAny(authorities);
	}
}
