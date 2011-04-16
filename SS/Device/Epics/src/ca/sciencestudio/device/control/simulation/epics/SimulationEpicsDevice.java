/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		SimulationEpicsDevice class.
 *     
 */
package ca.sciencestudio.device.control.simulation.epics;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.HashMap;


import ca.sciencestudio.device.control.DeviceStatus;
import ca.sciencestudio.device.control.DeviceType;
import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.event.DeviceEventType;
import ca.sciencestudio.device.control.component.DeviceComponent;
import ca.sciencestudio.device.control.simulation.SimulationDevice;
import ca.sciencestudio.device.control.simulation.SimulationEvent;
import ca.sciencestudio.device.control.simulation.SimulationEventListener;

/**
 * @author maxweld
 *
 */
public class SimulationEpicsDevice extends DeviceComponent implements SimulationEventListener {
	
	protected Class<?> epicsValueClass;
	protected String simulationValueKey;
	protected Class<?> simulationValueClass;
	protected SimulationDevice simulationDevice;
	
	public SimulationEpicsDevice(Class<?> epicsValueClass, String id, String simulationValueKey, SimulationDevice simulationDevice) {
		super(id);
		deviceType = DeviceType.Simple;
		status = DeviceStatus.NORMAL;
		alarm.setStatus("N/A");
		alarm.setSeverity("N/A");
		this.epicsValueClass = epicsValueClass;
		this.simulationValueKey = simulationValueKey;
		this.simulationValueClass = epicsValueClass;
		this.simulationDevice = simulationDevice;
		this.simulationDevice.addEventListener(this);
		initSimulationValueClass();
	}
	
	private void initSimulationValueClass() {
		Map<String,Object> valueMap = getMapFromObject(simulationDevice.getValue());
		if(valueMap.containsKey(simulationValueKey)) {
			Object simulationValue = valueMap.get(simulationValueKey);
			simulationValueClass = simulationValue.getClass();
		}
		else {
			log.warn("Could not initialize simulation value class, using: " + simulationValueClass.getSimpleName());
		}
	}
	
	public Object getValue() {
		Object value = simulationDevice.getValue();
		Map<String,Object> simulationValueMap = getMapFromObject(value);
		Object simulationValue = simulationValueMap.get(simulationValueKey);
		Object epicsValue = getEpicsValueFromSimulationValue(simulationValue);
		if(epicsValue == null) {
			epicsValue = getEpicsValueFromSimulationValue("0");
		}
		return epicsValue; 
	}
	
	public void setValue(Object epicsValue) {
		Object simulationValue = getSimulationValueFromEpicsValue(epicsValue);
		if(simulationValue != null) {
			Map<String,Object> simulationValueMap = new HashMap<String,Object>();			
			simulationValueMap.put(simulationValueKey, simulationValue);
			simulationDevice.setValue(simulationValueMap);
		}
	}
	
	@SuppressWarnings("unchecked")
	private Map<String,Object> getMapFromObject(Object obj) {
		try {
			return (Map<String,Object>)obj;
		}
		catch(ClassCastException e) {
			log.warn("Object is not the expected class: Map<String,Object>.");
			return new HashMap<String,Object>();
		}
	}
	
	protected Object getEpicsValueFromSimulationValue(Object simulationValue) {

		if(simulationValue == null) {
			log.warn("Simulation value is null. Check the simulation value key?");
			return null;
		}
	
		Class<?> simulationValueClass = simulationValue.getClass(); 
		Class<?> epicsValueComponentType = epicsValueClass.getComponentType();
		
		if(simulationValueClass == epicsValueClass) {
			return simulationValue;
		}
		 
		Object epicsValueComponent = coerceValueToClass(simulationValue, epicsValueComponentType);
		Object epicsValue = Array.newInstance(epicsValueComponentType, 1);
		Array.set(epicsValue, 0, epicsValueComponent);
		return epicsValue;
	}
	
	protected Object getSimulationValueFromEpicsValue(Object epicsValue) {
		
		if(epicsValue == null) {
			log.warn("Epics value is null. Check the device configuration?");
			return null;
		}
		
		Class<?> epicsValueClass = epicsValue.getClass();
		
		if(epicsValueClass != this.epicsValueClass) {
			log.warn("Epics value is unexpected class: " + epicsValue.getClass().getSimpleName());
			return null;
		}

		if(!epicsValueClass.isArray()) {
			log.warn("Epics value is not an array as expected.");
			return null;
		}
		
		 if(Array.getLength(epicsValue) == 0) {
			log.warn("Epics value is array with length of zero.");
			return null;
		}
		
		if(epicsValueClass == simulationValueClass) {
			return epicsValue;
		}
	
		Object simulationValue = Array.get(epicsValue, 0);
		return coerceValueToClass(simulationValue, simulationValueClass);
	}
	
	protected Object coerceValueToClass(Object value, Class<?> clazz) {
		
		if((value == null) || (clazz == null)) { 
			return null; 
		}
		
		if(value.getClass() == clazz) {
			return value;
		}
		
		if(value instanceof Number) {
			return coerceNumberToClass((Number)value, clazz);
		}
		
		if(value instanceof String) {
			return coerceStringToClass((String)value, clazz);
		}
		
		log.warn("Cannot coerce value of class: " + value.getClass().getSimpleName());
		return null;
	}
	
	protected Object coerceNumberToClass(Number value, Class<?> clazz) {
		
		if((clazz == double.class) || (clazz == Double.class)) {
			return value.doubleValue();
		}
		else if((clazz == float.class) || (clazz == Float.class)) {
			return value.floatValue();
		}
		else if((clazz == int.class) || (clazz == Integer.class)) {
			return value.intValue();
		}
		else if((clazz == short.class) || (clazz == Short.class)) {
			return value.shortValue();
		}
		else if((clazz == byte.class) || (clazz == Byte.class)) {
			return value.byteValue();
		}
		else if(clazz == String.class) {
			return value.toString();
		}
		else {
			log.warn("Cannot coerce Number to class: " + clazz.getSimpleName());
			return null;
		}	
	}
	
	protected Object coerceStringToClass(String value, Class<?> clazz) {
		
		if((clazz == double.class) || (clazz == Double.class)) {
			 return new Double(value);
		}
		else if((clazz == float.class) || (clazz == Float.class)) {
			return new Float(value);
		}
		else if((clazz == int.class) || (clazz == Integer.class)) {
			return new Integer(value);
		}
		else if((clazz == short.class) || (clazz == Short.class)) {
			return new Short(value);
		}
		else if((clazz == byte.class) || (clazz == Byte.class)) {
			return new Byte(value);
		}
		else if(clazz == String.class) {
			return value;
		}
		else {
			log.warn("Cannot coerce String to class: " + clazz.getSimpleName());
			return null;
		}
	}
	
	public void handleEvent(SimulationEvent event) {
		
		Object value = event.getValue();
		Map<String,Object> valueMap = getMapFromObject(value);
		
		if(valueMap.containsKey(simulationValueKey)) {
			Object simulationValue = valueMap.get(simulationValueKey);
			Object epicsValue = getEpicsValueFromSimulationValue(simulationValue);
			if(epicsValue != null) {
				DeviceEvent deviceEvent = new DeviceEvent(DeviceEventType.VALUE_CHANGE, id, (Serializable)epicsValue, status, alarm);
				publishEvent(deviceEvent);
			}
		}
	}
}
