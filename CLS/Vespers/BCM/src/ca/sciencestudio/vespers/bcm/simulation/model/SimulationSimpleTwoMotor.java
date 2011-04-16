/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		SimulationSimpleTwoMotor class.
 *     
 */
package ca.sciencestudio.vespers.bcm.simulation.model;

import java.util.HashMap;
import java.util.Map;

import ca.sciencestudio.device.control.simulation.SimulationDevice;
import ca.sciencestudio.device.control.simulation.SimulationEvent;
import ca.sciencestudio.device.control.simulation.SimulationEventListener;
import ca.sciencestudio.device.control.simulation.SimulationEventType;
import ca.sciencestudio.device.control.simulation.AbstractSimulationDevice;

/**
 * @author maxweld
 *
 */
public class SimulationSimpleTwoMotor extends AbstractSimulationDevice implements SimulationEventListener {

	public static final String VALUE_KEY_POSITION = "position";
	public static final String VALUE_KEY_SETPOINT = "setpoint";
	public static final String VALUE_KEY_STATUS = "status";
	public static final String VALUE_KEY_STATUS_VALUES = "statusValues";
	
	private SimulationDevice motorA;
	private SimulationDevice motorB;
	
	private double position;
	private double setpoint;
	private int status;
	private String[] statusValues;
	
	private double offsetA = 0.0;
	private double offsetB = 0.0;
	private double cosineA = Math.sqrt(0.5); // 0.7071.. //
	private double cosineB = Math.sqrt(0.5); // 0.7071.. //
	
	public SimulationSimpleTwoMotor(String id, SimulationDevice motorA, SimulationDevice motorB) {
		super(id);
		this.motorA = motorA;
		this.motorB = motorB;
		this.motorA.addEventListener(this);
		this.motorB.addEventListener(this);
		update();
	}
	
	public Object getValue() {
		Map<String,Object> value = new HashMap<String,Object>();
		value.put(VALUE_KEY_POSITION, getPosition());
		value.put(VALUE_KEY_SETPOINT, getSetpoint());
		value.put(VALUE_KEY_STATUS, getStatus());
		value.put(VALUE_KEY_STATUS_VALUES, getStatusValues());
		return value;
	}

	public void setValue(Object value) {
		
		Map<String,Object> valueMap = getMapFromObject(value);
		
		if(valueMap.containsKey(VALUE_KEY_SETPOINT)) {
			try {
				setSetpoint((Double)valueMap.get(VALUE_KEY_SETPOINT));
			}
			catch(ClassCastException e) {
				log.warn("Value for key, " + VALUE_KEY_SETPOINT + ", is unexpected class.");
			}
		}
	}

	public double getPosition() {
		return position;
	}
	
	public double getSetpoint() {
		return setpoint;
	}

	public void setSetpoint(double setpoint) {
		setSetpointA(setpoint);
		setSetpointB(setpoint);
	}

	public int getStatus() {
		return status;
	}

	public String[] getStatusValues() {
		return statusValues;
	}
	
	public double getOffsetA() {
		return offsetA;
	}

	public void setOffsetA(double offsetA) {
		this.offsetA = offsetA;
		update();
	}

	public double getOffsetB() {
		return offsetB;
	}

	public void setOffsetB(double offsetB) {
		this.offsetB = offsetB;
		update();
	}

	public double getCosineA() {
		return cosineA;
	}

	public void setCosineA(double cosineA) {
		this.cosineA = cosineA;
		update();
	}

	public double getCosineB() {
		return cosineB;
	}

	public void setCosineB(double cosineB) {
		this.cosineB = cosineB;
		update();
	}

	protected void setSetpointA(double setpoint) {
		Map<String,Object> valueMap = new HashMap<String,Object>();
		valueMap.put(VALUE_KEY_SETPOINT, computeA(setpoint));
		motorA.setValue(valueMap);
	}
	
	protected void setSetpointB(double setpoint) {
		Map<String,Object> valueMap = new HashMap<String,Object>();
		valueMap.put(VALUE_KEY_SETPOINT, computeB(setpoint));
		motorB.setValue(valueMap);	
	}
	
	protected void update() {
		Map<String,Object> valueMapA = getMapFromObject(motorA.getValue());
		Map<String,Object> valueMapB = getMapFromObject(motorB.getValue());
		updatePositionWithValueMaps(valueMapA, valueMapB);
		updateSetpointWithValueMaps(valueMapA, valueMapB);
		updateStatusWithValueMaps(valueMapA, valueMapB);
	}
	
	protected void updatePositionWithValueMaps(Map<String,Object> valueMapA, Map<String,Object> valueMapB) {
		try {
			if(valueMapA.containsKey(VALUE_KEY_POSITION) && valueMapB.containsKey(VALUE_KEY_POSITION)) {
				double positionA = (Double)valueMapA.get(VALUE_KEY_POSITION);
				double positionB = (Double)valueMapB.get(VALUE_KEY_POSITION);
				this.position = computeS(positionA, positionB);
			}
			else {
				log.warn("Map does not contain key: " + VALUE_KEY_POSITION);
			}
		}
		catch(ClassCastException e) {
			log.warn("Value for key, " + VALUE_KEY_POSITION + ", is unexpected class.");
		}
	}
	
	protected void updateSetpointWithValueMaps(Map<String,Object> valueMapA, Map<String,Object> valueMapB) {
		try {
			if(valueMapA.containsKey(VALUE_KEY_SETPOINT) && valueMapB.containsKey(VALUE_KEY_SETPOINT)) {
				double setpointA = (Double)valueMapA.get(VALUE_KEY_SETPOINT);
				double setpointB = (Double)valueMapB.get(VALUE_KEY_SETPOINT);
				this.setpoint = computeS(setpointA, setpointB);
			}
			else {
				log.warn("Map does not contain key: " + VALUE_KEY_SETPOINT);
			}
		}
		catch(ClassCastException e) {
			log.warn("Value for key, " + VALUE_KEY_SETPOINT + ", is unexpected class.");
		}
	}
	
	protected void updateStatusWithValueMaps(Map<String,Object> valueMapA, Map<String,Object> valueMapB) {
		try {
			if(valueMapA.containsKey(VALUE_KEY_STATUS) && valueMapB.containsKey(VALUE_KEY_STATUS)) {
				int statusA = (Integer)valueMapA.get(VALUE_KEY_STATUS);
				int statusB = (Integer)valueMapB.get(VALUE_KEY_STATUS);
				this.status = Math.max(statusA, statusB);
			}
			else {
				log.warn("Map does not contain key: " + VALUE_KEY_STATUS);
			}
		}
		catch(ClassCastException e) {
			log.warn("Value for key, " + VALUE_KEY_STATUS + ", is unexpected class.");
		}
		
		try {
			if(valueMapA.containsKey(VALUE_KEY_STATUS_VALUES) || valueMapB.containsKey(VALUE_KEY_STATUS_VALUES)) {
				String[] statusValues = (String[])valueMapA.get(VALUE_KEY_STATUS_VALUES);
				if(statusValues == null) {
					statusValues = (String[])valueMapB.get(VALUE_KEY_STATUS_VALUES);
				}
				this.statusValues = statusValues;
			}
			else {
				log.warn("Map does not contain key: " + VALUE_KEY_STATUS_VALUES);
			}
		}
		catch(ClassCastException e) {
			log.warn("Value for key, " + VALUE_KEY_STATUS_VALUES+ ", is unexpected class.");
		}
	}
	
	protected double computeS(double a, double b) {
		return ((((a / cosineA) - offsetA) + ((b / cosineB) - offsetB)) / 2.0);
	}
	
	protected double computeA(double s) {
		return ((s + offsetA) * cosineA);
	}
	
	protected double computeB(double s) {
		return ((s + offsetB) * cosineB);
	}
	
	public void handleEvent(SimulationEvent event) {
		
		String deviceId = event.getDeviceId();
		Map<String,Object> newValueMap = new HashMap<String,Object>();
		
		if(motorA.getId().equals(deviceId)) {
			Map<String,Object> valueMapA = getMapFromObject(event.getValue());
			Map<String,Object> valueMapB = getMapFromObject(motorB.getValue());
			
			if(valueMapA.containsKey(VALUE_KEY_POSITION)) {
				updatePositionWithValueMaps(valueMapA, valueMapB);
				newValueMap.put(VALUE_KEY_POSITION, getPosition());
			}
			
			if(valueMapA.containsKey(VALUE_KEY_SETPOINT)) {
				updateSetpointWithValueMaps(valueMapA, valueMapB);
				newValueMap.put(VALUE_KEY_SETPOINT, getSetpoint());
			}
			
			if(valueMapA.containsKey(VALUE_KEY_STATUS)) {
				updateStatusWithValueMaps(valueMapA, valueMapB);
				newValueMap.put(VALUE_KEY_STATUS, getStatus());
				newValueMap.put(VALUE_KEY_STATUS_VALUES, getStatusValues());
			}
		}

		if(motorB.getId().equals(deviceId)) {
			Map<String,Object> valueMapA = getMapFromObject(motorA.getValue());
			Map<String,Object> valueMapB = getMapFromObject(event.getValue());
			
			if(valueMapB.containsKey(VALUE_KEY_POSITION)) {
				updatePositionWithValueMaps(valueMapA, valueMapB);
				newValueMap.put(VALUE_KEY_POSITION, getPosition());
			}
			
			if(valueMapB.containsKey(VALUE_KEY_SETPOINT)) {
				updateSetpointWithValueMaps(valueMapA, valueMapB);
				newValueMap.put(VALUE_KEY_SETPOINT, getSetpoint());
			}
			
			if(valueMapB.containsKey(VALUE_KEY_STATUS)) {
				updateStatusWithValueMaps(valueMapA, valueMapB);
				newValueMap.put(VALUE_KEY_STATUS, getStatus());
				newValueMap.put(VALUE_KEY_STATUS_VALUES, getStatusValues());
			}
		}
		
		if(!newValueMap.isEmpty()) {
			SimulationEvent simulationEvent = new SimulationEvent(SimulationEventType.VALUE_CHANGE, id, newValueMap);
			publishEvent(simulationEvent);
		}
		
	}
}
