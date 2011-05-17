/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ModelDAO interface.
 *     
 */
package ca.sciencestudio.model.dao;

import java.util.List;

import org.springframework.validation.Errors;

import ca.sciencestudio.model.Model;
import ca.sciencestudio.model.validators.ModelValidator;

/**
 * @author maxweld
 *
 */
public interface ModelDAO<T extends Model> {
	
	public Errors add(T t);
	public Errors edit(T t);
	public boolean remove(Object gid);
	
	public T get(Object gid);
	
	public List<T> getAll();
	
	public ModelValidator<T> getValidator();
}
