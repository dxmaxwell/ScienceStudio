/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     LoginGroup interface.
 *     
 */
package ca.sciencestudio.login.model;

import java.io.Serializable;

/**
 * @author maxweld
 *
 */
public interface LoginGroup extends Serializable {

	public int getId();
	public void setId(int id);
	
	public String getName();
	public void setName(String name);
}
