/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractModelValidator abstract class.
 *     
 */
package ca.sciencestudio.model.validators.support;

import org.springframework.validation.Errors;

import ca.sciencestudio.model.Model;
import ca.sciencestudio.model.validators.ModelValidator;
import ca.sciencestudio.util.text.ValidationUtils;

/**
 * @author maxweld
 *
 *
 */
public abstract class AbstractModelValidator<T extends Model> extends ValidationUtils implements ModelValidator<T> {
	
	@Override
	public boolean supports(Class<?> clazz) {
		return getModelClass().isAssignableFrom(clazz);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void validate(Object obj, Errors errors) {
		if(supports(obj.getClass())) {
			validate((T)obj, errors);
		}
		else {
			errors.reject(EC_CLASS_NOT_SUPPORTED, "Validator does not support class: " + obj.getClass());
		}
	}
	
	@Override
	public Errors validate(T t) {
		return invokeValidator(this, t);
	}
	
	public abstract void validate(T t, Errors errors);
	
	protected abstract Class<T> getModelClass();
}
