/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		AbstractScanDevice class.
 *     
 */
package ca.sciencestudio.vespers.bcm.daq;

import ca.sciencestudio.device.control.component.DeviceComponent;

/**
 * @author maxweld
 *
 */
public abstract class AbstractScanDevice extends DeviceComponent {

	public static enum ScanDeviceState { UNKNOWN, STOPPED, STARTING, PAUSED, ACQUIRING, STOPPING, COMPLETE };

	protected ScanDeviceState scanState = ScanDeviceState.UNKNOWN;
	
	protected AbstractScanDevice(String id) {
		super(id);
	}
	
	public ScanDeviceState getScanState() {
		return scanState;
	}
	
	abstract public void start();
	abstract public void stop();
	abstract public void pause();
}
