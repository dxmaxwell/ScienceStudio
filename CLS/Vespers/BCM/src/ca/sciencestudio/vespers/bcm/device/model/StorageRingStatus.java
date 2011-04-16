/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		StorageRingStatus class.
 *     
 */
package ca.sciencestudio.vespers.bcm.device.model;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;

import ca.sciencestudio.device.control.Device;
import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.event.DeviceEventType;
import ca.sciencestudio.device.control.event.DeviceEventListener;
import ca.sciencestudio.device.control.composite.DeviceComposite;

/**
 * @author maxweld
 *
 */
public class StorageRingStatus extends DeviceComposite<Device> implements DeviceEventListener {

	protected static final String COMPONENT_KEY_SR_ENERGY = "srEnergy";
	protected static final String COMPONENT_KEY_SR_CURRENT = "srCurrent";
	protected static final String COMPONENT_KEY_SR_SHUTTERS = "srShutters";
	protected static final String COMPONENT_KEY_SR_HALFLIFE = "srHalfLife";
	protected static final String COMPONENT_KEY_SR_INJECTING = "srInjecting";
	protected static final String COMPONENT_KEY_SR_STATUS_MSG_L1 = "srStatusMsg1";
	protected static final String COMPONENT_KEY_SR_STATUS_MSG_L2 = "srStatusMsg2";
	protected static final String COMPONENT_KEY_SR_STATUS_MSG_L3 = "srStatusMsg3";
	
	protected static final String VALUE_KEY_SR_ENERGY = "srEnergy";
	protected static final String VALUE_KEY_SR_CURRENT = "srCurrent";
	protected static final String VALUE_KEY_SR_SHUTTERS = "srShutters";
	protected static final String VALUE_KEY_SR_HALFLIFE = "srHalfLife";
	protected static final String VALUE_KEY_SR_INJECTING = "srInjecting";
	protected static final String VALUE_KEY_SR_STATUS_MSG = "srStatusMsg";
	
	protected static final String[] SHUTTER_STATES = { "DISABLED", "ENABLED", "UNKNOWN" };
	
	public StorageRingStatus(String id, Map<String,Device> components) {
		super(id, components);
		for(Device device : components.values()) {
			device.addEventListener(this);
		}
	}
	
	@Override
	public Object getValue() {
		Map<String,Object> value = new HashMap<String,Object>();
		value.put(VALUE_KEY_SR_ENERGY, getStorageRingEnergy());
		value.put(VALUE_KEY_SR_CURRENT, getStorageRingCurrent());
		value.put(VALUE_KEY_SR_SHUTTERS, getStorageRingShutters());
		value.put(VALUE_KEY_SR_HALFLIFE, getStorageRingHalfLife());
		value.put(VALUE_KEY_SR_INJECTING, getStorageRingInjecting());
		value.put(VALUE_KEY_SR_STATUS_MSG, getStorageRingStatusMsg());
		return value;
	}

	public String getStorageRingEnergy() {
		Device device = getComponent(COMPONENT_KEY_SR_ENERGY);
		return getStorageRingEnergy(device.getValue());
	}
	
	public String getStorageRingCurrent() {
		Device device = getComponent(COMPONENT_KEY_SR_CURRENT);
		return getStorageRingCurrent(device.getValue());
	}
	
	public String getStorageRingShutters() {
		Device device = getComponent(COMPONENT_KEY_SR_SHUTTERS);
		return getStorageRingShutters(device.getValue());
	}
	
	public String getStorageRingHalfLife() {
		Device device = getComponent(COMPONENT_KEY_SR_HALFLIFE);
		return getStorageRingHalfLife(device.getValue());
	}
	
	public String getStorageRingInjecting() {
		Device device = getComponent(COMPONENT_KEY_SR_INJECTING);
		return getStorageRingInjecting(device.getValue());
	}
	
	public String getStorageRingStatusMsg() {
		Object value1 = getComponent(COMPONENT_KEY_SR_STATUS_MSG_L1).getValue();
		Object value2 = getComponent(COMPONENT_KEY_SR_STATUS_MSG_L2).getValue();
		Object value3 = getComponent(COMPONENT_KEY_SR_STATUS_MSG_L3).getValue();
		return getStorageRingStatusMsg(value1, value2, value3);
	}
	
	protected String getStorageRingEnergy(Object value) {
		try {
			String[] strArray = (String[]) value;
			return strArray[0];
		}
		catch(ClassCastException e) {
			log.warn("Value for '"+ VALUE_KEY_SR_ENERGY +"', is not expected class (String[]).", e);
			return getStorageRingEnergy(Double.NaN);
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + VALUE_KEY_SR_ENERGY + "', is null (Disconnected EpicsDevice?).", e);
			return getStorageRingEnergy(Double.NaN);
		}
	}
	
	protected String getStorageRingEnergy(double srEnergy) {
		return String.format("%.1f GeV", srEnergy);
	}
	
	protected String getStorageRingCurrent(Object value) {
		try {
			double[] dblArray = (double[]) value;
			return getStorageRingCurrent(dblArray[0]);
		}
		catch(ClassCastException e) {
			log.warn("Value for '"+ VALUE_KEY_SR_CURRENT +"', is not expected class (double[]).", e);
			return getStorageRingCurrent(Double.NaN);
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + VALUE_KEY_SR_CURRENT + "', is null (Disconnected EpicsDevice?).", e);
			return getStorageRingCurrent(Double.NaN);
		}
	}
	
	protected String getStorageRingCurrent(double srCurrent) {
		return String.format("%.2f mA", srCurrent);
	}
	
	protected String getStorageRingShutters(Object value) {
		try {
			short[] shtArray = (short[]) value;
			return SHUTTER_STATES[shtArray[0]];
		}
		catch(ClassCastException e) {
			log.warn("Value for '"+ VALUE_KEY_SR_SHUTTERS +"', is not expected class (double[]).", e);
			return SHUTTER_STATES[2];
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + VALUE_KEY_SR_SHUTTERS + "', is null (Disconnected EpicsDevice?).", e);
			return SHUTTER_STATES[2];
		}
		catch(IndexOutOfBoundsException e) {
			log.warn("Value for '" + VALUE_KEY_SR_SHUTTERS + "', is out of bounds.", e);
			return SHUTTER_STATES[2];
		}
	}
	
	protected String getStorageRingHalfLife(Object value) {
		try {
			double[] dblArray = (double[]) value;
			return getStorageRingHalfLife(dblArray[0]);
		}
		catch(ClassCastException e) {
			log.warn("Value for '"+ VALUE_KEY_SR_HALFLIFE +"', is not expected class (double[]).", e);
			return getStorageRingCurrent(Double.NaN);
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + VALUE_KEY_SR_HALFLIFE + "', is null (Disconnected EpicsDevice?).", e);
			return getStorageRingCurrent(Double.NaN);
		}
	}
	
	protected String getStorageRingHalfLife(double srHalfLife) {
		return String.format("%.2f hr", srHalfLife);
	}
	
	protected String getStorageRingInjecting(Object value) {
		try {
			double[] dblArray = (double[]) value;
			return String.valueOf(dblArray[0] != 0.0).toUpperCase();
		}
		catch(ClassCastException e) {
			log.warn("Value for '"+ VALUE_KEY_SR_INJECTING +"', is not expected class (double[]).", e);
			return "FALSE";
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + VALUE_KEY_SR_INJECTING + "', is null (Disconnected EpicsDevice?).", e);
			return "FALSE";
		}
	}
	
	protected String getStorageRingStatusMsg(Object value1, Object value2, Object value3) {
		StringBuffer srStatusMsg = new StringBuffer();
		srStatusMsg.append(getStorageRingStatusMsg(value1));
		srStatusMsg.append("\n");
		srStatusMsg.append(getStorageRingStatusMsg(value2));
		srStatusMsg.append("\n");
		srStatusMsg.append(getStorageRingStatusMsg(value3));
		return srStatusMsg.toString();
	}
	
	protected String getStorageRingStatusMsg(Object value) {
		try {
			return ((String[]) value)[0];	
		}
		catch(ClassCastException e) {
			log.warn("Value for '"+ VALUE_KEY_SR_STATUS_MSG +"', is not expected class (String?).", e);
			return "";
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + VALUE_KEY_SR_STATUS_MSG + "', is null (Disconnected EpicsDevice?).", e);
			return "";
		}
	}
	
	public void handleEvent(DeviceEvent event) {
		
		switch (event.getDeviceEventType()) {
		
			case VALUE_CHANGE:
				Map<String,Object> newValueMap = new HashMap<String,Object>();
				
				String deviceId = event.getDeviceId();
				Object value = event.getValue();
				
				if(getComponent(COMPONENT_KEY_SR_ENERGY).getId().equals(deviceId)) {
					newValueMap.put(VALUE_KEY_SR_ENERGY, getStorageRingEnergy(value));
				}
				else if(getComponent(COMPONENT_KEY_SR_CURRENT).getId().equals(deviceId)) {
					newValueMap.put(VALUE_KEY_SR_CURRENT, getStorageRingCurrent(value));
				}
				else if(getComponent(COMPONENT_KEY_SR_SHUTTERS).getId().equals(deviceId)) {
					newValueMap.put(VALUE_KEY_SR_SHUTTERS, getStorageRingShutters(value));
				}
				else if(getComponent(COMPONENT_KEY_SR_HALFLIFE).getId().equals(deviceId)) {
					newValueMap.put(VALUE_KEY_SR_HALFLIFE, getStorageRingHalfLife(value));
				}
				else if(getComponent(COMPONENT_KEY_SR_INJECTING).getId().equals(deviceId)) {
					newValueMap.put(VALUE_KEY_SR_INJECTING, getStorageRingInjecting(value));
				}
				else if(getComponent(COMPONENT_KEY_SR_STATUS_MSG_L1).getId().equals(deviceId)) {
					Object value2 = getComponent(COMPONENT_KEY_SR_STATUS_MSG_L2).getValue();
					Object value3 = getComponent(COMPONENT_KEY_SR_STATUS_MSG_L3).getValue();
					newValueMap.put(VALUE_KEY_SR_STATUS_MSG, getStorageRingStatusMsg(value, value2, value3));
				}
				else if(getComponent(COMPONENT_KEY_SR_STATUS_MSG_L2).getId().equals(deviceId)) {
					Object value1 = getComponent(COMPONENT_KEY_SR_STATUS_MSG_L1).getValue();
					Object value3 = getComponent(COMPONENT_KEY_SR_STATUS_MSG_L3).getValue();
					newValueMap.put(VALUE_KEY_SR_STATUS_MSG, getStorageRingStatusMsg(value1, value, value3));
				}
				else if(getComponent(COMPONENT_KEY_SR_STATUS_MSG_L3).getId().equals(deviceId)) {
					Object value1 = getComponent(COMPONENT_KEY_SR_STATUS_MSG_L1).getValue();
					Object value2 = getComponent(COMPONENT_KEY_SR_STATUS_MSG_L2).getValue();
					newValueMap.put(VALUE_KEY_SR_STATUS_MSG, getStorageRingStatusMsg(value1, value2, value));
				}
				
				if(!newValueMap.isEmpty()) {
					publishEvent(new DeviceEvent(DeviceEventType.VALUE_CHANGE, id, (Serializable) newValueMap, status, alarm));
				}
				break;
			
			case ALARM_CHANGE:
			case CONNECTIVITY_CHANGE:
				break;
				
			default:
				log.warn("DeviceEventType, " + event.getDeviceEventType() + ", not supported.");
				break;
		}
	}
}
