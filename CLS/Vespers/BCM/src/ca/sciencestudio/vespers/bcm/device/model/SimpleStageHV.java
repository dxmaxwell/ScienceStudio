/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		SimpleStageHV class.
 *     
 */
package ca.sciencestudio.vespers.bcm.device.model;

import java.util.Map;
import java.util.HashMap;

import ca.sciencestudio.device.control.composite.DeviceComposite;
import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.event.DeviceEventListener;
import ca.sciencestudio.device.control.event.DeviceEventType;

/**
 * @author maxweld
 *
 */
public class SimpleStageHV extends DeviceComposite<SimpleMotor> {

	protected static final String COMPONENT_KEY_MOTOR_H = "motorH";
	protected static final String COMPONENT_KEY_MOTOR_V = "motorV";
	
	public static final String VALUE_KEY_POSITION_H = "positionH";
	public static final String VALUE_KEY_POSITION_V = "positionV";
	public static final String VALUE_KEY_SET_POINT_H = "setPointH";
	public static final String VALUE_KEY_SET_POINT_V = "setPointV";
	public static final String VALUE_KEY_MOVING = "moving";
	
	private SimpleStageHVDeviceEventListener simpleStageHVDeviceEventListener;
	
	public SimpleStageHV(String id, Map<String,SimpleMotor> components) {
		super(id, components);
		initSimpleStageHVDeviceEventListener();
	}
	
	protected void initSimpleStageHVDeviceEventListener() {
		simpleStageHVDeviceEventListener = new SimpleStageHVDeviceEventListener();
		for(Map.Entry<String,SimpleMotor> entry : getComponents().entrySet()) {
			entry.getValue().addEventListener(simpleStageHVDeviceEventListener);
		}
	}
	
	public Object getValue() {
		Map<String,Object> value = new HashMap<String,Object>();
		value.put(VALUE_KEY_POSITION_H, getPositionH());
		value.put(VALUE_KEY_POSITION_V, getPositionV());
		value.put(VALUE_KEY_SET_POINT_H, getSetPointH());
		value.put(VALUE_KEY_SET_POINT_V, getSetPointV());
		value.put(VALUE_KEY_MOVING, isMoving());
		return value;
	}
	
	@SuppressWarnings("unchecked")
	public void setValue(Object value) {
		try {
			Map<String,Object> valueMap = (Map<String,Object>) value;
		
			if(valueMap.containsKey(VALUE_KEY_SET_POINT_H)) {
				try {
					setSetPointH((Double)valueMap.get(VALUE_KEY_SET_POINT_H));
				}
				catch(ClassCastException e) {
					log.warn("Set value for '" + VALUE_KEY_SET_POINT_H + "', is not expected class (Double).");
				}
			}
			
			if(valueMap.containsKey(VALUE_KEY_SET_POINT_V)) {
				try {
					setSetPointV((Double)valueMap.get(VALUE_KEY_SET_POINT_V));
				}
				catch(ClassCastException e) {
					log.warn("Set value for '" + VALUE_KEY_SET_POINT_V + "', is not expected class (Double).");
				}
			}
		}
		catch(ClassCastException e) {
			log.warn("Set value argument is not expected class (Map<String,Object>).");
		}
	}
	
	public double getPositionH() {
		SimpleMotor simpleMotor = getComponent(COMPONENT_KEY_MOTOR_H);
		return simpleMotor.getPosition();
	}
	
	public double getPositionV() {
		SimpleMotor simpleMotor = getComponent(COMPONENT_KEY_MOTOR_V);
		return simpleMotor.getPosition();
	}
	
	public double getSetPointH() {
		SimpleMotor simpleMotor = getComponent(COMPONENT_KEY_MOTOR_H);
		return simpleMotor.getSetPoint();
	}
	
	public double getSetPointV() {
		SimpleMotor simpleMotor = getComponent(COMPONENT_KEY_MOTOR_V);
		return simpleMotor.getSetPoint();
	}
	
	public boolean isMoving() {
		SimpleMotor simpleMotorH = getComponent(COMPONENT_KEY_MOTOR_H);
		SimpleMotor simpleMotorV = getComponent(COMPONENT_KEY_MOTOR_V);
		return isMoving(simpleMotorH.isMoving(), simpleMotorV.isMoving());
	}
	
	public void setSetPointH(double setPointH) {
		SimpleMotor simpleMotor = getComponent(COMPONENT_KEY_MOTOR_H);
		simpleMotor.setSetPoint(setPointH);
	}
	
	public void setSetPointV(double setPointV) {
		SimpleMotor simpleMotor = getComponent(COMPONENT_KEY_MOTOR_V);
		simpleMotor.setSetPoint(setPointV);
	}
	
	protected boolean isMoving(boolean movingH, boolean movingV) {
		return (movingH || movingV);
	}
	
	protected String getComponentKeyByDeviceId(String deviceId) {
		for(Map.Entry<String,SimpleMotor> entry : components.entrySet()) {
			if(entry.getValue().getId().equals(deviceId)) {
				return entry.getKey();
			}	
		}
		return new String();
	}
	
	protected class SimpleStageHVDeviceEventListener implements DeviceEventListener  {

		public SimpleStageHVDeviceEventListener() {}

		@SuppressWarnings("unchecked")
		public void handleEvent(DeviceEvent event) {
			
			String deviceId = event.getDeviceId();
			String componentKey = getComponentKeyByDeviceId(deviceId);
			
			DeviceEventType deviceEventType = event.getDeviceEventType();
			
			switch (deviceEventType) {
				case VALUE_CHANGE:
					try {
						Map<String,Object> valueMap = (Map<String,Object>) event.getValue();
						HashMap<String,Object> newValueMap = new HashMap<String, Object>();
						
						if(COMPONENT_KEY_MOTOR_H.equals(componentKey)) {
							if(valueMap.containsKey(SimpleMotor.VALUE_KEY_POSITION)) {
								Object value = valueMap.get(SimpleMotor.VALUE_KEY_POSITION);
								newValueMap.put(VALUE_KEY_POSITION_H, value);	
							}
							else if(valueMap.containsKey(SimpleMotor.VALUE_KEY_SET_POINT)) {
								Object value = valueMap.get(SimpleMotor.VALUE_KEY_SET_POINT);
								newValueMap.put(VALUE_KEY_SET_POINT_H, value);
							}
							else if(valueMap.containsKey(SimpleMotor.VALUE_KEY_MOVING)) {
								try {
									Boolean movingH = (Boolean) valueMap.get(SimpleMotor.VALUE_KEY_MOVING);
									SimpleMotor motorV = getComponent(COMPONENT_KEY_MOTOR_V);									
									newValueMap.put(VALUE_KEY_MOVING, isMoving(movingH, motorV.isMoving()));
								}
								catch(ClassCastException e) {
									log.warn(COMPONENT_KEY_MOTOR_H + ": Value for '" + VALUE_KEY_MOVING + "', is not expected class (Boolean).");								
								}
							}
						} 
						else if(COMPONENT_KEY_MOTOR_V.equals(componentKey)) {
							if(valueMap.containsKey(SimpleMotor.VALUE_KEY_POSITION)) {
								Object value = valueMap.get(SimpleMotor.VALUE_KEY_POSITION);
								newValueMap.put(VALUE_KEY_POSITION_V, value);
							}
							else if(valueMap.containsKey(SimpleMotor.VALUE_KEY_SET_POINT)) {
								Object value = valueMap.get(SimpleMotor.VALUE_KEY_SET_POINT);
								newValueMap.put(VALUE_KEY_SET_POINT_V, value);
							}
							else if(valueMap.containsKey(SimpleMotor.VALUE_KEY_MOVING)) {
								try {
									SimpleMotor motorH = getComponent(COMPONENT_KEY_MOTOR_H);
									Boolean movingV = (Boolean) valueMap.get(SimpleMotor.VALUE_KEY_MOVING);
									newValueMap.put(VALUE_KEY_MOVING, isMoving(motorH.isMoving(), movingV));
								}
								catch(ClassCastException e) {
									log.warn(COMPONENT_KEY_MOTOR_V + ": Value for '" + VALUE_KEY_MOVING + "', is not expected class (Boolean).");
								}
							}
						}
						
						if(!newValueMap.isEmpty()) {
							publishEvent(new DeviceEvent(DeviceEventType.VALUE_CHANGE, id, newValueMap, status, alarm));
						}
					}
					catch(ClassCastException e) {
						log.warn("Device event value is not expected class (Map<String,Object>).");
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
