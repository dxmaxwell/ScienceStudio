/* Copyright (c) Canadian Light Source, Inc.  All rights reserved.
 *  - see license.txt for details.
 *  
 *  Description:
 *     DeviceStatus enum.
 *     
 */
package ca.sciencestudio.device.control;

import java.io.Serializable;

/**
 * @author chabotd
 *
 */
public enum DeviceStatus implements Serializable {
	NORMAL,
	ALARM,
	ERROR;
}
