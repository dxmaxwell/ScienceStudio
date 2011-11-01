/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractRestModelAuthzDAO abstract class.
 *     
 */
package ca.sciencestudio.model.dao.rest;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import ca.sciencestudio.model.Model;
import ca.sciencestudio.model.dao.AbstractModelDAO;
import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.ModelAuthzDAO;
import ca.sciencestudio.model.dao.SimpleData;
import ca.sciencestudio.model.utilities.GID;
import ca.sciencestudio.util.exceptions.AuthorizationException;
import ca.sciencestudio.util.exceptions.ModelAccessException;
import ca.sciencestudio.util.rest.AddResult;
import ca.sciencestudio.util.rest.EditResult;
import ca.sciencestudio.util.rest.RemoveResult;

/**
 * @author maxweld
 *
 *
 */
public abstract class AbstractRestModelAuthzDAO<T extends Model> extends AbstractModelDAO<T> implements ModelAuthzDAO<T> {
	
	private String origin;
	
	private String baseUrl;
	
	private RestTemplate restTemplate;
	
	
	@Override
	public Data<AddResult> add(String user, T t) {
		ResponseEntity<AddResult> response;
		try {
			response = getRestTemplate().postForEntity(getModelUrl("", "user={user}"), toRestModel(t), AddResult.class, user);	
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while adding Model: " + e.getMessage());
			return new SimpleData<AddResult>(new ModelAccessException(e));
		}
		AddResult result = response.getBody();
		HttpStatus status = response.getStatusCode();
		
		if(status.series() == HttpStatus.Series.SUCCESSFUL) {
		
			if(status != HttpStatus.CREATED) {
				logger.info("Response to POST does not have status code CREATED.");
			}
			
			String gid = parseLocationForGid(response.getHeaders().getLocation());
			
			if(gid == null) {
				if(status == HttpStatus.CREATED) {
					logger.info("Response to POST does not have header LOCATION.");
				}
				for(String l : result.getLocations()) {
					gid = parseLocationForGid(l);
					if(gid != null) { break; }
				}
			}
			
			if(gid == null) {
				ModelAccessException e = new ModelAccessException("Response to POST does not contain a valid GID");
				logger.warn("Error getting GID from successful response.", e);
				throw e;
			}
			
			t.setGid(gid);
			
			if(logger.isDebugEnabled()) {
				logger.debug("Add Model with GID: " + t.getGid());
			}
		}
		
		return new SimpleData<AddResult>(result);
	}
	
	@Override
	public Data<EditResult> edit(String user, T t) {
		ResponseEntity<EditResult> response;
		try {
			// RestTemplate does not support getting the response body from a PUT request, so use the more complex exchange method instead. // 
			response = getRestTemplate().exchange(getModelUrl("/{gid}", "user={user}"), HttpMethod.PUT, new HttpEntity<Object>(toRestModel(t)), EditResult.class, t.getGid(), user);
		}
		catch(HttpClientErrorException e) {
			logger.debug("HTTP Client Error exception while editing Model: " + e.getMessage());
			return new SimpleData<EditResult>(new ModelAccessException(e));
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Edit Model with GID: " + t.getGid());
		}
		
		return new SimpleData<EditResult>(response.getBody());
	}
	
	@Override
	public Data<RemoveResult> remove(String user, String gid) {
		ResponseEntity<RemoveResult> response;
		try {
			// RestTemplate does not support getting the response body from a DELETE request, so use the more complex exchange method instead. // 
			response = getRestTemplate().exchange(getModelUrl("/{gid}", "user={user}"), HttpMethod.DELETE, new HttpEntity<Object>(new HttpHeaders()), RemoveResult.class, gid, user);
		}
		catch(HttpClientErrorException e) {
			logger.debug("HTTP Client Error exception while removing Model: " + e.getMessage());
			if(e.getStatusCode() == HttpStatus.FORBIDDEN) {
				return new SimpleData<RemoveResult>(new AuthorizationException(e));
			} else {
				return new SimpleData<RemoveResult>(new ModelAccessException(e));
			}
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while removing Model: " + e.getMessage());
			return new SimpleData<RemoveResult>(new ModelAccessException(e));
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Remove Model with GID: " + gid);
		}
		return new SimpleData<RemoveResult>(response.getBody());
	}
	
	// Not required by the interface, buy many DAOs implement this method.
	public Data<T> get(String gid) {
		T t;
		try {
			t = getRestTemplate().getForObject(getModelUrl("/{gid}"), getModelClass(), gid);
		}
		catch(HttpClientErrorException e) {
			logger.debug("HTTP Client Error exception while editing Model: " + e.getMessage());
			return new SimpleData<T>((T)null);
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while getting Model: " + e.getMessage());
			return new SimpleData<T>(new ModelAccessException(e));
		}	
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get Model with GID: " + gid);
		}
		return new SimpleData<T>(t);
	}
	
	@Override
	public Data<T> get(String user, String gid) {
		T t;
		try {
			t = getRestTemplate().getForObject(getModelUrl("/{gid}", "user={user}"), getModelClass(), gid, user);
		}
		catch(HttpClientErrorException e) {
			logger.debug("HTTP Client Error exception while editing Model: " + e.getMessage());
			return new SimpleData<T>((T)null);
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while getting Model: " + e.getMessage());
			return new SimpleData<T>(new ModelAccessException(e));
		}	
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get Model with GID: " + gid);
		}
		return new SimpleData<T>(t);
	}

	// Not required by the interface, but many DAOs implement this method.
	public Data<List<T>> getAll() {
		List<T> ts;
		try {
			ts = Arrays.asList(getRestTemplate().getForObject(getModelUrl(""), getModelArrayClass()));
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while getting Model list: " + e.getMessage());
			return new SimpleData<List<T>>(new ModelAccessException(e));
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Models, size: " + ts.size());
		}
		return new SimpleData<List<T>>(Collections.unmodifiableList(ts));
	}
	
	// Not required by the interface, but many DAOs implement this method. 
	public Data<List<T>> getAll(String user) {
		List<T> ts;
		try {
			ts = Arrays.asList(getRestTemplate().getForObject(getModelUrl("", "user={user}"), getModelArrayClass(), user));
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while getting Model list: " + e.getMessage());
			return new SimpleData<List<T>>(new ModelAccessException(e));
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Models, size: " + ts.size());
		}
		return new SimpleData<List<T>>(Collections.unmodifiableList(ts));
	}
	
	protected String getModelUrl(String prefix, String... query) {
		return getRestUrl(getModelPath(), prefix, query);
	}
	
	protected String getRestUrl(String path, String prefix, String... query) {
		StringBuffer url = new StringBuffer(getBaseUrl());
		url.append(path).append(prefix);		
		
		List<String> parameters = new ArrayList<String>();
		if(origin != null) {
			parameters.add("origin=" + origin);
		}
		if((query != null)) {
			parameters.addAll(Arrays.asList(query));
		}
			
		boolean first = true;
		for(String p : parameters) {
			if(first) {
				url.append("?");
				first = false;
			} else {
				url.append("&");
			}
			url.append(p);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Constructed RESTful URL: " + url);
		}
		
		return url.toString();
	}
	
	protected String parseLocationForGid(String location) {
		try {
			return parseLocationForGid(URI.create(location));
		}
		catch(Exception e) {
			return null;
		}
	}
	
	protected String parseLocationForGid(URI location) {
		if(location == null) {
			return null;
		}
		
		String path = location.getPath();
		if((path == null) || (path.length() == 0)) {
			return null;
		}
		
		int gidStart = path.lastIndexOf("/");
		if(gidStart < 0) {
			gidStart = 0;
		} else {
			gidStart += 1;
		}
		
		int gidEnd = path.indexOf(".", gidStart);
		if(gidEnd < 0) {
			gidEnd = path.length();
		}
		
		GID gid = GID.parse(path.substring(gidStart, gidEnd));
		if(gid == null) {
			return null;
		}
		
		return gid.toString();
	}
	
	
	protected abstract Object toRestModel(T t);
	
	protected abstract String getModelPath();
	
	protected abstract Class<T> getModelClass();
	
	protected abstract Class<T[]> getModelArrayClass();
	
	
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}

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
