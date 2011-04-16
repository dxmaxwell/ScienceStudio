/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		MultiElementDetector class.
 *     
 */
package ca.sciencestudio.vespers.bcm.device.model;

import java.lang.reflect.Array;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.sciencestudio.device.control.Device;
import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.event.DeviceEventListener;
import ca.sciencestudio.device.control.event.DeviceEventType;
import ca.sciencestudio.device.control.composite.DeviceComposite;

/**
 * @author maxweld
 *
 */
public class MultiElementDetector extends DeviceComposite<Device> implements DeviceEventListener {

	private static final short[] EPICS_ONE = new short[] { 1 }; 
	
	protected static final String[] MED_ACQUIRE_STATES = { "DONE", "ACQUIRING" };
	
	protected static final String COMPONENT_KEY_SPECTRUM_ALL = "spectrumAll";
	protected static final String COMPONENT_KEY_ACQUIRE_START_ALL = "acquireStartAll";
	protected static final String COMPONENT_KEY_ACQUIRE_STOP_ALL = "acquireStopAll";
	protected static final String COMPONENT_KEY_ACQUIRE_ERASE_ALL = "acquireEraseAll";
	protected static final String COMPONENT_KEY_ACQUIRE_ERASE_START_ALL = "acquireEraseStartAll";
	protected static final String COMPONENT_KEY_ACQUIRE_STATE_ALL = "acquireStateAll";
	protected static final String COMPONENT_KEY_ELAPSED_TIME_ALL = "elapsedTimeAll";
	protected static final String COMPONENT_KEY_PRESET_TIME_ALL = "presetTimeAll";
	protected static final String COMPONENT_KEY_DEAD_TIME_ALL = "deadTimeAll";
	protected static final String COMPONENT_KEY_MAX_ENERGY_ALL = "maxEnergyAll";
	protected static final String COMPONENT_KEY_N_CHANNELS_ALL = "nChannelsAll";
	protected static final String COMPONENT_KEY_SLOW_PEAKS_FMT = "slowPeaks%s";
	protected static final String COMPONENT_KEY_INPUT_COUNT_RATE_FMT = "inputCountRate%s";
	protected static final String COMPONENT_KEY_OUTPUT_COUNT_RATE_FMT = "outputCountRate%s";
	
	public static final String VALUE_KEY_SPECTRUM_ALL = "spectrumAll";
	public static final String VALUE_KEY_ACQUIRE_START_ALL = "acquireStartAll";
	public static final String VALUE_KEY_ACQUIRE_STOP_ALL = "acquireStopAll";
	public static final String VALUE_KEY_ACQUIRE_ERASE_ALL = "acquireEraseAll";
	public static final String VALUE_KEY_ACQUIRE_ERASE_START_ALL = "acquireEraseStartAll";
	public static final String VALUE_KEY_ACQUIRE_STATE_ALL = "acquireStateAll";
	public static final String VALUE_KEY_ELAPSED_TIME_ALL = "elapsedTimeAll";
	public static final String VALUE_KEY_PRESET_TIME_ALL = "presetTimeAll";
	public static final String VALUE_KEY_DEAD_TIME_ALL = "deadTimeAll";
	public static final String VALUE_KEY_MAX_ENERGY_ALL = "maxEnergyAll";
	public static final String VALUE_KEY_N_CHANNELS_ALL = "nChannelsAll";
	public static final String VALUE_KEY_SLOW_PEAKS_FMT = "slowPeaks%s";
	public static final String VALUE_KEY_INPUT_COUNT_RATE_FMT = "inputCountRate%s";
	public static final String VALUE_KEY_OUTPUT_COUNT_RATE_FMT = "outputCountRate%s";
		
	private int nElements;
	
	public MultiElementDetector(String id, int nElements, Map<String, Device> map) {
		super(id, map);
		this.nElements = nElements;
		// Register this class as event listener for its components. //
		for(Map.Entry<String,Device> entry : getComponents().entrySet()) {
			entry.getValue().addEventListener(this);
		}
	}
	
	public Device getComponent(String name, int elementIdx) {
		return getComponent(name, String.valueOf(elementIdx));
	}
	
	protected Device getComponent(String name, String elementIdx) {
		return getComponent(String.format(name, elementIdx));
	}
	
	public int getNElements() {
		return nElements;
	}
	
	public Object getValue() {
		Map<String,Object> value = new HashMap<String,Object>();
		value.put(VALUE_KEY_SPECTRUM_ALL, getSpectrumAll());
		value.put(VALUE_KEY_ACQUIRE_STATE_ALL, getAcquireState());
		value.put(VALUE_KEY_ELAPSED_TIME_ALL, getElapsedTimeAll());
		value.put(VALUE_KEY_PRESET_TIME_ALL, getPresetTimeAll());
		value.put(VALUE_KEY_DEAD_TIME_ALL, getDeadTimeAll());
		value.put(VALUE_KEY_MAX_ENERGY_ALL, getMaxEnergyAll());
		value.put(VALUE_KEY_N_CHANNELS_ALL, getNChannelsAll());
		for(int idx=1; idx<=nElements; idx++) {
			String elementIdx =  String.valueOf(idx);
			value.put(String.format(VALUE_KEY_SLOW_PEAKS_FMT, elementIdx), getSlowPeaks(elementIdx));
			value.put(String.format(VALUE_KEY_INPUT_COUNT_RATE_FMT, elementIdx), getInputCountRate(elementIdx));
			value.put(String.format(VALUE_KEY_OUTPUT_COUNT_RATE_FMT, elementIdx), getOutputCountRate(elementIdx));
		}
		return value;
	}
	
	@SuppressWarnings("unchecked")
	public void setValue(Object value) {
		try {
			Map<String,Object> valueMap = (Map<String,Object>) value;
			
			if(valueMap.containsKey(VALUE_KEY_ACQUIRE_STOP_ALL)) {
				acquireStopAll();
			}
			
			if(valueMap.containsKey(VALUE_KEY_ACQUIRE_ERASE_ALL)) {
				acquireEraseAll();
			}
			
			if(valueMap.containsKey(VALUE_KEY_PRESET_TIME_ALL)) {
				setPresetTimeAll(valueMap.get(VALUE_KEY_PRESET_TIME_ALL));
			}
			
			if(valueMap.containsKey(VALUE_KEY_MAX_ENERGY_ALL)) {
				setMaxEnergyAll(valueMap.get(VALUE_KEY_MAX_ENERGY_ALL));
			}
			
			if(valueMap.containsKey(VALUE_KEY_ACQUIRE_START_ALL)) {
				acquireStartAll();
			}
			
			if(valueMap.containsKey(VALUE_KEY_ACQUIRE_ERASE_START_ALL)) {
				acquireEraseStartAll();
			}
		}
		catch(ClassCastException e) {
			log.warn("Set value argument is not expected class (Map<String,Object>).", e);
		}
	}
	
	public void acquireStartAll() {
		Device device = getComponent(COMPONENT_KEY_ACQUIRE_START_ALL);
		device.setValue(EPICS_ONE);
	}
	
	public void acquireStopAll() {
		Device device = getComponent(COMPONENT_KEY_ACQUIRE_STOP_ALL);
		device.setValue(EPICS_ONE);		
	}
	
	public void acquireEraseAll() {
		Device device = getComponent(COMPONENT_KEY_ACQUIRE_ERASE_ALL);
		device.setValue(EPICS_ONE);
	}
	
	public void acquireEraseStartAll() {
		Device device = getComponent(COMPONENT_KEY_ACQUIRE_ERASE_START_ALL);
		device.setValue(EPICS_ONE);
	}
	
	public int[] getSpectrumAll() {
		Device device = getComponent(COMPONENT_KEY_SPECTRUM_ALL);
		Object value = device.getValue();
		return getSpectrumAll(value);
	}
	
	public String getAcquireState() {
		Device device = getComponent(COMPONENT_KEY_ACQUIRE_STATE_ALL);
		Object value = device.getValue();
		return getAcquireStateAll(value);
	}
	
	public double getElapsedTimeAll() {
		Device device = getComponent(COMPONENT_KEY_ELAPSED_TIME_ALL);
		Object value = device.getValue();
		return getElapsedTimeAll(value);
	}
	
	public double getPresetTimeAll() {
		Device device = getComponent(COMPONENT_KEY_ELAPSED_TIME_ALL);
		Object value = device.getValue();
		return getPresetTimeAll(value);
	}
	
	public double getDeadTimeAll() {
		Device device = getComponent(COMPONENT_KEY_DEAD_TIME_ALL);
		Object value = device.getValue();
		return getDeadTimeAll(value);
	}
	
	public double getMaxEnergyAll() {
		Device device = getComponent(COMPONENT_KEY_MAX_ENERGY_ALL);
		Object value = device.getValue();
		return getMaxEnergyAll(value);
	}
	
	public int getNChannelsAll() {
		Device device = getComponent(COMPONENT_KEY_N_CHANNELS_ALL);
		Object value = device.getValue();
		return getNChannelsAll(value);
	}
	
	public int getSlowPeaks(int elementIdx) {
		return getSlowPeaks(String.valueOf(elementIdx));
	}
	
	protected int getSlowPeaks(String elementIdx) {
		Device device = getComponent(COMPONENT_KEY_SLOW_PEAKS_FMT, elementIdx);
		return getSlowPeaks(device.getValue(), elementIdx);
	}
	
	public double getInputCountRate(int elementIdx) {
		return getInputCountRate(String.valueOf(elementIdx));
	}
	
	protected double getInputCountRate(String elementIdx) {
		Device device = getComponent(COMPONENT_KEY_INPUT_COUNT_RATE_FMT, elementIdx);
		return getInputCountRate(device.getValue(), elementIdx);
	}
	
	public double getOutputCountRate(int elementIdx) {
		return getOutputCountRate(String.valueOf(elementIdx));
	}
	
	protected double getOutputCountRate(String elementIdx) {
		Device device = getComponent(COMPONENT_KEY_OUTPUT_COUNT_RATE_FMT, elementIdx);
		return getOutputCountRate(device.getValue(), elementIdx);
	}
	
	public void setPresetTimeAll(Object presetTimeAll) {
		try {
			Device device = getComponent(COMPONENT_KEY_PRESET_TIME_ALL);
			device.setValue(new double[] {  ((Number)presetTimeAll).doubleValue() });
		}
		catch(ClassCastException e) {
			log.warn("Set value for '" + VALUE_KEY_PRESET_TIME_ALL + "', is not a number.");
		}
		catch(NullPointerException e) {
			log.warn("Set value for '" + VALUE_KEY_PRESET_TIME_ALL + "', is null.");	
		}
	}
	
	public void setMaxEnergyAll(Object maxEnergyAll) {
		try {
			Device device = getComponent(COMPONENT_KEY_MAX_ENERGY_ALL);
			device.setValue(new double[] { ((Number)maxEnergyAll).doubleValue() });
		}
		catch(ClassCastException e) {
			log.warn("Set value for '" + VALUE_KEY_MAX_ENERGY_ALL + "', is not a number.");
		}
		catch(NullPointerException e) {
			log.warn("Set value for '" + VALUE_KEY_MAX_ENERGY_ALL + "', is null.");	
		}
	}
	
	protected int[] getSpectrumAll(Object value) {
		try {
			int[] intArray = (int[]) value;
			if(intArray == null) {
				throw new NullPointerException("Spectrum value is null.  Throw NullPointer Exception.");
			}
			return intArray;
		}
		catch(ClassCastException e) {
			log.warn("Value for '" + VALUE_KEY_SPECTRUM_ALL + "', is not expected class (int[]).");
			return new int[0];
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + VALUE_KEY_SPECTRUM_ALL + "', is null (Disconnected EpicsDevice?).");
			return new int[0];
		}
	}
	
	protected String getAcquireStateAll(Object value) {
		return getStringFromArray(value, MED_ACQUIRE_STATES, "N/A", VALUE_KEY_ACQUIRE_STATE_ALL);
	}
	
	protected double getElapsedTimeAll(Object value) {
		return getDoubleFromArray(value, Double.NaN, VALUE_KEY_ELAPSED_TIME_ALL);
	}
	
	protected double getPresetTimeAll(Object value) {
		return getDoubleFromArray(value, Double.NaN, VALUE_KEY_PRESET_TIME_ALL);
	}
	
	protected double getDeadTimeAll(Object value) {
		return getDoubleFromArray(value, Double.NaN, VALUE_KEY_DEAD_TIME_ALL);
	}
	
	protected double getMaxEnergyAll(Object value) {
		return getDoubleFromArray(value, Double.NaN, VALUE_KEY_MAX_ENERGY_ALL);
	}
	
	protected int getNChannelsAll(Object value) {
		return getIntFromArray(value, 0, VALUE_KEY_N_CHANNELS_ALL);
	}
	
	protected int getSlowPeaks(Object value, String elementIdx) {
		String slowPeaksValueKey = String.format(VALUE_KEY_SLOW_PEAKS_FMT, elementIdx);
		return getIntFromArray(value, 0, slowPeaksValueKey);
	}
	
	protected double getInputCountRate(Object value, String elementIdx) {
		String inputCountRateValueKey = String.format(VALUE_KEY_INPUT_COUNT_RATE_FMT, elementIdx);
		return getDoubleFromArray(value, 0, inputCountRateValueKey);
	}
	
	protected double getOutputCountRate(Object value, String elementIdx) {
		String outputCountRateValueKey = String.format(VALUE_KEY_OUTPUT_COUNT_RATE_FMT, elementIdx);
		return getDoubleFromArray(value, 0, outputCountRateValueKey);
	}
	
	
	/* This is a lot like the TypesUtility in SS_Data_Core !! //
	protected int[] toIntArray(Object value, int[] defaultValue, String description) {
		
		if(value == null) {
			// log
			return defaultValue;
		}
		
		Class<?> valueClass = value.getClass();
		
		if(valueClass.isArray()) {
			
			Class<?> valueClassComponentType = valueClass.getComponentType();
			
			if(Integer.TYPE.isAssignableFrom(valueClassComponentType)) {
				return (int[]) value;
			}
			
			int length = Array.getLength(value);
			int[] intArray = new int[length];
			
			if(Number.class.isAssignableFrom(valueClassComponentType)) {
				for(int idx=0; idx<length; idx++) {
					intArray[idx] = ((Number)Array.get(value, idx)).intValue();
				}
				return intArray;
			}
			
			try {
				for(int idx=0; idx<length; idx++) {
					intArray[idx] = Integer.valueOf(Array.get(value, idx).toString());
				}
				return intArray;
			}
			catch(NumberFormatException e) {
				// nothing to do //
			}
		}
		else {
			if(Integer.class.isAssignableFrom(valueClass)) {
				return new int[] { (Integer)value };
			}
			
			if(Number.class.isAssignableFrom(valueClass)) {
				return new int[] { ((Number)value).intValue() };
			}
			
			try {
				return new int[] { Integer.valueOf(value.toString()) };
			}
			catch(NumberFormatException e) {
				// nothing to do //
			}
		}
		
		return defaultValue;
	}
	*/
	
	
	protected Object getObjectFromArray(Object array, String description) {
		try {
			Object value = Array.get(array, 0);
			if(value == null) {
				log.warn("Value for '" + description + "', is null. (EpicsDevice problem!!)");
				return null;
			}
			return value;
		}
		catch(NullPointerException e) {
			log.warn("Array for '" + description + "', is null. (Disconnected EpicsDevice?)");
			return null;
		}
		catch(IllegalArgumentException e) {
			log.warn("Array for '" + description + "', is not an array. (EpicsDevice value is array?)");
			return null;
		}
		catch(IndexOutOfBoundsException e) {
			log.warn("Array for '" + description + "', has array size < 1. (EpicsDevice value is Array?)");
			return null;
		}
	}
	
	protected int getIntFromArray(Object array, int defaultValue, String description) {
		
		Object value = getObjectFromArray(array, description);
		
		if(value == null) {
			return defaultValue;
		}
		
		if(value instanceof Number) {
			return ((Number)value).intValue();
		}
		
		try {
			return Integer.valueOf(value.toString());
		}
		catch(NumberFormatException e) {
			// Nothing to do. //
		}
		
		log.warn("Value for '" + description + "', could not be converted to an Integer.");
		return defaultValue;
	}
	
	protected double getDoubleFromArray(Object array, double defaultValue, String description) {
	
		Object value = getObjectFromArray(array, description);
		
		if(value == null) {
			return defaultValue;
		}
		
		if(value instanceof Number) {
			return ((Number)value).doubleValue();
		}
		
		try {
			return Double.valueOf(value.toString());
		}
		catch(NumberFormatException e) {
			// Nothing to do. //
		}
		
		log.warn("Value for '" + description + "', could not be converted to a Double.");
		return defaultValue;
	}
	
	protected String getStringFromArray(Object array, String[] possibleValues, String defaultValue, String description) {
	
		int value = getIntFromArray(array, -1, description);
		
		try {
			return possibleValues[value];
		}
		catch(IndexOutOfBoundsException e) {
			log.warn("Value for '" + description + "', could not be converted to a String.");
			return defaultValue;
		}
	}
	
	protected String getComponentKeyByDeviceId(String deviceId) {
		for(Map.Entry<String,Device> entry : components.entrySet()) {
			if(entry.getValue().getId().equals(deviceId)) {
				return entry.getKey();
			}	
		}
		return new String();
	}
	
	public void handleEvent(DeviceEvent event) {
		
		String deviceId = event.getDeviceId();
		String componentKey = getComponentKeyByDeviceId(deviceId);
		
		DeviceEventType deviceEventType = event.getDeviceEventType();
		
		switch (deviceEventType) {
			case VALUE_CHANGE:
	
				Object value = event.getValue();
	
				String slowPeaksRegex = String.format(COMPONENT_KEY_SLOW_PEAKS_FMT, "(\\d+)");
				Matcher slowPeaksMatcher = Pattern.compile(slowPeaksRegex).matcher(componentKey);
				
				String inputCountRateRegex = String.format(COMPONENT_KEY_INPUT_COUNT_RATE_FMT, "(\\d+)");
				Matcher inputCountRateMatcher = Pattern.compile(inputCountRateRegex).matcher(componentKey);
				
				String outputCountRateRegex = String.format(COMPONENT_KEY_OUTPUT_COUNT_RATE_FMT, "(\\d+)");
				Matcher outputCountRateMatcher = Pattern.compile(outputCountRateRegex).matcher(componentKey);
				
				HashMap<String,Object> newValueMap = new HashMap<String,Object>();
				
				if(COMPONENT_KEY_ACQUIRE_STATE_ALL.equals(componentKey)) {
					newValueMap.put(VALUE_KEY_ACQUIRE_STATE_ALL, getAcquireStateAll(value));
				}
				else if(COMPONENT_KEY_SPECTRUM_ALL.equals(componentKey)) {
					newValueMap.put(VALUE_KEY_SPECTRUM_ALL, getSpectrumAll(value));
				}
				else if(COMPONENT_KEY_ELAPSED_TIME_ALL.equals(componentKey)) {
					newValueMap.put(VALUE_KEY_ELAPSED_TIME_ALL, getElapsedTimeAll(value));
				} 
				else if(COMPONENT_KEY_PRESET_TIME_ALL.equals(componentKey)) {
					newValueMap.put(VALUE_KEY_PRESET_TIME_ALL, getPresetTimeAll(value));
				}
				else if(COMPONENT_KEY_DEAD_TIME_ALL.equals(componentKey)) {
					newValueMap.put(VALUE_KEY_DEAD_TIME_ALL, getDeadTimeAll(value));
				}
				else if(COMPONENT_KEY_MAX_ENERGY_ALL.equals(componentKey)) {
					newValueMap.put(VALUE_KEY_MAX_ENERGY_ALL, getMaxEnergyAll(value));
				}
				else if(COMPONENT_KEY_N_CHANNELS_ALL.equals(componentKey)) {
					newValueMap.put(VALUE_KEY_N_CHANNELS_ALL, getNChannelsAll(value));
				}
				else if(slowPeaksMatcher.matches()) {
					String elementIdx = slowPeaksMatcher.group(1);
					String valueKey = String.format(VALUE_KEY_SLOW_PEAKS_FMT, elementIdx);
					newValueMap.put(valueKey, getSlowPeaks(value, elementIdx));
				}
				else if(inputCountRateMatcher.matches()) {
					String elementIdx = inputCountRateMatcher.group(1);
					String valueKey = String.format(VALUE_KEY_INPUT_COUNT_RATE_FMT, elementIdx);
					newValueMap.put(valueKey, getInputCountRate(value, elementIdx));
				}
				else if(outputCountRateMatcher.matches()) {
					String elementIdx = outputCountRateMatcher.group(1);
					String valueKey = String.format(VALUE_KEY_OUTPUT_COUNT_RATE_FMT, elementIdx);
					newValueMap.put(valueKey, getOutputCountRate(value, elementIdx));
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
