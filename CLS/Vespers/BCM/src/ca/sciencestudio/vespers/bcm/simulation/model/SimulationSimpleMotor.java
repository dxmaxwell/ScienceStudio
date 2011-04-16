/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		SimulationSimpleMotor class.
 *     
 */
package ca.sciencestudio.vespers.bcm.simulation.model;

import java.lang.Math;
import java.lang.Thread;
import java.lang.Runnable;
import java.util.Map;
import java.util.HashMap;

import ca.sciencestudio.device.control.simulation.SimulationEvent;
import ca.sciencestudio.device.control.simulation.SimulationEventType;
import ca.sciencestudio.device.control.simulation.AbstractSimulationDevice;

/**
 * @author maxweld
 *
 */
public class SimulationSimpleMotor extends AbstractSimulationDevice implements Runnable {
	
	public static final String VALUE_KEY_POSITION = "position";
	public static final String VALUE_KEY_SETPOINT = "setpoint";
	public static final String VALUE_KEY_STATUS = "status";
	public static final String VALUE_KEY_STATUS_VALUES = "statusValues";
	
	public static enum SimulationSimpleMotorStatus {
		STOPPED, MOVING
	}
	
	private int simulationUpdateInterval = 2000; // milliseconds //
	private Thread simulationSimpleMotorThread = new Thread(this);
	
	private double position = 0.0;  
	private double setpoint = 0.0; 
	private double velocity = 1.0; // per millisecond //
	private int status = SimulationSimpleMotorStatus.STOPPED.ordinal();
	
	public SimulationSimpleMotor(String id) {
		super(id);
		simulationSimpleMotorThread.setDaemon(true);
		simulationSimpleMotorThread.start();
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
		updateSetpoint(setpoint);
	}

	public double getVelocity() {
		return velocity;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}
	
	public int getSimulationUpdateInterval() {
		return simulationUpdateInterval;
	}

	public void setSimulationUpdateInterval(int simulationUpdateInterval) {
		this.simulationUpdateInterval = simulationUpdateInterval;
	}

	public int getStatus() {
		return status;
	}

	public String[] getStatusValues() {
		SimulationSimpleMotorStatus[] statusValues = SimulationSimpleMotorStatus.values();
		String[] statusValueNames = new String[statusValues.length];
		for(int index=0; index<statusValues.length; index++) {
			statusValueNames[index] = statusValues[index].name();
		}
		return statusValueNames;
	}
	
	protected void updateSetpoint(double setpoint) {
		
		if(this.setpoint != setpoint) {
			
			this.setpoint = setpoint;
			
			Map<String,Object> value = new HashMap<String,Object>();	
			value.put(VALUE_KEY_SETPOINT, getSetpoint());		
		
			if(setpoint == position) {
				if(status == SimulationSimpleMotorStatus.MOVING.ordinal()) {
					status = SimulationSimpleMotorStatus.STOPPED.ordinal();
					value.put(VALUE_KEY_STATUS, getStatus());
					value.put(VALUE_KEY_STATUS_VALUES, getStatusValues());
				}
			} else {
				if(status == SimulationSimpleMotorStatus.STOPPED.ordinal()) {
					status = SimulationSimpleMotorStatus.MOVING.ordinal();
					value.put(VALUE_KEY_STATUS, getStatus());
					value.put(VALUE_KEY_STATUS_VALUES, getStatusValues());
				}
			}
					
			SimulationEvent event = new SimulationEvent(SimulationEventType.VALUE_CHANGE, id, value);
			publishEvent(event);
		}
	}
	
	protected void updatePositionWithVelocity(double velocity) {
		
		Map<String,Object> value = new HashMap<String,Object>();
		
		double delta = Math.abs(setpoint - position);
		double direction = (setpoint >= position) ? 1.0 : -1.0;
		
		if(delta > 0.0) {
			position += direction * Math.min(delta, velocity);
			value.put(VALUE_KEY_POSITION, getPosition());
		}
		
		if(setpoint == position) {
			if(status != SimulationSimpleMotorStatus.STOPPED.ordinal()) {
				status = SimulationSimpleMotorStatus.STOPPED.ordinal();
				value.put(VALUE_KEY_STATUS, getStatus());
				value.put(VALUE_KEY_STATUS_VALUES, getStatusValues());
			}
		}
		
		if(!value.isEmpty()) {
			SimulationEvent event = new SimulationEvent(SimulationEventType.VALUE_CHANGE, id, value);
			publishEvent(event);
		}
	}
	
	public void run() {
		
		while(true) {
			
			updatePositionWithVelocity(velocity * simulationUpdateInterval);
			
			try { 
				Thread.sleep(simulationUpdateInterval); 
			}
			catch(InterruptedException e) { 
				break;
			}
		}	
	}
}
