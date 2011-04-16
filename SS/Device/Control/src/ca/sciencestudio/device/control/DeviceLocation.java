/* Copyright (c) Canadian Light Source, Inc.  All rights reserved.
 *  - see license.txt for details.
 *
 *	Description:
 *		DeviceLocation class.
 */
package ca.sciencestudio.device.control;

/* just a little example... */
public enum DeviceLocation {
	SR("Storage Ring"),
	FE("Front End"),
	POE("Primary Optical Enclosure"),
	SOE("Secondary Optical Enclosure");
	
	private final String fullName;
	
	private DeviceLocation(String str) {
		fullName = str;
	}
	public String toString() {
		return fullName;
	}
}
