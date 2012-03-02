/** Copyright (c) Canadian Light Source, Inc.  All rights reserved.
 *  - see license.txt for details.
 *  
 *  Description:
 *		GenericDevice class.
 */
package ca.sciencestudio.device.control.component.impl;

import ca.sciencestudio.device.control.DeviceType;
import ca.sciencestudio.device.control.component.DeviceComponent;

/**
 * @author maxweld
 *
 * @param <T>
 */
public class GenericDevice<T> extends DeviceComponent {

	private T value;
	
	public GenericDevice(String id, T value) {
		super(id);
		this. value = value;
		deviceType = DeviceType.Simple;
	}
	
	public Object getValue() {
		return getRawValue();
	}
	
	@SuppressWarnings("unchecked")
	public void setValue(Object value) {
		try {
			setRawValue((T)value);
		}
		catch(ClassCastException e) {
			
		}
	}
	
	public T getRawValue() {
		return value;
	}
	
	public void setRawValue(T value) {
		this.value = value;
	}
	
	public String toString() {
		return value.toString();
	}
}
