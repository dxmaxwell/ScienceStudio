/** Copyright (c) Canadian Light Source, Inc.  All rights reserved.
 *  - see license.txt for details.
 *  
 *  Description:
 *      LogEventListener class.
 */
package ca.sciencestudio.device.control.support;

import ca.sciencestudio.device.control.component.epics.EpicsDevice;
import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.event.DeviceEventListener;

public final class LogEventListener implements DeviceEventListener 
{
	public LogEventListener( Object usrArg )
	{
		EpicsDevice msgDevice = (EpicsDevice)usrArg;
		msgDevice.addEventListener(this);	
	}
	
	public void handleEvent(DeviceEvent event) 
	{
		//DeviceComponent msgDevice = (DeviceComponent)usrArg;
		switch( event.getDeviceEventType() )
		{
		case ALARM_CHANGE:
		case CONNECTIVITY_CHANGE:
			//msgDevice.log.info( msgDevice.id + ": " + event.getStatus().toString() );
			System.out.println("connectivity change");
			break;
		case VALUE_CHANGE:
			//String[] message = (String[])msgDevice.getValue();
			//msgDevice.log.info( msgDevice.id + ": " + message[0] );
			break;
		default:
			//msgDevice.log.error("oops");
			//TODO throw something
		} //end switch
	}
}

