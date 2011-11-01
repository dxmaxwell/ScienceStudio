/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ModelAuthzDAO interface.
 *     
 */
package ca.sciencestudio.model.dao;

import ca.sciencestudio.model.Model;
import ca.sciencestudio.util.rest.AddResult;
import ca.sciencestudio.util.rest.EditResult;
import ca.sciencestudio.util.rest.RemoveResult;

/**
 * @author maxweld
 *
 *
 */
public interface ModelAuthzDAO<T extends Model> {
	
	public Data<AddResult> add(String user, T t);
	public Data<EditResult> edit(String user, T t);
	public Data<RemoveResult> remove(String user, String gid);
	
	public Data<T> get(String user, String gid);
}
