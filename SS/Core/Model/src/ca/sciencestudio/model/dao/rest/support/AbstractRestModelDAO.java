/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractRestModelDAO abstract class.
 *     
 */
package ca.sciencestudio.model.dao.rest.support;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import ca.sciencestudio.model.Model;
import ca.sciencestudio.model.dao.support.AbstractModelDAO;
import ca.sciencestudio.model.utilities.GID;

/**
 * @author maxweld
 *
 *
 */
public abstract class AbstractRestModelDAO<T extends Model, R> extends AbstractModelDAO<T> {

	private String baseUrl;
	
	private RestTemplate restTemplate;
	
	@Override
	public Errors add(T t) {
		Errors errors = getValidator().validate(t);
		if(errors.hasErrors()) {
			return errors;
		}
		
		URI location;
		try {	
			location = getRestTemplate().postForLocation(getModelUrl(), toRestModel(t));
		}
		catch(RestClientException e) {
			logger.warn("Client error while adding Model", e);
			errors.reject(RestClientException.class.getSimpleName(), e.getMessage());
			return errors;
		}
		
		String gid = getPathBaseName(location);
		if(!GID.isValid(gid)) {
			logger.warn("Client response contains an invalid GID: " + gid);
			errors.reject(RestClientException.class.getSimpleName(), "Location header contains invalid GID.");
			return errors;
		}
		
		t.setGid(gid);
		if(logger.isDebugEnabled()) {
			logger.debug("Add Model with GID: " + t.getGid());
		}
		return errors;
	}
	

	@Override
	public Errors edit(T t) {
		Errors errors = getValidator().validate(t);
		if(errors.hasErrors()) {
			return errors;
		}
		
		try {
			getRestTemplate().put(getModelUrl("/{gid}"), toRestModel(t), t.getGid());
		}
		catch(RestClientException e) {
			logger.warn("Client error while editing Model with GID: " + t.getGid(), e);
			errors.reject(RestClientException.class.getSimpleName(), e.getMessage());
			return errors;
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Edit Model with GID: " + t.getGid());
		}
		return errors;
	}
	

	@Override
	public boolean remove(Object modelGid) {
		try {
			getRestTemplate().delete(getModelUrl("/{gid}"), modelGid);
		}
		catch(RestClientException e) {
			logger.warn("Client error while removing Model with GID: " + modelGid, e);
			return false;
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Remove Model with GID: " + modelGid);
		}
		return true;
	}

	
	@Override
	public T get(Object modelGid) {
		T t;
		try {
			t = getRestTemplate().getForObject(getModelUrl("/{gid}"), getModelClass(), modelGid);
		}
		catch(RestClientException e) {
			logger.warn("Client error while getting Model with GID: " + modelGid, e);
			return null;
		}	
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get Model with GID: " + modelGid);
		}
		return t;
	}

	@Override
	public List<T> getAll() {
		List<T> ts;
		try {
			ts = Arrays.asList(getRestTemplate().getForObject(getModelUrl(), getModelArrayClass()));
		}
		catch(RestClientException e) {
			logger.warn("Client error while getting Model list", e);
			return Collections.emptyList();
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Models, size: " + ts.size());
		}
		return Collections.unmodifiableList(ts);
	}
	
	protected String getPathBaseName(URI url) {
		if(url == null) {
			return null;
		}
		int lastSlashIdx = url.getPath().lastIndexOf("/");
		if(lastSlashIdx < 0) {
			return null;
		}
		return url.getPath().substring(lastSlashIdx+1);
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
