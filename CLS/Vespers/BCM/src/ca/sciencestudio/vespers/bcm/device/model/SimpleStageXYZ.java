/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		SimpleStageXYZ class.
 *     
 */
package ca.sciencestudio.vespers.bcm.device.model;

import java.util.Map;
import java.util.HashMap;

import ca.sciencestudio.device.control.composite.DeviceComposite;
import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.event.DeviceEventListener;
import ca.sciencestudio.device.control.event.DeviceEventType;

import ca.sciencestudio.vespers.bcm.device.model.SimpleMotor;

/**
 * @author maxweld
 *
 */
public class SimpleStageXYZ extends DeviceComposite<SimpleMotor> {

	private static final String COMPONENT_KEY_MOTOR_X = "motorX";
	private static final String COMPONENT_KEY_MOTOR_Y = "motorY";
	private static final String COMPONENT_KEY_MOTOR_Z = "motorZ";
	
	public static final String VALUE_KEY_POSITION_X = "positionX";
	public static final String VALUE_KEY_POSITION_Y = "positionY";
	public static final String VALUE_KEY_POSITION_Z = "positionZ";
	public static final String VALUE_KEY_SET_POINT_X = "setPointX";
	public static final String VALUE_KEY_SET_POINT_Y = "setPointY";
	public static final String VALUE_KEY_SET_POINT_Z = "setPointZ";
	public static final String VALUE_KEY_MOVING = "moving";
	
	private SimpleStageXYZDeviceEventListener simpleStageXYZDeviceEventListener;
	
	public SimpleStageXYZ(String id, Map<String,SimpleMotor> map) {
		super(id, map);
		initSimpleStageXYZDeviceEventListener();
	}

	protected void initSimpleStageXYZDeviceEventListener() {
		simpleStageXYZDeviceEventListener = new SimpleStageXYZDeviceEventListener();
		for(Map.Entry<String,SimpleMotor> entry : components.entrySet()) {
			entry.getValue().addEventListener(simpleStageXYZDeviceEventListener);
		}
	}
	
	public Object getValue() {
		Map<String,Object> valueMap = new HashMap<String,Object>();
		valueMap.put(VALUE_KEY_POSITION_X, getPositionX());
		valueMap.put(VALUE_KEY_POSITION_Y, getPositionY());
		valueMap.put(VALUE_KEY_POSITION_Z, getPositionZ());
		valueMap.put(VALUE_KEY_SET_POINT_X, getSetPointX());
		valueMap.put(VALUE_KEY_SET_POINT_Y, getSetPointY());
		valueMap.put(VALUE_KEY_SET_POINT_Z, getSetPointZ());
		valueMap.put(VALUE_KEY_MOVING, isMoving());
		return valueMap;
	}
	
	@SuppressWarnings("unchecked")
	public void setValue(Object value) {
		try {
			Map<String,Object> valueMap = (Map<String,Object>) value;
		
			if(valueMap.containsKey(VALUE_KEY_SET_POINT_X)) {
				try {
					setSetPointX((Double)valueMap.get(VALUE_KEY_SET_POINT_X));
				}
				catch(ClassCastException e) {
					log.warn("Set value for '" + VALUE_KEY_SET_POINT_X + "', is not expected class (Double).");
				}
			}
			
			if(valueMap.containsKey(VALUE_KEY_SET_POINT_Y)) {
				try {
					setSetPointY((Double)valueMap.get(VALUE_KEY_SET_POINT_Y));
				}
				catch(ClassCastException e) {
					log.warn("Set value for '" + VALUE_KEY_SET_POINT_Y + "', is not expected class (Double).");
				}
			}
			
			if(valueMap.containsKey(VALUE_KEY_SET_POINT_Z)) {
				try {
					setSetPointZ((Double)valueMap.get(VALUE_KEY_SET_POINT_Z));
				} catch(ClassCastException e) {
					log.warn("set value for '" + VALUE_KEY_SET_POINT_Z + "', is not expected class (Double).");
				}
			}
		}
		catch(ClassCastException e) {
			log.warn("Set value argument is not expected class (Map<String,Object>).");
		}
	}
		
	public Double getPositionX() {
		SimpleMotor simpleMotor = getComponent(COMPONENT_KEY_MOTOR_X);
		return simpleMotor.getPosition();
	}
	
	public Double getPositionY() {
		SimpleMotor simpleMotor = getComponent(COMPONENT_KEY_MOTOR_Y);
		return simpleMotor.getPosition();
	}
	
	public Double getPositionZ() {
		SimpleMotor simpleMotor = getComponent(COMPONENT_KEY_MOTOR_Z);
		return simpleMotor.getPosition();
	}
	
	public Double getSetPointX() {
		SimpleMotor simpleMotor = getComponent(COMPONENT_KEY_MOTOR_X);
		return simpleMotor.getSetPoint();
	}
	
	public Double getSetPointY() {
		SimpleMotor simpleMotor = getComponent(COMPONENT_KEY_MOTOR_Y);
		return simpleMotor.getSetPoint();
	}
	
	public Double getSetPointZ() {
		SimpleMotor simpleMotor = getComponent(COMPONENT_KEY_MOTOR_Z);
		return simpleMotor.getSetPoint();
	}
	
	public Boolean isMoving() {
		SimpleMotor simpleMotorX = getComponent(COMPONENT_KEY_MOTOR_X);
		SimpleMotor simpleMotorY = getComponent(COMPONENT_KEY_MOTOR_Y);
		SimpleMotor simpleMotorZ = getComponent(COMPONENT_KEY_MOTOR_Z);
		return isMoving(simpleMotorX.isMoving(), simpleMotorY.isMoving(), simpleMotorZ.isMoving());
	}
	
	public void setSetPointX(Double setPointX) {
		SimpleMotor motorX = getComponent(COMPONENT_KEY_MOTOR_X);
		motorX.setSetPoint(setPointX);
	}
	
	public void setSetPointY(Double setPointY) {
		SimpleMotor motorX = getComponent(COMPONENT_KEY_MOTOR_Y);
		motorX.setSetPoint(setPointY);
	}
	
	public void setSetPointZ(Double setPointZ) {
		SimpleMotor motorX = getComponent(COMPONENT_KEY_MOTOR_Z);
		motorX.setSetPoint(setPointZ);
	}
	
	protected Boolean isMoving(Boolean isMovingX, Boolean isMovingY, Boolean isMovingZ) {
		return (isMovingX || isMovingY || isMovingZ);
	}
	
	protected String getComponentKeyByDeviceId(String deviceId) {
		for(Map.Entry<String,SimpleMotor> entry : components.entrySet()) {
			if(entry.getValue().getId().equals(deviceId)) {
				return entry.getKey();
			}	
		}
		return new String();
	}
	
	protected class SimpleStageXYZDeviceEventListener implements DeviceEventListener {
		
		public SimpleStageXYZDeviceEventListener() {}

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
						
						if(COMPONENT_KEY_MOTOR_X.equals(componentKey)) {
							if(valueMap.containsKey(SimpleMotor.VALUE_KEY_POSITION)) {
								Object value = valueMap.get(SimpleMotor.VALUE_KEY_POSITION);
								newValueMap.put(VALUE_KEY_POSITION_X, value);	
							}
							else if(valueMap.containsKey(SimpleMotor.VALUE_KEY_SET_POINT)) {
								Object value = valueMap.get(SimpleMotor.VALUE_KEY_SET_POINT);
								newValueMap.put(VALUE_KEY_SET_POINT_X, value);
							}
							else if(valueMap.containsKey(SimpleMotor.VALUE_KEY_MOVING)) {
								try {
									Boolean movingX = (Boolean) valueMap.get(SimpleMotor.VALUE_KEY_MOVING);
									SimpleMotor motorY = getComponent(COMPONENT_KEY_MOTOR_Y);
									Boolean movingY = motorY.isMoving();
									SimpleMotor motorZ = getComponent(COMPONENT_KEY_MOTOR_Z);
									Boolean movingZ = motorZ.isMoving();
									Boolean isMoving = isMoving(movingX, movingY, movingZ);
									//log.info(COMPONENT_KEY_MOTOR_X + " moving: " + movingX + " ("+ COMPONENT_KEY_MOTOR_Y + ":"+ movingY + " " + COMPONENT_KEY_MOTOR_Z + ":" + movingZ + ") Stage moving: " + isMoving);
									newValueMap.put(VALUE_KEY_MOVING, isMoving);
								}
								catch(ClassCastException e) {
									log.warn(COMPONENT_KEY_MOTOR_X + " :Value for '" + VALUE_KEY_MOVING + "', is not expected class (Boolean).");
								}
							}
						} 
						else if(COMPONENT_KEY_MOTOR_Y.equals(componentKey)) {
							if(valueMap.containsKey(SimpleMotor.VALUE_KEY_POSITION)) {
								Object value = valueMap.get(SimpleMotor.VALUE_KEY_POSITION);
								newValueMap.put(VALUE_KEY_POSITION_Y, value);
							}
							else if(valueMap.containsKey(SimpleMotor.VALUE_KEY_SET_POINT)) {
								Object value = valueMap.get(SimpleMotor.VALUE_KEY_SET_POINT);
								newValueMap.put(VALUE_KEY_SET_POINT_Y, value);
							}
							else if(valueMap.containsKey(SimpleMotor.VALUE_KEY_MOVING)) {
								try {
									SimpleMotor motorX = getComponent(COMPONENT_KEY_MOTOR_X);
									Boolean movingX = motorX.isMoving();
									Boolean movingY = (Boolean) valueMap.get(SimpleMotor.VALUE_KEY_MOVING);
									SimpleMotor motorZ = getComponent(COMPONENT_KEY_MOTOR_Z);
									Boolean movingZ = motorZ.isMoving();
									Boolean isMoving = isMoving(movingX, movingY, movingZ);
									//log.info(COMPONENT_KEY_MOTOR_Y + " moving: " + movingY + " ("+ COMPONENT_KEY_MOTOR_X + ":"+ movingX + " " + COMPONENT_KEY_MOTOR_Z + ":" + movingZ + ") Stage moving: " + isMoving);
									newValueMap.put(VALUE_KEY_MOVING, isMoving);
								}
								catch(ClassCastException e) {
									log.warn(COMPONENT_KEY_MOTOR_Y + " :Value for '" + VALUE_KEY_MOVING + "', is not expected class (Boolean).");
								}
							}
						}
						else if(COMPONENT_KEY_MOTOR_Z.equals(componentKey)) {
							if(valueMap.containsKey(SimpleMotor.VALUE_KEY_POSITION)) {
								Object value = valueMap.get(SimpleMotor.VALUE_KEY_POSITION);
								newValueMap.put(VALUE_KEY_POSITION_Z, value);
							}
							else if(valueMap.containsKey(SimpleMotor.VALUE_KEY_SET_POINT)) {
								Object value = valueMap.get(SimpleMotor.VALUE_KEY_SET_POINT);
								newValueMap.put(VALUE_KEY_SET_POINT_Z, value);
							}
							else if(valueMap.containsKey(SimpleMotor.VALUE_KEY_MOVING)) {
								try {
									SimpleMotor motorX = getComponent(COMPONENT_KEY_MOTOR_X);
									Boolean movingX = motorX.isMoving();
									SimpleMotor motorY = getComponent(COMPONENT_KEY_MOTOR_Y);
									Boolean movingY = motorY.isMoving();
									Boolean movingZ = (Boolean) valueMap.get(SimpleMotor.VALUE_KEY_MOVING);
									Boolean isMoving = isMoving(movingX, movingY, movingZ);
									//log.info(COMPONENT_KEY_MOTOR_Z + " moving: " + movingZ + " ("+ COMPONENT_KEY_MOTOR_X + ":"+ movingX + " " + COMPONENT_KEY_MOTOR_Y + ":" + movingY + ") Stage moving: " + isMoving);
									newValueMap.put(VALUE_KEY_MOVING, isMoving);
								}
								catch(ClassCastException e) {
									log.warn(COMPONENT_KEY_MOTOR_Z + " :Value for '" + VALUE_KEY_MOVING + "', is not expected class (Boolean).");
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
