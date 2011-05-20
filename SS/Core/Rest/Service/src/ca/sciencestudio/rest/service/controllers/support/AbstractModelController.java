/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractModelController class.
 *     
 */
package ca.sciencestudio.rest.service.controllers.support;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;

import ca.sciencestudio.model.Model;

import ca.sciencestudio.model.dao.ModelDAO;
import ca.sciencestudio.model.dao.support.ModelAccessException;
import ca.sciencestudio.model.validators.ModelValidator;

/**
 * @author maxweld
 *
 */
public abstract class AbstractModelController<T extends Model, D extends ModelDAO<T>, V extends ModelValidator<T>> {

	private D modelDAO;
	private V validator;
	
	protected Log logger = LogFactory.getLog(getClass());
	
	public List<String> add(T t, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return add(t, null, request, response);
	}
	
	public List<String> add(T t, String facility, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(t == null) {
			response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
			return Collections.emptyList();
		}
		
		if(validator == null) {
			logger.warn("Validator is null. Check the application configuration.");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return Collections.emptyList();
		}
		
		Errors errors = validator.validate(t);
		if(errors.hasErrors()) {
			response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
			return Collections.emptyList();
		}
		
		boolean success;
		try {
			if(facility == null) {
				success = modelDAO.add(t);
			} else {
				success = modelDAO.add(t, facility);
			}
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return Collections.emptyList();
		}
		
		if(!success) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.emptyList();
		}
		
		StringBuffer location = new StringBuffer(); 
		location.append(request.getContextPath());
		location.append(request.getServletPath());
		location.append(getModelUrl());
		location.append("/").append(t.getGid());
		response.setHeader("Location", location.toString());
		response.setStatus(HttpServletResponse.SC_CREATED);
		return Collections.singletonList(location.toString());
	}
	
	public void edit(T t, String gid, HttpServletResponse response) throws Exception {
		if(t == null) {
			response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
			return;
		}
		
		if(validator == null) {
			logger.warn("Validator is null. Check the application configuration.");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		
		Errors errors = validator.validate(t);
		if(errors.hasErrors()) {
			response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
			return;
		}
		
		t.setGid(gid);
		
		boolean success;
		try {
			success = modelDAO.edit(t);
		}
		
		catch(ModelAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		
		if(!success) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		return;
	}
	
	public void remove(String gid, HttpServletResponse response) throws Exception{
		boolean success;
		try {
			success = modelDAO.remove(gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		
		if(!success) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		return;
	}
	
	public Object get(String gid, HttpServletResponse response) throws Exception {
		T t;
		try {
			t = modelDAO.get(gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return Collections.emptyMap();
		}
		
		if(t == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.emptyMap();
		}	
		return t; 
	}
	
	public List<T> getAll(HttpServletResponse response) {
		try {
			return modelDAO.getAll();
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return Collections.emptyList();
		}
	}
	
	protected abstract String getModelUrl();

	public D getModelDAO() {
		return modelDAO;
	}
	public void setModelDAO(D modelDAO) {
		this.modelDAO = modelDAO;
	}

	public V getValidator() {
		return validator;
	}
	public void setValidator(V validator) {
		this.validator = validator;
	}
}
