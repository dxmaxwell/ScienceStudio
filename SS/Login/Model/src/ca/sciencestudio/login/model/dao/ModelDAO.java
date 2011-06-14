/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ModelDAO interface.
 *     
 */
package ca.sciencestudio.login.model.dao;

import java.util.List;

import ca.sciencestudio.login.model.Model;

/** 
 * @author maxweld
 * 
 *
 */
public interface ModelDAO<T extends Model> {
	
	public boolean add(T t);
	public boolean edit(T t);
	public boolean remove(int id);
	
	public T get(int id);
	
	public List<T> getAll();
}
