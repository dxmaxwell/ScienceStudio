/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     LinkedHashConverterMap class.
 *     
 */
package ca.sciencestudio.data.converter;

import java.util.Map;
import java.util.LinkedHashMap;

/**
 * @author maxweld
 *
 */
public class LinkedHashConverterMap extends LinkedHashMap<String, Object> implements ConverterMap {
	
	private static final long serialVersionUID = 1L;

	private String toFormat = "Unknown";
	private String fromFormat = "Unknown";
	private boolean forceUpdate = false;
	
	public LinkedHashConverterMap() {
		super();
	}

	public LinkedHashConverterMap(String fromFormat, String toFormat) {
		super();
		this.toFormat = toFormat;
		this.fromFormat = fromFormat;
	}
	
	public LinkedHashConverterMap(String fromFormat, String toFormat, boolean forceUpdate) {
		super();
		this.toFormat = toFormat;
		this.fromFormat = fromFormat;
		this.forceUpdate = forceUpdate;
	}
	
	public LinkedHashConverterMap(ConverterMap converterMap) {
		super(converterMap);
		setToFormat(converterMap.getToFormat());
		setFromFormat(converterMap.getFromFormat());
		setForceUpdate(converterMap.isForceUpdate());
	}

	public LinkedHashConverterMap(Map<? extends String, ? extends Object> map) {
		super(map);
	}

	public String getToFormat() {
		return toFormat;
	}
	
	public void setToFormat(String toFormat) {
		if((toFormat != null) && (toFormat.length() > 0)) {
			this.toFormat = toFormat; 
		}
	}
	
	public String getFromFormat() {
		return fromFormat;
	}
	
	public void setFromFormat(String fromFormat) {
		if((fromFormat != null) && (fromFormat.length() > 0)) {
			this.fromFormat = fromFormat;
		}
	}
	
	public boolean isForceUpdate() {
		return forceUpdate;
	}
	
	public void setForceUpdate(boolean forceUpdate) {
		this.forceUpdate = forceUpdate;
	}
	
	public Object get(Object key, Object defaultValue) {
		Object value = get(key);
		if(value != null) {
			return value;
		}
		return defaultValue;
	}
}
