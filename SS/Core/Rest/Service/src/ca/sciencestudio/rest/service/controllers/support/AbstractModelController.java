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

import org.springframework.validation.Errors;

import ca.sciencestudio.model.Model;

import ca.sciencestudio.model.dao.ModelDAO;

/**
 * @author maxweld
 *
 */
public abstract class AbstractModelController<T extends Model, D extends ModelDAO<T>> {

	private D modelDAO;
	
	public List<String> add(T t, HttpServletRequest request, HttpServletResponse response) throws Exception{
		if(t == null) {
			response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
			return Collections.emptyList();
		}
		
		Errors errors = modelDAO.add(t);
		if(errors.hasErrors()) {
			response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
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
	
	public void edit(T t, String gid, HttpServletResponse response) throws Exception{
		if(t == null) {
			response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
			return;
		}
		
		t.setGid(gid);
		
		Errors errors = modelDAO.edit(t);
		if(errors.hasFieldErrors("gid") || errors.hasGlobalErrors()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		else if(errors.hasErrors()) {
			response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
			return;
		}
		
		response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		return;
	}
	
	public void remove(String gid, HttpServletResponse response) throws Exception{
		boolean success = modelDAO.remove(gid);
		if(!success) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		return;
	}
	
	public Object get(String gid, HttpServletResponse response) throws Exception {
		T t = modelDAO.get(gid);
		if(t == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.emptyMap();
		}	
		return t; 
	}
	
	public List<T> getAll() {
		return modelDAO.getAll();
	}
	
	protected abstract String getModelUrl();
	
	public D getModelDAO() {
		return modelDAO;
	}
	public void setModelDAO(D modelDAO) {
		this.modelDAO = modelDAO;
	}	
}
