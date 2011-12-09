/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     DeviceProxy interface.
 *     
 */
package ca.sciencestudio.device.proxy;

import ca.sciencestudio.util.state.StateMap;

/**
 * @author maxweld
 *
 */
public interface DeviceProxy extends StateMap {
	
	public String getDeviceId();
}
