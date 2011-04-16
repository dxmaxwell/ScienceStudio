/** Copyright (c) Canadian Light Source, Inc.  All rights reserved.
 *  - see license.txt for details.
 *  
 *  Description:
 *  	EpicsDevice class.
 *
 */
package ca.sciencestudio.device.control.component.epics;

import java.io.Serializable;

import gov.aps.jca.CAException;
import gov.aps.jca.CAStatus;
import gov.aps.jca.Channel;
import gov.aps.jca.Monitor;
import gov.aps.jca.TimeoutException;
import gov.aps.jca.Channel.ConnectionState;
import gov.aps.jca.dbr.CTRL;
import gov.aps.jca.dbr.DBR;
import gov.aps.jca.dbr.DBRType;
import gov.aps.jca.dbr.LABELS;
import gov.aps.jca.dbr.STS;
import gov.aps.jca.event.ConnectionEvent;
import gov.aps.jca.event.ConnectionListener;
import gov.aps.jca.event.MonitorEvent;
import gov.aps.jca.event.MonitorListener;

import com.cosylab.epics.caj.CAJChannel;
import com.cosylab.epics.caj.CAJContext;

import ca.sciencestudio.device.control.component.DeviceComponent;
import ca.sciencestudio.device.control.component.epics.EpicsAlarmStatus;
import ca.sciencestudio.device.control.DeviceStatus;
import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.event.DeviceEventType;
import ca.sciencestudio.device.control.support.DeviceException;


/**
 * Implements GoF State and Facade patterns, abstracting an EPICS PV as a DeviceComponent.
 * @author chabotd
 */
public class EpicsDevice extends DeviceComponent {

	public static long INITIALIZE_DELAY_MILLISECONDS = 100L;
	public static double PENDIO_TIMEOUT_SECONDS = 1.0;
	public static int PENDIO_MAX_RETRY = 9;
	
	private boolean monitor;
	private CAJContext context;
	private CAJChannel channel;
	private DBR dbr;
	private DBRType requestType; //the data-type we want the channel to return
	private DBRType nativeType;  //the native data-type of the channel
	private int nativeElementCount = 0;
	/*private IState connected; 
	private IState disconnected;*/
	private ConnectionState currentConnectionState = Channel.NEVER_CONNECTED;
	private CAConnectionListener connListener = new CAConnectionListener();
	private CAMonitorListener monListener = new CAMonitorListener();
	

/**************************************************************************/
	public EpicsDevice(String dbRecordName, CAJContext context) {
		this(dbRecordName, context, true);
	}
	
	public EpicsDevice(String dbRecordName, CAJContext context, boolean monitor) {
		super(dbRecordName);
		this.context = context;
		this.monitor = monitor;
		initialize();
	}
	
	/**
	 * Called once in ctor
	 */
	private void initialize() {
		try {//FIXME -- what if the channel doesn't exist ?
			alarm.setStatus("N/A");
			alarm.setSeverity("N/A");
			setStatus(DeviceStatus.ERROR);
			channel = (CAJChannel) context.createChannel(getId(), connListener);
			context.flushIO();
			Thread.sleep(INITIALIZE_DELAY_MILLISECONDS);
		}
		catch (CAException e) {
			throw new DeviceException(e);
		} 
		catch (InterruptedException e) {
			// ignore interruption //
		}
	}
	
	public synchronized void destroy() {
		if(channel != null) {
			if(log.isDebugEnabled()) {
				log.debug("In "+id+" destroy()...");
			}
			deviceEventListeners.removeAll(deviceEventListeners);
			try {
				channel.removeConnectionListener(connListener);
				channel.destroy();
				channel = null;
			} catch (CAException e) {
				throw new DeviceException(e);
			}
		}
	}

	/**
	 * May block awaiting completion for up to 10 seconds.
	 * @return an array of values. Scaler-valued records return a single-element array.
	 */
	public Object getValue() {
		//return status.getValue();
		if((getStatus() == DeviceStatus.ERROR)) {
			return null;
		}
		try {
			int pendIORetryCount = 0;
			DBR dbr = channel.get(requestType, nativeElementCount);
			while(true) {
				try {
					context.pendIO(PENDIO_TIMEOUT_SECONDS);
					return dbr.getValue();
				}
				catch(TimeoutException e) {
					if(pendIORetryCount < PENDIO_MAX_RETRY) {
						pendIORetryCount++;
						log.warn("EPICS pendIO timeout exception thrown, retry " + pendIORetryCount + " of " + PENDIO_MAX_RETRY);				
					}
					else {
						log.warn("EPICS pendIO timeout exception thrown, all " + PENDIO_MAX_RETRY + " retries have been used");
						throw new DeviceException(e);
					}
				}	
			}
		}
		catch (CAException e) {
			throw new DeviceException(e);
		}
		catch(IllegalStateException e) {
			throw new DeviceException(e);
		}
	}

	/**
	 * Will return immediately (non-blocking).
	 */
	public void setValue(Object aValue) {
		//status.setValue(aValue);
		if(aValue instanceof double[]) {
			log.debug("SET EPICS DEVICE: " + getId() + ", VALUE: " + ((double[])aValue)[0]);
		}
		else if(aValue instanceof float[]) {
			log.debug("SET EPICS DEVICE: " + getId() + ", VALUE: " + ((float[])aValue)[0]);
		}
		else if(aValue instanceof int[]) {
			log.debug("SET EPICS DEVICE: " + getId() + ", VALUE: " + ((int[])aValue)[0]);
		}
		if(getStatus() != DeviceStatus.ERROR) {
			try {
				channel.put(nativeType, nativeElementCount, aValue);
				context.flushIO();
			} catch (CAException e) {
				throw new DeviceException(e);
			}
		}
	}
	
	public Number getLowerControlLimit() {
		return ((CTRL)dbr).getLowerCtrlLimit();
	}

	public Number getLowerDisplayLimit() {
		return ((CTRL)dbr).getLowerDispLimit();
	}

	public String getUnits() {
		if(requestType != DBRType.CTRL_ENUM) { 
			return ((CTRL)dbr).getUnits();
		}
		return null;
	}

	public Number getUpperControlLimit() {
		return ((CTRL)dbr).getUpperCtrlLimit();
	}

	public Number getUpperDisplayLimit() {
		return ((CTRL)dbr).getUpperDispLimit();
	}

	public String[] getLabels() {
		if(requestType == DBRType.CTRL_ENUM) {
			return ((LABELS)dbr).getLabels();
		}
		return null;
	}
	
	/**
	 * @return a String representation of the Channel's value, iff this Channel's data is a scaler data-type. 
	 */
	public String toString() {
		if(nativeElementCount == 1) {
			if(nativeType == DBRType.STRING) {
				String [] val = (String[]) dbr.getValue();
				return val[0];
			}
			else if(nativeType == DBRType.SHORT){
				short[] val = (short[]) dbr.getValue();
				return Short.toString(val[0]);
			}
			else if(nativeType == DBRType.FLOAT) { 
				float[] val = (float[]) dbr.getValue();
				return Float.toString(val[0]);
			}
			else if(nativeType == DBRType.ENUM) { 
				String[] labels = getLabels();
				short[] val = (short[]) dbr.getValue();
				return labels[val[0]];
			}
			else if(nativeType == DBRType.BYTE) {
				byte[] val = (byte[]) dbr.getValue();
				return Byte.toString(val[0]);
			}
			else if(nativeType == DBRType.INT) { 
				int[] val = (int[]) dbr.getValue();
				return Integer.toString(val[0]);
			}
			else if(nativeType == DBRType.DOUBLE) {
				double[] val = (double[]) dbr.getValue();
				return Double.toString(val[0]);
			}
			else {
				throw new DeviceException("Cannot convert DBR value toString()");
			}
		}
		return "";
	}
	
	/**
	   * Called once upon initial connection
	   * @param nativeType : DBRType
	   */
	private void setRequestDBRType(DBRType nativeType) {
		if(nativeType == DBRType.STRING) {
			requestType=DBRType.STS_STRING;
		}
		else if(nativeType == DBRType.SHORT){
			requestType=DBRType.CTRL_SHORT;
		}
		else if(nativeType == DBRType.FLOAT) { 
			requestType=DBRType.CTRL_FLOAT;
		}
		else if(nativeType == DBRType.ENUM) { 
			requestType=DBRType.CTRL_ENUM;
		}
		else if(nativeType == DBRType.BYTE) {
			requestType=DBRType.CTRL_BYTE;
		}
		else if(nativeType == DBRType.INT) { 
			requestType=DBRType.CTRL_INT;
		}
		else if(nativeType == DBRType.DOUBLE) {
			requestType=DBRType.CTRL_DOUBLE;
		}
		else {
			throw new DeviceException("Error setting DBR Type: Unknown type: " + nativeType);
		}	
	}
	
	/**
	 * Drives the CONNECTIVITY_CHANGE state-changes of this class.
	 */
	private class CAConnectionListener implements ConnectionListener {

		public void connectionChanged(ConnectionEvent ev) {
			if(log.isDebugEnabled()) {
				log.debug("In connectionChanged: "+ev.toString());
			}
			//have we disconnected ??
			if(!ev.isConnected() && currentConnectionState==Channel.CONNECTED) {
				setStatus(DeviceStatus.ERROR);
				currentConnectionState = channel.getConnectionState();
				final DeviceEvent evDev = new DeviceEvent(DeviceEventType.CONNECTIVITY_CHANGE, 
												id, (Serializable) dbr.getValue(), status, alarm);
				//let our listeners know what's up...
				publishEvent(evDev);
				
				if((parent != null) && (parent.getStatus() != DeviceStatus.ERROR)) {
					parent.setStatus(DeviceStatus.ERROR);
				}
			}
			//are we reconnecting ??
			else if (ev.isConnected() && currentConnectionState==Channel.DISCONNECTED) {
				/*XXX don't publish this event: a monitor event will fire to inform any listeners.
				 * 
				 * NOTE: the above comment ASSumes that a monitor has been placed on this channel
				 * to begin with... a monitor for each channel may become v. expensive...
				 */
				setStatus(DeviceStatus.NORMAL);
				currentConnectionState = channel.getConnectionState();
				
				if((parent!=null) && (parent.getStatus() != DeviceStatus.NORMAL)) {
					parent.setStatus(DeviceStatus.NORMAL);
				}
			}
			//are we connecting for the first time ??
			else if(ev.isConnected() && (currentConnectionState==Channel.NEVER_CONNECTED)) {
				setStatus(DeviceStatus.NORMAL);
				currentConnectionState = Channel.CONNECTED;
				nativeElementCount = channel.getElementCount();
				nativeType = channel.getFieldType();
				//set this channel to return the max amount of info on get()'s and monitor events...
				setRequestDBRType(nativeType);
				
				if(monitor) {
					/* hook up a channel-value-monitor. We don't need to retain a Monitor reference...*/
					try {
						channel.addMonitor(requestType, 
											nativeElementCount,
											Monitor.VALUE/*|Monitor.ALARM*/,
											monListener);
						
						context.flushIO();
					} catch (CAException e) {
						throw new DeviceException(e);
					}
				}
				
				if((parent!=null) && (parent.getStatus() != DeviceStatus.NORMAL)) {
					parent.setStatus(DeviceStatus.NORMAL);
				}
			}
		}
		
	}

	private class CAMonitorListener implements MonitorListener {

		public void monitorChanged(MonitorEvent ev) {
			if(log.isDebugEnabled()) { 
				log.debug(getId()+": monitorChanged - "+ev.toString());
			}
			if(ev.getStatus() == CAStatus.NORMAL) {
				//update our DBR object...
				dbr = ev.getDBR();
				//get alarm info:
				int alarmStatus = ((STS)dbr).getStatus().getValue();

				DeviceStatus devStat = status;
				DeviceEventType evType = DeviceEventType.VALUE_CHANGE;
				switch(status) {
					case NORMAL: {
						if(alarmStatus != EpicsAlarmStatus.NO_ALARM.ordinal()) {
							//this is a NORMAL->ALARM state-change: update our DeviceStatus
							devStat = DeviceStatus.ALARM;
							evType = DeviceEventType.ALARM_CHANGE;
						}
						//else: this is a VALUE_CHANGED, transition-to-self event
						break;
					}
					case ALARM: {
						if(alarmStatus==EpicsAlarmStatus.NO_ALARM.ordinal()) {
							//this is an ALARM->NORMAL state-change: update our DeviceStatus
							devStat = DeviceStatus.NORMAL;
						}
						/* else: this is a VALUE_CHANGED, transition-to-self event
						 * (and/or an Alarm-severity and/or Alarm-status change).
						 */
						evType = DeviceEventType.ALARM_CHANGE;
						break;
					}
					default: {
						log.error(id+",monitorChanged, default switch-case, AAARRRGGHH");
						return;
					}
				} //end switch(status)
				
				if(devStat != status) {
					setStatus(devStat);
				}
				//update our Alarm
				alarm.setStatus(((STS)dbr).getStatus().getName());
				alarm.setSeverity(((STS)dbr).getSeverity().getName());
				/*if(setAlarm) {
					alarm.setStatus(((STS)dbr).getStatus().getName());
					alarm.setSeverity(((STS)dbr).getSeverity().getName());
					if(parent!=null) {
						parent.setDeviceAlarm(alarm);
					}
				}*/
				
				final DeviceEvent evDev = new DeviceEvent(evType, id, (Serializable) dbr.getValue(),status, alarm);
				publishEvent(evDev);
				
				if(parent!=null) {
					parent.setDeviceAlarm(alarm);
					if(parent.getStatus()!=status) {
						parent.setStatus(status);
					}
				}
			}
			else { //XXX -- how else to handle the abnormal condition ???
				setStatus(DeviceStatus.ERROR);
				if((parent!=null) && (parent.getStatus() != DeviceStatus.ERROR)) {
					parent.setStatus(DeviceStatus.ERROR);
				}
				if(log.isErrorEnabled()) {
					log.error(id+": monitorChanged - abnormal status="+ev.getStatus().getMessage());
				}
			}
		}	
	}
}
