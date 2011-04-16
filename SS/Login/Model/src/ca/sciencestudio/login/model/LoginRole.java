/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     LoginRole interface.
 *     
 */
package ca.sciencestudio.login.model;

import java.io.Serializable;

/**
 * @author maxweld
 *
 */
public interface LoginRole extends Serializable {
	
	public int getId();
	public void setId(int id);
	
	public String getName();
	public void setName(String name);
}
