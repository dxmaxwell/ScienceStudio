/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ConverterMap interface.
 *     
 */
package ca.sciencestudio.data.converter;

import java.util.Map;

/**
 * @author maxweld
 *
 */
public interface ConverterMap extends Map<String, Object> {
	
	public String getToFormat();
	public void setToFormat(String toFormat);
	
	public String getFromFormat();
	public void setFromFormat(String fromFormat);
	
	public boolean isForceUpdate();
	public void setForceUpdate(boolean forceUpdate);
	
	public Object get(Object key, Object defaultValue);	
}
