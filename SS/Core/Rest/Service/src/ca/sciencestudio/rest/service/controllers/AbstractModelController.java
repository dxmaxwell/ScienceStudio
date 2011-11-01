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

import ca.sciencestudio.model.AddResult;
import ca.sciencestudio.model.EditResult;
import ca.sciencestudio.model.Model;

import ca.sciencestudio.model.dao.ModelBasicDAO;
import ca.sciencestudio.model.validators.ModelValidator;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 *
 */
public abstract class AbstractModelController<T extends Model> {

	protected Log logger = LogFactory.getLog(getClass());
	
	public AddResult doAdd(T t, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		if(getModelValidator() == null) {
			logger.warn("Validator is null. Check the application configuration.");
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new AddResult(/* Message here? */);
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
			return new AddResult(/* Message here? */);
		}
		
		StringBuffer location = new StringBuffer(); 
		location.append(request.getContextPath());
		location.append(request.getServletPath());
		location.append(getModelPath());
		location.append("/").append(t.getGid());
		response.setHeader("Location", location.toString());
		response.setStatus(HttpStatus.CREATED.value());
		return new AddResult(location.toString());
	}
	
	public EditResult doEdit(T t, HttpServletResponse response) throws Exception {
		
		if(getModelValidator() == null) {
			logger.warn("Validator is null. Check the application configuration.");
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new EditResult(/* Message here? */);
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
			return new EditResult(/* Message here? */);
		}
				
		if(!found) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return new EditResult(/* Message here? */);
		}
		
		response.setStatus(HttpStatus.FOUND.value());
		return new EditResult();
	}
	
	public void doRemove(String gid, HttpServletResponse response) throws Exception{
		boolean success;
		try {
			success = getModelBasicDAO().remove(gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return;
		}
		
		if(!success) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return;
		}
		
		response.setStatus(HttpStatus.NO_CONTENT.value());
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
	
	public abstract String getModelPath();
	
	public abstract ModelBasicDAO<T> getModelBasicDAO();
	
	public abstract ModelValidator<T> getModelValidator();
}
