/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Model interface.
 *     
 */
package ca.sciencestudio.login.model;

import java.io.Serializable;

/**
 * @author maxweld
 *
 *
 */
public interface Model extends Serializable {
	
	public int getId();
	public void setId(int id);
}
