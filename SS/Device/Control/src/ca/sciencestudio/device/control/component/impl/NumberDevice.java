/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		NumberDevice class.
 * 
 */
package ca.sciencestudio.device.control.component.impl;

/**
 * @author maxweld
 *
 */
public class NumberDevice extends GenericDevice<Number> {
	
	
	public NumberDevice(String id, Number value) {
		super(id, value);
	}
	
	public int toInt() {
		return getRawValue().intValue();
	}
	
	public long toLong() {
		return getRawValue().longValue();
	}
	
	public float toFloat() {
		return getRawValue().floatValue();
	}
	
	public double toDouble() {
		return getRawValue().doubleValue();
	}
	
	public String toString() {
		return getRawValue().toString();
	}
}
