/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestAuthoritiesModelAuthzDAO abstract class.
 *     
 */
package ca.sciencestudio.model.dao.rest;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import ca.sciencestudio.model.Model;
import ca.sciencestudio.model.dao.AuthoritiesDAO;
import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.SimpleData;
import ca.sciencestudio.util.authz.Authorities;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 *
 * 
 */
public abstract class RestAuthoritiesModelAuthzDAO<T extends Model> extends AbstractRestModelAuthzDAO<T> implements AuthoritiesDAO {

	@Override
	public Data<Authorities> getAuthorities(String user, String gid) {
		Authorities authorities;
		try {
			authorities = getRestTemplate().getForObject(getAuthzUrl("/{gid}", "user={user}"), Authorities.class, gid, user);
		}
		catch(HttpClientErrorException e) {
			logger.debug("HTTP Client Error exception while getting Authorities: " + e.getMessage());
			return new SimpleData<Authorities>(new Authorities());
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while getting Authorities: " + e.getMessage());
			return new SimpleData<Authorities>(new ModelAccessException(e));
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get Authorities, size: " + authorities.size());
		}
		return new SimpleData<Authorities>(authorities);
	}
	
	protected String getAuthzUrl(String prefix, String... query) {
		return getRestUrl(getAuthzPath(), prefix, query);
	}
	
	protected abstract String getAuthzPath();
}
