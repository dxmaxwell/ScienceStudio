/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SecurityAuthorizeTag class.
 *     
 */
package ca.sciencestudio.util.tags;

import java.util.Arrays;
import java.util.Collection;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import ca.sciencestudio.util.authz.Authorities;

/**
 * @author maxweld
 * 
 *
 */
public class SecurityAuthorizeTag extends TagSupport {

	private static final long serialVersionUID = 1L;
	
	private static final String AUTHORITIES_ATTR_NAME = "authorities";
	
	private static final String AUTHORITIES_SPLIT_REGEX = "(\\s+)|(\\s*[;,]\\s*)";
	
	private String ifContainsAll;
	private String ifContainsAny;
	private String ifContainsNone;
	private Object authorities;
	
	@Override
	public int doStartTag() throws JspException {
				
		Authorities authorities = getAuthoritiesFromObject(this.authorities);
		
		if(authorities == null) {
			Object obj = pageContext.findAttribute(AUTHORITIES_ATTR_NAME);
			authorities = getAuthoritiesFromObject(obj);
		}
		
		if(authorities == null) {
			throw new JspException("Authorities not found for 'authorize' tag.");
		}
		
		if(ifContainsAll != null) {
			String auths = ifContainsAll.trim();
			if(auths.length() > 0) {
				if(authorities.containsAll((Object[])auths.split(AUTHORITIES_SPLIT_REGEX))) {
					return EVAL_BODY_INCLUDE;
				}
				return SKIP_BODY;
			}
		}
		
		if(ifContainsAny != null) {
			String auths = ifContainsAny.trim();
			if(auths.length() > 0) {
				if(authorities.containsAny((Object[])auths.split(AUTHORITIES_SPLIT_REGEX))) {
					return EVAL_BODY_INCLUDE;
				}
				return SKIP_BODY;
			}
		}
		
		if(ifContainsNone != null) {
			String auths = ifContainsNone.trim();
			if(auths.length() > 0) {
				if(authorities.containsNone((Object[])auths.split(AUTHORITIES_SPLIT_REGEX))) {
					return EVAL_BODY_INCLUDE;
				}
				return SKIP_BODY;
			}
			
		}
		
		return SKIP_BODY;
	}
	
//	@Override
//	public int doEndTag() throws JspException {
//		// If 'authorities' attribute is not specified
//		// then the 'setAttribute' method is not called
//		// and the authorities will be saved between
//		// requests unless they are explicitly cleared. 
//		authorities = null;
//		return super.doEndTag();
//	}

	protected static Authorities getAuthoritiesFromObject(Object obj) {
		if(obj == null) {
			return null;
		}
		
		if(obj instanceof Authorities) {
			return (Authorities)obj;
		}
		
		if(obj instanceof Collection) {
			Authorities authorities = new Authorities();
			for(Object o : (Collection<?>)obj) {
				authorities.add(o.toString());
			}
			return authorities;
		}
		
		if(obj instanceof Object[]) {
			Authorities authorities = new Authorities();
			for(Object o : (Object[])obj) {
				authorities.add(o.toString());
			}
			return authorities;
		}
		
		if(obj instanceof String) {
			String[] splitAttr = ((String)obj).split(AUTHORITIES_SPLIT_REGEX);
			return new Authorities(Arrays.asList(splitAttr));
		}
		
		return null;
	}
	
	public String getIfContainsAll() {
		return ifContainsAll;
	}
	public void setIfContainsAll(String ifContainsAll) {
		this.ifContainsAll = ifContainsAll;
	}
	
	public String getIfContainsAny() {
		return ifContainsAny;
	}
	public void setIfContainsAny(String ifContainsAny) {
		this.ifContainsAny = ifContainsAny;
	}
	
	public String getIfContainsNone() {
		return ifContainsNone;
	}
	public void setIfContainsNone(String ifContainsNone) {
		this.ifContainsNone = ifContainsNone;
	}

	public Object getAuthorities() {
		return authorities;
	}
	public void setAuthorities(Object authorities) {
		this.authorities = authorities;
	}
}
