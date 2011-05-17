/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ModelValidator interface.
 *     
 */
package ca.sciencestudio.model.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ca.sciencestudio.model.Model;

/**
 * @author maxweld
 *
 */
public interface ModelValidator<T extends Model> extends Validator {

	public static final String EC_NULL = "null";
	public static final String EC_EMPTY = "empty";
	public static final String EC_FORMAT = "format";
	public static final String EC_INVALID = "invalid";
	public static final String EC_REQUIRED = "required";
	public static final String EC_MIN_LENGTH = "min.length";
	public static final String EC_MAX_LENGTH = "max.length";
	
	public static final String EC_AFTER = "after";
	public static final String EC_BEFORE = "before";
	
	public static final String EC_TYPE = "type";
	public static final String EC_FACILITY = "facility";
	
	public static final String EC_CLASS_NOT_SUPPORTED = "class.not.supported";
	
	public Errors validate(T t);
}
