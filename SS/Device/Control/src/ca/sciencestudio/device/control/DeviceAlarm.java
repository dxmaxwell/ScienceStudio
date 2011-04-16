/* Copyright (c) Canadian Light Source, Inc.  All rights reserved.
 *  - see license.txt for details. 
 *
 * Description
 *    DeviceAlarm class.
 *    
 */
package ca.sciencestudio.device.control;

import java.io.Serializable;

/**
 * @author chabotd
 */
public class DeviceAlarm implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String status;
	private String severity;

	public DeviceAlarm(){}
	
	public DeviceAlarm(String status, String severity) {
		this.status = status;
		this.severity = severity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}
}

