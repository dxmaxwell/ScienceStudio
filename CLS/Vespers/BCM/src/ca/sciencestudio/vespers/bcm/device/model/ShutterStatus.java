/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		ShutterStatus class.
 *     
 */
package ca.sciencestudio.vespers.bcm.device.model;

import java.util.Map;
import java.util.HashMap;

import ca.sciencestudio.device.control.Device;
import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.event.DeviceEventType;
import ca.sciencestudio.device.control.composite.DeviceComposite;
import ca.sciencestudio.device.control.event.DeviceEventListener;

/**
 * @author maxweld
 *
 */
public class ShutterStatus extends DeviceComposite<Device> implements DeviceEventListener {

	protected static final String COMPONENT_KEY_PHOTON_SHUTTER_1_STATE = "photonShutter1State";
	protected static final String COMPONENT_KEY_PHOTON_SHUTTER_2_STATE = "photonShutter2State";
	protected static final String COMPONENT_KEY_SAFETY_SHUTTER_1_STATE = "safetyShutter1State";
	protected static final String COMPONENT_KEY_SAFETY_SHUTTER_2_STATE = "safetyShutter1State";
	
	protected static final String[] SHUTTER_STATE = 
		{ "ERROR00", "OPEN", "BETWEEN", "ERROR03", "CLOSED", "ERROR05", "ERROR06", "ERROR07", 
			"ERROR8", "ERROR9", "ERROR10", "ERROR11", "ERROR12", "ERROR13", "ERROR14", "ERROR15" };
	
	public static final String VALUE_KEY_PHOTON_SHUTTER_1_STATE = "psh1State";
	public static final String VALUE_KEY_PHOTON_SHUTTER_2_STATE = "psh2State";
	public static final String VALUE_KEY_SAFETY_SHUTTER_1_STATE = "ssh1State";
	public static final String VALUE_KEY_SAFETY_SHUTTER_2_STATE = "ssh2State";
	
	public ShutterStatus(String id, Map<String,Device> components) {
		super(id, components);
		for(Device device : components.values()) {
			device.addEventListener(this);
		}
	}
	
	public Object getValue() {
		Map<String,Object> value = new HashMap<String,Object>();
		value.put(VALUE_KEY_PHOTON_SHUTTER_1_STATE, getPhotonShutter1State());
		value.put(VALUE_KEY_PHOTON_SHUTTER_2_STATE, getPhotonShutter2State());
		value.put(VALUE_KEY_SAFETY_SHUTTER_1_STATE, getSafetyShutter1State());
		value.put(VALUE_KEY_SAFETY_SHUTTER_2_STATE, getSafetyShutter2State());
		return value;
	}
	
	public String getPhotonShutter1State() {
		return getShutterState(COMPONENT_KEY_PHOTON_SHUTTER_1_STATE);
	}
	
	public String getPhotonShutter2State() {
		return getShutterState(COMPONENT_KEY_PHOTON_SHUTTER_2_STATE);
	}
	
	public String getSafetyShutter1State() {
		return getShutterState(COMPONENT_KEY_SAFETY_SHUTTER_1_STATE);
	}
	
	public String getSafetyShutter2State() {
		return getShutterState(COMPONENT_KEY_SAFETY_SHUTTER_2_STATE);
	}
	
	protected String getShutterState(String componentKey) {
		try {
			Device device = getComponent(componentKey);
			return getShutterState(device.getValue());
		}
		catch(NullPointerException e) {
			log.warn("Device component not found, " + componentKey, e);
			return "";
		}
	}
	
	protected String getShutterState(Object object) {
		try {
			short[] value = (short[]) object;
			return SHUTTER_STATE[value[0]];
		}
		catch(ClassCastException e) {
			log.warn("Device component value is unexpected class.", e);
			return "";
		}
		catch(NullPointerException e) {
			log.warn("Device component value is null (Disconnected EpicsDevice?).", e);
			return "";
		}
		catch(IndexOutOfBoundsException e) {
			log.warn("Device component value invalid array index.", e);
			return "";
		}
	}
		
	public void handleEvent(DeviceEvent event) {
		
		String deviceId = event.getDeviceId();
		DeviceEventType deviceEventType = event.getDeviceEventType();
		
		switch (deviceEventType) {
		
			case VALUE_CHANGE:
				Object value = event.getValue();
				HashMap<String,Object> newValueMap = new HashMap<String,Object>();
				
				if(getComponent(COMPONENT_KEY_PHOTON_SHUTTER_1_STATE).getId().equals(deviceId)) {
					newValueMap.put(VALUE_KEY_PHOTON_SHUTTER_1_STATE, getShutterState(value));	
				}
				else if(getComponent(COMPONENT_KEY_PHOTON_SHUTTER_2_STATE).getId().equals(deviceId)) {
					newValueMap.put(VALUE_KEY_PHOTON_SHUTTER_2_STATE, getShutterState(value));
				}
				else if(getComponent(COMPONENT_KEY_SAFETY_SHUTTER_1_STATE).getId().equals(deviceId)) {
					newValueMap.put(VALUE_KEY_SAFETY_SHUTTER_1_STATE, getShutterState(value));	
				}
				else if(getComponent(COMPONENT_KEY_SAFETY_SHUTTER_2_STATE).getId().equals(deviceId)) {
					newValueMap.put(VALUE_KEY_SAFETY_SHUTTER_2_STATE, getShutterState(value));
				}
				
				if(!newValueMap.isEmpty()) {
					publishEvent(new DeviceEvent(DeviceEventType.VALUE_CHANGE, id, newValueMap, status, alarm));
				}
				break;
			
			case ALARM_CHANGE:
			case CONNECTIVITY_CHANGE:
				break;
				
			default:
				log.warn("DeviceEventType, " + deviceEventType.toString() + ", not supported.");
				break;
		}
	}
}
