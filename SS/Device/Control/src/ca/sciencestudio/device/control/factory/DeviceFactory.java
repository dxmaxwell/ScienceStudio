/** Copyright (c) Canadian Light Source, Inc.  All rights reserved.
 *  - see license.txt for details.
 *  
 *  Description:
 *     DeviceFactory abstract class.
 */
package ca.sciencestudio.device.control.factory;

import java.util.List;
import java.util.Map;

import ca.sciencestudio.device.control.Device;
import ca.sciencestudio.device.control.DeviceLocation;
import ca.sciencestudio.device.control.DeviceSubsystem;
import ca.sciencestudio.device.control.DeviceType;
import ca.sciencestudio.device.control.factory.DeviceFactoryImpl;

/**
 * Implementors should probably be Singletons.
 * 
 * @author maxweld
 *
 */
public abstract class DeviceFactory {
	
	static DeviceFactoryImpl deviceFactoryImpl = null;
	
	public static DeviceFactoryImpl getDeviceFactoryImpl() {
		return deviceFactoryImpl;
	}
	public static void setDeviceFactoryImpl(DeviceFactoryImpl deviceFactoryImpl) {
		DeviceFactory.deviceFactoryImpl = deviceFactoryImpl;
	}
	
	public static Device getDevice(String deviceId) {
		return getDeviceFactoryImpl().getDevice(deviceId);
	}
	public static Map<String,Device> getDevicesById() {
		return getDeviceFactoryImpl().getDevicesById();
	}
	
	public static Map<DeviceLocation, List<Device>> getDevicesByLocation() {
		return getDeviceFactoryImpl().getDevicesByLocation();
	}
	public static List<Device> getDevicesAtLocation(DeviceLocation location) {
		return getDeviceFactoryImpl().getDevicesAtLocation(location);
	}
	
	public static Map<DeviceType, List<Device>> getDevicesByType() {
		return getDeviceFactoryImpl().getDevicesByType();
	}
	public static List<Device> getDevicesOfType(DeviceType type) {
		return getDeviceFactoryImpl().getDevicesOfType(type);
	}
	
	public static Map<DeviceSubsystem, List<Device>> getDevicesBySystem() {
		return getDeviceFactoryImpl().getDevicesBySystem();
	}
	public static List<Device> getDevicesOfSystem(DeviceSubsystem system) {
		return getDeviceFactoryImpl().getDevicesOfSystem(system);
	}
	
	public static void publishDeviceValues() {
		getDeviceFactoryImpl().publishDeviceValues();
	}
	
	public static void refreshDeviceFactory() {
		getDeviceFactoryImpl().refreshDeviceFactory();
	}
}
