/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ModelAuthzDAO interface.
 *     
 */
package ca.sciencestudio.model.dao;

import java.util.List;

import ca.sciencestudio.model.Model;

/**
 * @author maxweld
 *
 *
 */
public interface ModelAuthzDAO<T extends Model> {

	public boolean add(String personGid, T t);
	public boolean add(String personGid, T t, String facility);
	public boolean edit(String personGid, T t);
	public boolean remove(String personGid, String gid);
	
	public T get(String personGid, String gid);
	
	public List<T> getAll(String personGid, boolean admin);
}
