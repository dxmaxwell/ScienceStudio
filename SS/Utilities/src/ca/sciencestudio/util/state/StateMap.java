/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     StateMap interface.
 *     
 */
package ca.sciencestudio.util.state;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @author maxweld
 * 
 */
public interface StateMap extends Map<String,Serializable> {

	public String getName();
	public void setName(String name);
	
	public Date getTimestamp();
	public void setTimestamp(Date timestamp);
	
	public String toXML();
}
