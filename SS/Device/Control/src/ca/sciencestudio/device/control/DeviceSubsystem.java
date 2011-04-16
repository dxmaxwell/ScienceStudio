/* Copyright (c) Canadian Light Source, Inc.  All rights reserved.
 *  - see license.txt for details.
 *  
 *  Description:
 *  	DeviceSubsystem enum.   
 *
 */
package ca.sciencestudio.device.control;

import java.io.Serializable;

/**
 * @author chabotd
 */
public enum DeviceSubsystem implements Serializable {
	CRYOGENICS,
	INSTRUMENTATION,
	VACUUM,
	OPTICS,
	MISC;
}
