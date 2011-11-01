/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AuthoritiesDAO interface.
 *     
 */
package ca.sciencestudio.model.dao;

import ca.sciencestudio.util.authz.Authorities;

/**
 * @author maxweld
 *
 *
 */
public interface AuthoritiesDAO {

	public Data<Authorities> getAuthorities(String user, String gid);
}
