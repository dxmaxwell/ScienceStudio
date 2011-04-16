/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		SimpleMotor class.
 *     
 */
package ca.sciencestudio.vespers.bcm.device.model;

import java.io.Serializable;

import java.util.Map;
import java.util.HashMap;

import ca.sciencestudio.device.control.component.DeviceComponent;
import ca.sciencestudio.device.control.composite.DeviceComposite;
import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.event.DeviceEventListener;
import ca.sciencestudio.device.control.event.DeviceEventType;

/**
 * @author maxweld
 *
 */
public class SimpleMotor extends DeviceComposite<DeviceComponent> {

	private static final String COMPONENT_KEY_POSITION = "position";
	private static final String COMPONENT_KEY_SET_POINT = "setPoint";
	private static final String COMPONENT_KEY_STATUS = "status";
	
	public static final String VALUE_KEY_POSITION = "position";
	public static final String VALUE_KEY_SET_POINT = "setPoint";
	public static final String VALUE_KEY_MOVING = "moving";
	
	private SimpleMotorDeviceEventListener simpleMotorDeviceEventListener; 
	
	public SimpleMotor(String id, Map<String,DeviceComponent> components) {
		super(id, components);
		initSimpleMotorDeviceEventListener();
	}
	
	protected void initSimpleMotorDeviceEventListener() {
		simpleMotorDeviceEventListener = new SimpleMotorDeviceEventListener();
		for(Map.Entry<String,DeviceComponent> entry : components.entrySet()) {
			entry.getValue().addEventListener(simpleMotorDeviceEventListener);
		}
	}
	
	public Object getValue() {
		Map<String,Object> value = new HashMap<String,Object>();
		value.put(VALUE_KEY_POSITION, getPosition());
		value.put(VALUE_KEY_SET_POINT, getSetPoint());
		value.put(VALUE_KEY_MOVING, isMoving());
		return value;
	}		
	
	@SuppressWarnings("unchecked")
	public void setValue(Object value) {
		try {
			Map<String,Object> valueMap = (Map<String,Object>) value;
			
			if(valueMap.containsKey(VALUE_KEY_SET_POINT)) {
				try {
					setSetPoint((Double)valueMap.get(VALUE_KEY_SET_POINT));
				}
				catch(ClassCastException e) {
					log.warn("Set value for '" + VALUE_KEY_SET_POINT +"', is not expected class (Double).");
				}
			}
		}
		catch(ClassCastException e) {
			log.warn("Set value argument is not expected class (Map<String,Object>).");
		}
	}
	
	public Double getPosition() {
		DeviceComponent deviceComponent = getComponent(COMPONENT_KEY_POSITION);
		Object value = deviceComponent.getValue();
		return getPosition(value);
	}
	
	public Double getSetPoint() {
		DeviceComponent deviceComponent = getComponent(COMPONENT_KEY_SET_POINT);
		Object value = deviceComponent.getValue();
		return getSetPoint(value);
	}
	
	public Boolean isMoving() {
		DeviceComponent deviceComponent = getComponent(COMPONENT_KEY_STATUS);
		Object value = deviceComponent.getValue();
		return isMoving(value);
	}
	
	public void setSetPoint(Double setPoint) {
		DeviceComponent deviceComponent = getComponent(COMPONENT_KEY_SET_POINT);
		double[] value = new double[] { setPoint }; 
		deviceComponent.setValue(value);
	}
	
	protected Double getPosition(Object value) {
		try {
			double[] dblArray = (double[]) value;
			return new Double(dblArray[0]);
		}
		catch (ClassCastException e) {
			log.warn("Value for '" + VALUE_KEY_POSITION + "', is not expected class (double[]).");
			return Double.NaN;
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + VALUE_KEY_POSITION + "', is null (Disconnected EpicsDevice?).");
			return Double.NaN;
		}
	}
	
	protected Double getSetPoint(Object value) {
		try {
			double[] dblArray = (double[]) value;
			return new Double(dblArray[0]);
		}
		catch (ClassCastException e) {
			log.warn("Value for '" + VALUE_KEY_SET_POINT + "', is not expected class (double[]).");
			return Double.NaN;
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + VALUE_KEY_SET_POINT + "', is null (Disconnected EpicsDevice?).");
			return Double.NaN;
		}
	}
	
	protected Boolean isMoving(Object value) {
		try {
			short[] shtArray = (short[]) value;
			return new Boolean(shtArray[0] != 0);
		} 
		catch(ClassCastException e) {
			log.warn("Value for '" + VALUE_KEY_MOVING + "', is not expected class (short[]).");
			return false;
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + VALUE_KEY_MOVING + "', is null (Disconnected EpicsDevice?).");
			return false;
		}
	}
	
	protected String getComponentKeyByDeviceId(String deviceId) {
		for(Map.Entry<String,DeviceComponent> entry : components.entrySet()) {
			if(entry.getValue().getId().equals(deviceId)) {
				return entry.getKey();
			}	
		}
		return new String();
	}
	
	protected class  SimpleMotorDeviceEventListener implements DeviceEventListener {
		
		public SimpleMotorDeviceEventListener() {}

		public void handleEvent(DeviceEvent event) {
			
			String deviceId = event.getDeviceId(); 
			String componentKey = getComponentKeyByDeviceId(deviceId);
			
			DeviceEventType deviceEventType = event.getDeviceEventType();
			
			switch (deviceEventType) {
				case VALUE_CHANGE:
			
					HashMap<String,Serializable> newValueMap = new HashMap<String,Serializable>();
					
					if(COMPONENT_KEY_POSITION.equals(componentKey)) {
						Object value = event.getValue();
						newValueMap.put(VALUE_KEY_POSITION, getPosition(value));
					} 
					else if(COMPONENT_KEY_SET_POINT.equals(componentKey)) {
						Object value = event.getValue();
						newValueMap.put(VALUE_KEY_SET_POINT, getSetPoint(value));
					} 
					else if(COMPONENT_KEY_STATUS.equals(componentKey)) {
						Object value = event.getValue();
						newValueMap.put(VALUE_KEY_MOVING, isMoving(value));
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
}
