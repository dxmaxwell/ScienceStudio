/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ValidationBindingResult class.
 *     
 */
package ca.sciencestudio.util.web;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import ca.sciencestudio.util.rest.ValidationError;
import ca.sciencestudio.util.rest.ValidationResult;

/**
 * @author maxweld
 * 
 *
 */
public class ValidationBindingResult extends BeanPropertyBindingResult {

	private static final long serialVersionUID = 1L;

	protected static String simpleClassNameToObjectName(Object target) {
		
		if(target == null) {
			return "null";
		}
		
		String simpleName = target.getClass().getSimpleName();
		if(simpleName.length() < 1) {
			return "target";
		}
		
		if(simpleName.length() < 2) {
			return simpleName.toLowerCase();
		}
		
		return simpleName.substring(0,1).toLowerCase() + simpleName.substring(1);
	}
	
	public ValidationBindingResult(Object target) {
		this(target, simpleClassNameToObjectName(target));
	}
	
	public ValidationBindingResult(Object target, String objectName) {
		super(target, objectName);
	}
	
	public ValidationBindingResult(Object target, ValidationResult result) {
		this(target, simpleClassNameToObjectName(target), result);
	}

	public ValidationBindingResult(Object target, String objectName, ValidationResult result) {
		super(target, objectName);
	
		for(String field : result.getFieldErrors().keySet()) {
			ValidationError error = result.getFieldErrors().get(field);
			String dmsg = error.getDefaultMessage();
			Object[] args = error.getArguments();
			String[] codes = error.getCodes();
			addError(new FieldError(getObjectName(), field, getFieldValue(field), false, codes, args, dmsg));
		}
		
		for(ValidationError error : result.getGlobalErrors()) {
			String dmsg = error.getDefaultMessage();
			Object[] args = error.getArguments();
			String[] codes = error.getCodes();
			addError(new ObjectError(getObjectName(), codes, args, dmsg));
		}
	}
}
