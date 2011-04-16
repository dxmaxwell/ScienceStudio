/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		SimpleDeviceProxy class.
 *     
 */
package ca.sciencestudio.device.proxy;

import ca.sciencestudio.util.state.SimpleStateMap;

/**
 * @author maxweld
 *
 */
public class SimpleDeviceProxy extends SimpleStateMap implements DeviceProxy  {

	public String getDeviceId() {
		return getName();
	}
	
	public void setDeviceId(String deviceId) {
		setName(deviceId);
	}
}
