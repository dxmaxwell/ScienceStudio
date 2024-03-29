/** Copyright (c) Canadian Light Source, Inc.  All rights reserved.
 *  - see license.txt for details.
 *  
 *  Description:
 *		StringDevice class.
 */
package ca.sciencestudio.device.control.component.impl;

/**
 * @author maxweld
 * 
 */
public class StringDevice extends GenericDevice<String> {

	public StringDevice(String id) {
		this(id, id);
	}

	public StringDevice(String id, String value) {
		super(id, value);
	}
}
