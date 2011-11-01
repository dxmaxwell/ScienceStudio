/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ModelBasicDAO interface.
 *     
 */
package ca.sciencestudio.model.dao;

import java.util.List;

import ca.sciencestudio.model.Model;

/**
 * @author maxweld
 *
 */
public interface ModelBasicDAO<T extends Model> {
	
	public String getGidType();
	public String getGidFacility();
	
	public void add(T t);
	public boolean edit(T t);
	public boolean remove(String gid);
	
	public T get(String gid);
	
	public List<T> getAll();
}
