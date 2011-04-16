/** Copyright (c) Canadian Light Source, Inc.  All rights reserved.
 *  - see license.txt for details.
 *
 * Description:
 *    AbstractSpringDeviceFactoryImpl class.
 */
package ca.sciencestudio.device.control.factory.spring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import ca.sciencestudio.device.control.Device;
import ca.sciencestudio.device.control.DeviceLocation;
import ca.sciencestudio.device.control.DeviceSubsystem;
import ca.sciencestudio.device.control.DeviceType;
import ca.sciencestudio.device.control.factory.impl.AbstractDeviceFactoryImpl;

/**
 * @author maxweld
 *
 */
public abstract class AbstractSpringDeviceFactoryImpl extends AbstractDeviceFactoryImpl {
	
	protected ApplicationContext applicationContext = null;
	protected Map<String,Device> deviceMap = null;
 
	public Device getDevice(String deviceId) {
		return getDevicesById().get(deviceId);
	}
	
	public Map<String,Device> getDevicesById() {
		if(deviceMap == null) {
			Map<String,Device> deviceBeanMap = applicationContext.getBeansOfType(Device.class);
			deviceMap = new HashMap<String,Device>();
			for(Device device : deviceBeanMap.values()) {
				deviceMap.put(device.getId(), device);
			}
		}
		return deviceMap;
	}

	public Map<DeviceLocation, List<Device>> getDevicesByLocation() {
		
		Map<DeviceLocation, List<Device>> deviceLocationMap = new HashMap<DeviceLocation,List<Device>>();
		
		for(DeviceLocation deviceLocation : DeviceLocation.values()) {
			deviceLocationMap.put(deviceLocation, new ArrayList<Device>());
		}
		
		for(Device device : getDevicesById().values()) {
			deviceLocationMap.get(device.getLocation()).add(device);
		}
		return deviceLocationMap;
	}

	public List<Device> getDevicesAtLocation(DeviceLocation deviceLocation) {
		
		List<Device> deviceList = new ArrayList<Device>();
		
		for(Device device : getDevicesById().values()) {
			if(device.getLocation().equals(deviceLocation)) {
				deviceList.add(device);
			}
		}
		return deviceList;
	}

	public Map<DeviceType, List<Device>> getDevicesByType() {
		
		Map<DeviceType,List<Device>> deviceTypeMap = new HashMap<DeviceType,List<Device>>();
		
		for(DeviceType deviceType : DeviceType.values()) {
			deviceTypeMap.put(deviceType, new ArrayList<Device>());
		}
		
		for(Device device : getDevicesById().values()) {
			deviceTypeMap.get(device.getDeviceType()).add(device);
		}
		return deviceTypeMap;
	}
	
	public List<Device> getDevicesOfType(DeviceType deviceType) {
		
		List<Device> deviceList = new ArrayList<Device>();
		
		for(Device device : getDevicesById().values()) {
			if(device.getDeviceType().equals(deviceType)) {
				deviceList.add(device);
			}
		}
		return deviceList;
	}
	
	public Map<DeviceSubsystem, List<Device>> getDevicesBySystem() {
		
		Map<DeviceSubsystem,List<Device>> deviceSystemMap = new HashMap<DeviceSubsystem,List<Device>>();
		
		for(DeviceSubsystem deviceSystem : DeviceSubsystem.values()) {
			deviceSystemMap.put(deviceSystem, new ArrayList<Device>());
		}
		
		for(Device device : getDevicesById().values()) {
			deviceSystemMap.get(device.getSubsystem()).add(device);
		}
		return deviceSystemMap;
	}

	public List<Device> getDevicesOfSystem(DeviceSubsystem deviceSystem) {
		
		List<Device> deviceList = new ArrayList<Device>();
		
		for(Device device : getDevicesById().values()) {
			if(device.getSubsystem().equals(deviceSystem)) {
				deviceList.add(device);
			}
		}
		return deviceList;
	}
	
	public void refreshDeviceFactory() {
		if(applicationContext instanceof AbstractApplicationContext) {
			((AbstractApplicationContext)applicationContext).refresh();
		}
		deviceMap = null;
		getDevicesById();
	}
}
