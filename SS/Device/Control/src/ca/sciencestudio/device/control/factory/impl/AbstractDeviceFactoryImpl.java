/** Copyright (c) Canadian Light Source, Inc.  All rights reserved.
 *  - see license.txt for details.
 *
 * Description:
 *    AbstractDeviceFactoryImpl class.
 *    
 */
package ca.sciencestudio.device.control.factory.impl;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.sciencestudio.device.control.Device;
import ca.sciencestudio.device.control.component.DeviceComponent;
import ca.sciencestudio.device.control.composite.DeviceComposite;
import ca.sciencestudio.device.control.factory.DeviceFactory;
import ca.sciencestudio.device.control.factory.DeviceFactoryImpl;

/**
 * @author maxweld
 *
 */
public abstract class AbstractDeviceFactoryImpl implements DeviceFactoryImpl {
	
	protected Log log = LogFactory.getLog(getClass());
	
	protected AbstractDeviceFactoryImpl() {
		DeviceFactory.setDeviceFactoryImpl(this);
	}

	public void publishDeviceValues() {
		
		Map<String, Device> devicesById = getDevicesById();
		List<Device> deviceList = new ArrayList<Device>();
		List<DeviceComponent> componentList = new ArrayList<DeviceComponent>();
		List<DeviceComposite<?>> compositeList = new ArrayList<DeviceComposite<?>>();
		
		for(Device device : devicesById.values()) {
			if(device instanceof DeviceComponent) {
				componentList.add((DeviceComponent)device);
			}
			else if(device instanceof DeviceComposite<?>) {
				compositeList.add((DeviceComposite<?>)device);
			} 
			else {
				deviceList.add(device);
			}
			device.stopPublishingEvents();
			device.startPublishingEvents();
		}
		
		log.info("Start publishing device values.");
		
		for(DeviceComponent component : componentList) {
			log.info("Publish value: " + component.getId());
			component.publishValue();
			Thread.yield();
		}
		
		for(DeviceComposite<?> composite : compositeList) {
			log.info("Publish value: " + composite.getId());
			composite.publishValue();
			Thread.yield();
		}
		
		for(Device device : deviceList) {
			log.info("Publish value: " + device.getId());
			device.publishValue();
			Thread.yield();
		}
		
		log.info("Done publishing device values.");
	}
}
