/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractModelController class.
 *     
 */
package ca.sciencestudio.rest.service.controllers;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;

import ca.sciencestudio.model.Model;

import ca.sciencestudio.model.dao.ModelBasicDAO;
import ca.sciencestudio.model.validators.ModelValidator;
import ca.sciencestudio.util.exceptions.ModelAccessException;
import ca.sciencestudio.util.rest.AddResult;
import ca.sciencestudio.util.rest.EditResult;
import ca.sciencestudio.util.rest.RemoveResult;

/**
 * @author maxweld
 *
 */
public abstract class AbstractModelController<T extends Model> {

	protected Log logger = LogFactory.getLog(getClass());
	
	protected AddResult doAdd(T t, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		if(getModelValidator() == null) {
			logger.warn("Validator is null. Check the application configuration.");
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new AddResult("Model Validator is required.");
		}
		
		Errors errors = getModelValidator().validate(t);
		if(errors.hasErrors()) {
			response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
			return new AddResult(errors);
		}
	
		try{
			getModelBasicDAO().add(t);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return new AddResult(e.getMessage());
		}
		
		String location = getLocationFromModel(t, request);	
		response.setHeader("Location", location);
		response.setStatus(HttpStatus.CREATED.value());
		return new AddResult(Collections.singletonList(location));
	}
	
	protected EditResult doEdit(T t, HttpServletResponse response) throws Exception {
		
		if(getModelValidator() == null) {
			logger.warn("Validator is null. Check the application configuration.");
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new EditResult("Model Validator is required.");
		}
	
		Errors errors = getModelValidator().validate(t);
		if(errors.hasErrors()) {
			response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
			return new EditResult(errors);
		}
		
		boolean found;
		try {
			found = getModelBasicDAO().edit(t);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new EditResult(e.getMessage());
		}
				
		if(!found) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return new EditResult("Model (" + t.getGid() +") not found.");
		}
		
		response.setStatus(HttpStatus.OK.value());
		return new EditResult();
	}
	
	protected RemoveResult doRemove(String gid, HttpServletResponse response) throws Exception{
		boolean success;
		try {
			success = getModelBasicDAO().remove(gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new RemoveResult(e.getMessage());
		}
		
		if(!success) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return new RemoveResult("Mode (" + gid + ") not found.");
		}
		
		return new RemoveResult();
	}
	
	protected Object doGet(String gid, HttpServletResponse response) throws Exception {
		T t;
		try {
			t = getModelBasicDAO().get(gid);
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
	
	protected String getLocationFromModel(T t, HttpServletRequest request) {
		StringBuffer buffer = new StringBuffer(); 
		buffer.append(request.getContextPath());
		buffer.append(request.getServletPath());
		buffer.append(getModelPath());
		buffer.append("/").append(t.getGid());
		return buffer.toString();
	}
	
	public abstract String getModelPath();
	
	public abstract ModelBasicDAO<T> getModelBasicDAO();
	
	public abstract ModelValidator<T> getModelValidator();
}
