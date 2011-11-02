/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ScienceStudioServiceTicketValidator class.
 *     
 */
package ca.sciencestudio.security.cas.client.validation;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.client.proxy.ProxyGrantingTicketStorage;
import org.jasig.cas.client.proxy.ProxyRetriever;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.TicketValidator;

import ca.sciencestudio.model.facility.Facility;
import ca.sciencestudio.model.facility.dao.FacilityAuthzDAO;

public class ScienceStudioServiceTicketValidator implements TicketValidator {

	private FacilityAuthzDAO facilityAuthzDAO;
		
	private boolean renew;
	private String proxyCallbackUrl;
	private Map<?,?> customParameters;
	private ProxyRetriever proxyRetriever;
	private ProxyGrantingTicketStorage proxyGrantingTicketStorage;
	
	protected Log logger = LogFactory.getLog(getClass());
	
	@Override
	public Assertion validate(String ticket, String service) throws TicketValidationException {

	
		List<Facility> facilities;
		
		try {
			facilities = facilityAuthzDAO.getAll().get();
		}
		catch(Exception e) {
			throw new TicketValidationException("Exception while getting list of Facilities.", e);
		}
		
		TicketValidationException first = null;
		for(Facility facility : facilities) {
			
			String casServerUrlPrefix = facility.getAuthcUrl();
			if((casServerUrlPrefix == null) || (casServerUrlPrefix.length() == 0)) {
				continue;
			}
				
			Cas20ServiceTicketValidator ticketValidator = 
				new Cas20ServiceTicketValidator(casServerUrlPrefix);
			
			if(proxyGrantingTicketStorage != null) {
				ticketValidator.setProxyGrantingTicketStorage(proxyGrantingTicketStorage);
			}
			
			if(proxyRetriever != null) {
				ticketValidator.setProxyRetriever(proxyRetriever);
			}
			
			if(customParameters != null) {
				ticketValidator.setCustomParameters(customParameters);
			}
			
			if(proxyCallbackUrl != null) {
				ticketValidator.setProxyCallbackUrl(proxyCallbackUrl);
			}
			
			ticketValidator.setRenew(renew);
			
			try {
				Assertion assertion = ticketValidator.validate(ticket, service);
				return new ScienceStudioDelegatingAssertion(assertion, facility.getName());
			}
			catch(TicketValidationException e) {
				if(first == null) { first = e; }
				logger.warn("Error while validating CAS ticket: " + ticket + ", and service: " + service, e);
			}
			catch(Exception e) {
				logger.warn("Error while attempting to validate service ticket with CAS server: " + casServerUrlPrefix, e);
			}
		}
		
		if(first != null) {
			throw first; 
		}
		
		throw new TicketValidationException("No valid CAS server found at the registered facilities.");
	}

	public FacilityAuthzDAO getFacilityAuthzDAO() {
		return facilityAuthzDAO;
	}
	public void setFacilityAuthzDAO(FacilityAuthzDAO facilityAuthzDAO) {
		this.facilityAuthzDAO = facilityAuthzDAO;
	}

	public boolean isRenew() {
		return renew;
	}
	public void setRenew(boolean renew) {
		this.renew = renew;
	}

	public String getProxyCallbackUrl() {
		return proxyCallbackUrl;
	}
	public void setProxyCallbackUrl(String proxyCallbackUrl) {
		this.proxyCallbackUrl = proxyCallbackUrl;
	}

	public Map<?, ?> getCustomParameters() {
		return customParameters;
	}
	public void setCustomParameters(Map<?, ?> customParameters) {
		this.customParameters = customParameters;
	}

	public ProxyRetriever getProxyRetriever() {
		return proxyRetriever;
	}
	public void setProxyRetriever(ProxyRetriever proxyRetriever) {
		this.proxyRetriever = proxyRetriever;
	}

	public ProxyGrantingTicketStorage getProxyGrantingTicketStorage() {
		return proxyGrantingTicketStorage;
	}
	public void setProxyGrantingTicketStorage(ProxyGrantingTicketStorage proxyGrantingTicketStorage) {
		this.proxyGrantingTicketStorage = proxyGrantingTicketStorage;
	}
}
