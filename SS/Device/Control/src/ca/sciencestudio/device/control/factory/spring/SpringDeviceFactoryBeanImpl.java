/** Copyright (c) Canadian Light Source, Inc.  All rights reserved.
 *  - see license.txt for details.
 *
 * Description:
 *    SpringDeviceFactoryBeanImpl class.
 */
package ca.sciencestudio.device.control.factory.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;

import ca.sciencestudio.device.control.factory.spring.AbstractSpringDeviceFactoryImpl;

/**
 * @author maxweld
 *
 */
public class SpringDeviceFactoryBeanImpl extends AbstractSpringDeviceFactoryImpl implements ApplicationContextAware {

	public SpringDeviceFactoryBeanImpl() {
		super();
	}
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		((AbstractApplicationContext)applicationContext).registerShutdownHook();
	}	
}
