/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractModelValidator abstract class.
 *     
 */
package ca.sciencestudio.model.validators;

import org.springframework.validation.Errors;

import ca.sciencestudio.model.Model;
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
	public Errors validate(T t) {
		return invokeValidator(this, t);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void validate(Object obj, Errors errors) {
		if(obj == null) {
			errors.reject(EC_CLASS_NOT_SUPPORTED, "Valdiator does not support NULL object.");
			return;
		}
		
		if(!supports(obj.getClass())) {
			errors.reject(EC_CLASS_NOT_SUPPORTED, "Validator does not support class: " + obj.getClass());
			return;
		}
		
		validate((T)obj, errors);
	}
	
	public abstract void validate(T t, Errors errors);
	
	protected abstract Class<T> getModelClass();
}
