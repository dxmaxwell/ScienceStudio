/** Copyright (c) Canadian Light Source, Inc.  All rights reserved.
 *  - see license.txt for details.
 *
 * Description:
 *    SpringDeviceFactoryImpl class.
 */
package ca.sciencestudio.device.control.factory.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import ca.sciencestudio.device.control.factory.DeviceFactory;
import ca.sciencestudio.device.control.factory.spring.AbstractSpringDeviceFactoryImpl;

/**
 * @author maxweld
 * 
 */
public class SpringDeviceFactoryImpl extends AbstractSpringDeviceFactoryImpl {
	
	public static boolean loadXmlApplicationContext(String configPath) throws BeansException {
		if(DeviceFactory.getDeviceFactoryImpl() == null) {
			new SpringDeviceFactoryImpl(new String[] { configPath });
			return true;
		}
		return false;
	}
	
	public static boolean loadXmlApplicationContext(String[] configPaths) throws BeansException {
		if(DeviceFactory.getDeviceFactoryImpl() == null) {
			new SpringDeviceFactoryImpl(configPaths);
			return true;
		}
		return false;
	}
	
	public static boolean loadXmlApplicationContext(String configPath, ApplicationContext parent) throws BeansException {
		if(DeviceFactory.getDeviceFactoryImpl() == null) {
			new SpringDeviceFactoryImpl(new String[] { configPath }, parent);
			return true;
		}
		return false;
	}
	
	public static boolean loadXmlApplicationContext(String[] configPaths, ApplicationContext parent) throws BeansException {
		if(DeviceFactory.getDeviceFactoryImpl() == null) {
			new SpringDeviceFactoryImpl(configPaths, parent);
			return true;
		}
		return false;
	}
	
	private SpringDeviceFactoryImpl(String[] configPaths) throws BeansException {
		super();
		applicationContext = new FileSystemXmlApplicationContext(configPaths);
		((AbstractApplicationContext)applicationContext).registerShutdownHook();
	}
	
	private SpringDeviceFactoryImpl(String[] configPaths, ApplicationContext parent) throws BeansException {
		super();
		applicationContext = new FileSystemXmlApplicationContext(configPaths, parent);
		((AbstractApplicationContext)applicationContext).registerShutdownHook();
	}
}

