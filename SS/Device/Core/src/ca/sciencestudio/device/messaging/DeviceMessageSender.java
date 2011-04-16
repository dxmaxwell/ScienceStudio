/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		DeviceMessageSender interface.
 */
package ca.sciencestudio.device.messaging;

import ca.sciencestudio.device.messaging.DeviceMessage;

/** 
 * @author maxweld
 *
 */
public interface DeviceMessageSender {

	public void send(DeviceMessage<?> deviceMessage);
}
