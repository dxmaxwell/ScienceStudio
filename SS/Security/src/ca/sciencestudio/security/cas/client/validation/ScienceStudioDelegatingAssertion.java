/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ScienceStudioDelegatingAssertion class.
 *     
 */
package ca.sciencestudio.security.cas.client.validation;

import java.util.Date;
import java.util.Map;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;

/**
 * @author maxweld
 * 
 *
 */
public class ScienceStudioDelegatingAssertion implements Assertion {

	private static final long serialVersionUID = 1L;
	
	private Assertion assertion;
	private String authenticator;
	
	ScienceStudioDelegatingAssertion(Assertion assertion, String authenticator) {
		if(assertion == null || authenticator == null) {
			throw new IllegalArgumentException();
		}
		this.assertion = assertion;
		this.authenticator = authenticator;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ScienceStudioDelegatingAssertion)) {
			return false;
		}
		
		if(!authenticator.equals(((ScienceStudioDelegatingAssertion)obj).getAuthenticator())) {
			return false;
		}
		
		return assertion.equals(obj);
	}

	public String getAuthenticator() {
		return authenticator;
	}

	@Override
	public Map<?,?> getAttributes() {
		return assertion.getAttributes();
	}

	@Override
	public AttributePrincipal getPrincipal() {
		return assertion.getPrincipal();
	}

	@Override
	public Date getValidFromDate() {
		return assertion.getValidFromDate();
	}

	@Override
	public Date getValidUntilDate() {
		return assertion.getValidUntilDate();
	}

	@Override
	public String toString() {
		return "[authc=" + authenticator + "assertion=[principal=" + getPrincipal().toString() +
				", validFrom=" + getValidFromDate() + ", validUntil=" + getValidUntilDate() + 
				", attributes=" + getAttributes().toString() + "]]";
	}
}
