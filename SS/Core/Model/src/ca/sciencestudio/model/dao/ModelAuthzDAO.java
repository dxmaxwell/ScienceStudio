/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ModelAuthzDAO interface.
 *     
 */
package ca.sciencestudio.model.dao;

import ca.sciencestudio.model.Model;
import ca.sciencestudio.model.AddResult;
import ca.sciencestudio.model.EditResult;
import ca.sciencestudio.model.Permissions;

/**
 * @author maxweld
 *
 *
 */
public interface ModelAuthzDAO<T extends Model> {

	public Data<Permissions> permissions(String user);
	public Data<Permissions> permissions(String user, String gid);
	
	public Data<AddResult> add(String user, T t, String facility);
	public Data<EditResult> edit(String user, T t);
	public Data<Boolean> remove(String user, String gid);
	
	public Data<T> get(String user, String gid);
}
