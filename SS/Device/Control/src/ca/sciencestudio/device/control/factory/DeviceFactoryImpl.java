/** Copyright (c) Canadian Light Source, Inc.  All rights reserved.
 *  - see license.txt for details.
 *
 * Description:
 *    DeviceFactoryImpl interface.
 *    
 */
package ca.sciencestudio.device.control.factory;

import java.util.List;
import java.util.Map;

import ca.sciencestudio.device.control.Device;
import ca.sciencestudio.device.control.DeviceLocation;
import ca.sciencestudio.device.control.DeviceSubsystem;
import ca.sciencestudio.device.control.DeviceType;

/**
 * @author maxweld
 *
 */
public interface DeviceFactoryImpl {

	public Device getDevice(String deviceId);
	public Map<String,Device> getDevicesById();
	
	public Map<DeviceLocation, List<Device>> getDevicesByLocation();
	public List<Device> getDevicesAtLocation(DeviceLocation location);
	
	public Map<DeviceType, List<Device>> getDevicesByType();
	public List<Device> getDevicesOfType(DeviceType type);
	
	public Map<DeviceSubsystem, List<Device>> getDevicesBySystem();
	public List<Device> getDevicesOfSystem(DeviceSubsystem system);
	
	public void publishDeviceValues();
	
	public void refreshDeviceFactory();
}
