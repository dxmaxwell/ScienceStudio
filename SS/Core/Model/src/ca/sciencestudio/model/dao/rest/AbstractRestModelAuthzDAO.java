/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractRestModelDAO abstract class.
 *     
 */
package ca.sciencestudio.model.dao.rest;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import ca.sciencestudio.model.Model;
import ca.sciencestudio.model.dao.AbstractModelDAO;
import ca.sciencestudio.model.dao.ModelAuthzDAO;
import ca.sciencestudio.util.exceptions.ModelAccessException;
import ca.sciencestudio.model.utilities.GID;

/**
 * @author maxweld
 *
 *
 */
public abstract class AbstractRestModelAuthzDAO<T extends Model, R> extends AbstractModelDAO<T> implements ModelAuthzDAO<T> {

	private String baseUrl;
	
	private RestTemplate restTemplate;
	
	@Override
	public boolean add(String personGid, T t) {
		return add(personGid, t, null);
	}
	
	@Override
	public boolean add(String personGid, T t, String facility) {
		URI location;
		try {
			if(facility == null) {
				location = getRestTemplate().postForLocation(getModelUrl(), toRestModel(t));
			} else {
				location = getRestTemplate().postForLocation(getModelUrl("/{facility}"), toRestModel(t), facility);
			}
		}
		catch(HttpClientErrorException e) {
			logger.debug("HTTP Client Error exception while adding Model: " + e.getMessage());
			return false;
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while adding Model: " + e.getMessage());
			throw new ModelAccessException(e);
		}
			
		if(location == null) {
			logger.warn("Rest Client response missing Location header.");
			return false;
		}
		
		String[] splitPath = location.getPath().split("/");
		String gid = splitPath[splitPath.length - 1];
		if(!GID.isValid(gid)) {
			logger.warn("Rest Client response contains an invalid GID: " + gid);
			return false;
		}	
		
		t.setGid(gid);
		if(logger.isDebugEnabled()) {
			logger.debug("Add Model with GID: " + t.getGid());
		}
		return true;
	}
	
	@Override
	public boolean edit(String personGid, T t) {
		try {
			getRestTemplate().put(getModelUrl("/{gid}"), toRestModel(t), t.getGid());
		}
		catch(HttpClientErrorException e) {
			logger.debug("HTTP Client Error exception while editing Model: " + e.getMessage());
			return false;
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while editing Model: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Edit Model with GID: " + t.getGid());
		}
		return true;
	}
	
	@Override
	public boolean remove(String personGid, String gid) {
		try {
			getRestTemplate().delete(getModelUrl("/{gid}"), gid);
		}
		catch(HttpClientErrorException e) {
			logger.debug("HTTP Client Error exception while editing Model: " + e.getMessage());
			return false;
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while removing Model: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Remove Model with GID: " + gid);
		}
		return true;
	}

	
	@Override
	public T get(String personGid, String gid) {
		T t;
		try {
			t = getRestTemplate().getForObject(getModelUrl("/{gid}"), getModelClass(), gid);
		}
		catch(HttpClientErrorException e) {
			logger.debug("HTTP Client Error exception while editing Model: " + e.getMessage());
			return null;
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while getting Model: " + e.getMessage());
			throw new ModelAccessException(e);
		}	
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get Model with GID: " + gid);
		}
		return t;
	}

	@Override
	public List<T> getAll(String personGid, boolean admin) {
		List<T> ts;
		try {
			ts = Arrays.asList(getRestTemplate().getForObject(getModelUrl(), getModelArrayClass()));
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while getting Model list: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Models, size: " + ts.size());
		}
		return Collections.unmodifiableList(ts);
	}
	
	protected String getModelUrl(String url) {
		return getModelUrl() + url;
	}
	
	protected abstract R toRestModel(T t);
	
	protected abstract String getModelUrl();
	
	protected abstract Class<T> getModelClass();
	
	protected abstract Class<T[]> getModelArrayClass();
	
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}
	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
}
