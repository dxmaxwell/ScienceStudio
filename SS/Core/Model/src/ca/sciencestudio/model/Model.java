/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Model interface. Indicates class is part of the model.
 *     
 */
package ca.sciencestudio.model;

import java.io.Serializable;

/**
 * @author maxweld
 *
 */
public interface Model extends Serializable {
	
	public String getGid();
	public void setGid(String gid);
}
