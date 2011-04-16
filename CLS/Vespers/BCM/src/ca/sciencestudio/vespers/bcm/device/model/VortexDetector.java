/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		VortexDetector class.
 *     
 */
package ca.sciencestudio.vespers.bcm.device.model;

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
public class VortexDetector extends DeviceComposite<DeviceComponent> {

	protected static final String COMPONENT_KEY_DATA = "data";
	protected static final String COMPONENT_KEY_ACQUIRE_START = "acquireStart";
	protected static final String COMPONENT_KEY_ACQUIRE_STOP = "acquireStop";
	protected static final String COMPONENT_KEY_ACQUIRE_ERASE = "acquireErase";
	protected static final String COMPONENT_KEY_ACQUIRE_ERASE_START = "acquireEraseStart";
	protected static final String COMPONENT_KEY_ACQUIRE_STATE = "acquireState";
	protected static final String COMPONENT_KEY_ELAPSED_TIME = "elapsedTime";
	protected static final String COMPONENT_KEY_PRESET_TIME = "presetTime";
	protected static final String COMPONENT_KEY_DEAD_TIME = "deadTime";
	protected static final String COMPONENT_KEY_PEAKING_TIME = "peakingTime";
	protected static final String COMPONENT_KEY_PEAKING_TIME_SP = "peakingTimeSP";
	protected static final String COMPONENT_KEY_TRIGGER_LEVEL = "triggerLevel";
	protected static final String COMPONENT_KEY_TRIGGER_LEVEL_SP = "triggerLevelSP";
	protected static final String COMPONENT_KEY_MAX_ENERGY = "maxEnergy";
	protected static final String COMPONENT_KEY_MAX_ENERGY_SP = "maxEnergySP";
	protected static final String COMPONENT_KEY_COUNTS = "counts";
	protected static final String COMPONENT_KEY_INPUT_COUNT_RATE = "inputCountRate";
	protected static final String COMPONENT_KEY_OUTPUT_COUNT_RATE = "outputCountRate";
	
	protected static final String[] VORTEX_DETECTOR_ACQUIRE_STATES = { "DONE", "ACQUIRING" };
	
	public static final String VALUE_KEY_SPECTRUM = "spectrum";
	public static final String VALUE_KEY_ACQUIRE_START = "acquireStart";
	public static final String VALUE_KEY_ACQUIRE_STOP = "acquireStop";
	public static final String VALUE_KEY_ACQUIRE_ERASE = "acquireErase";
	public static final String VALUE_KEY_ACQUIRE_ERASE_START = "acquireEraseStart";
	public static final String VALUE_KEY_ACQUIRE_STATE = "acquireState";
	public static final String VALUE_KEY_ELAPSED_TIME = "elapsedTime";
	public static final String VALUE_KEY_PRESET_TIME = "presetTime";
	public static final String VALUE_KEY_DEAD_TIME = "deadTime";
	public static final String VALUE_KEY_PEAKING_TIME = "peakingTime";
	public static final String VALUE_KEY_PEAKING_TIME_SP = "peakingTimeSP";
	public static final String VALUE_KEY_TRIGGER_LEVEL = "triggerLevel";
	public static final String VALUE_KEY_TRIGGER_LEVEL_SP = "triggerLevelSP";
	public static final String VALUE_KEY_MAX_ENERGY = "maxEnergy";
	public static final String VALUE_KEY_MAX_ENERGY_SP = "maxEnergySP";
	public static final String VALUE_KEY_COUNTS = "counts";
	public static final String VALUE_KEY_INPUT_COUNT_RATE = "inputCountRate";
	public static final String VALUE_KEY_OUTPUT_COUNT_RATE = "outputCountRate";
	
	private VortexDetectorDeviceEventListener vortexDetectorDeviceEventListener;
	
	public VortexDetector(String id, Map<String,DeviceComponent> components) {
		super(id, components);
		initVortexDetectorDeviceEventListener();
	}
	
	protected void initVortexDetectorDeviceEventListener() {
		vortexDetectorDeviceEventListener = new VortexDetectorDeviceEventListener();
		for(Map.Entry<String,DeviceComponent> entry : getComponents().entrySet()) {
			entry.getValue().addEventListener(vortexDetectorDeviceEventListener);
		}
	}
	
	public Object getValue() {
		Map<String,Object> value = new HashMap<String,Object>();
		value.put(VALUE_KEY_SPECTRUM, getSpectrum());
		value.put(VALUE_KEY_ACQUIRE_STATE, getAcquireState());
		value.put(VALUE_KEY_ELAPSED_TIME, getElapsedTime());
		value.put(VALUE_KEY_PRESET_TIME, getPresetTime());
		value.put(VALUE_KEY_DEAD_TIME, getDeadTime());
		value.put(VALUE_KEY_PEAKING_TIME, getPeakingTime());
		value.put(VALUE_KEY_PEAKING_TIME_SP, getPeakingTimeSP());
		value.put(VALUE_KEY_TRIGGER_LEVEL, getTriggerLevel());
		value.put(VALUE_KEY_TRIGGER_LEVEL_SP, getTriggerLevelSP());
		value.put(VALUE_KEY_MAX_ENERGY, getMaxEnergy());
		value.put(VALUE_KEY_MAX_ENERGY_SP, getMaxEnergySP());
		value.put(VALUE_KEY_COUNTS, getCounts());
		value.put(VALUE_KEY_INPUT_COUNT_RATE, getInputCountRate());
		value.put(VALUE_KEY_OUTPUT_COUNT_RATE, getOutputCountRate());
		return value;
	}
	
	@SuppressWarnings("unchecked")
	public void setValue(Object value) {
		try {
			Map<String,Object> valueMap = (Map<String,Object>) value;
			
			if(valueMap.containsKey(VALUE_KEY_ACQUIRE_STOP)) {
				acquireStop();
			}
			
			if(valueMap.containsKey(VALUE_KEY_ACQUIRE_ERASE)) {
				acquireErase();
			}
			
			if(valueMap.containsKey(VALUE_KEY_PRESET_TIME)) {
				try {
					Double presetTime = (Double) valueMap.get(VALUE_KEY_PRESET_TIME);
					setPresetTime(presetTime);
				}
				catch(ClassCastException e) {
					log.warn("Set value for '" + VALUE_KEY_PRESET_TIME + "', is not expected class (Double).", e);
				}
			}
			
			if(valueMap.containsKey(VALUE_KEY_PEAKING_TIME_SP)) {
				try {
					Double peakingTime = (Double) valueMap.get(VALUE_KEY_PEAKING_TIME_SP);
					setPeakingTimeSP(peakingTime);
				}
				catch(ClassCastException e) {
					log.warn("Set value for '" + VALUE_KEY_PEAKING_TIME_SP + "', is not expected class (Double).", e);
				}
			}
			
			if(valueMap.containsKey(VALUE_KEY_TRIGGER_LEVEL_SP)) {
				try {
					Double triggerLevel = (Double) valueMap.get(VALUE_KEY_TRIGGER_LEVEL_SP);
					setTriggerLevelSP(triggerLevel);
				}
				catch(ClassCastException e) {
					log.warn("Set value for '" + VALUE_KEY_TRIGGER_LEVEL_SP + "', is not expected class (Double).", e);
				}
			}
			
			if(valueMap.containsKey(VALUE_KEY_MAX_ENERGY_SP)) {
				try {
					Double maxEnergy = (Double) valueMap.get(VALUE_KEY_MAX_ENERGY_SP);
					setMaxEnergySP(maxEnergy);
				}
				catch(ClassCastException e) {
					log.warn("Set value for '" + VALUE_KEY_MAX_ENERGY_SP + "', is not expected class (Double).", e);
				}
			}
			
			if(valueMap.containsKey(VALUE_KEY_ACQUIRE_START)) {
				acquireStart();
			}
			
			if(valueMap.containsKey(VALUE_KEY_ACQUIRE_ERASE_START)) {
				acquireEraseStart();
			}
		}
		catch(ClassCastException e) {
			log.warn("Set value argument is not expected class (Map<String,Object>).", e);
		}
	}
	
	public void acquireStart() {
		DeviceComponent deviceComponent = getComponent(COMPONENT_KEY_ACQUIRE_START);
		deviceComponent.setValue(new short[] { 1 });
	}
	
	public void acquireStop() {
		DeviceComponent deviceComponent = getComponent(COMPONENT_KEY_ACQUIRE_STOP);
		deviceComponent.setValue(new short[] { 1 });		
	}
	
	public void acquireErase() {
		DeviceComponent deviceComponent = getComponent(COMPONENT_KEY_ACQUIRE_ERASE);
		deviceComponent.setValue(new short[] { 1 });
	}
	
	public void acquireEraseStart() {
		DeviceComponent deviceComponent = getComponent(COMPONENT_KEY_ACQUIRE_ERASE_START);
		deviceComponent.setValue(new short[] { 1 });
	}
	
	public int[] getSpectrum() {
		DeviceComponent deviceComponent = getComponent(COMPONENT_KEY_DATA);
		Object value = deviceComponent.getValue();
		return getSpectrum(value);
	}
	
	public String getAcquireState() {
		DeviceComponent deviceComponent = getComponent(COMPONENT_KEY_ACQUIRE_STATE);
		Object value = deviceComponent.getValue();
		return getAcquireState(value);
	}
	
	public double getElapsedTime() {
		DeviceComponent deviceComponent = getComponent(COMPONENT_KEY_ELAPSED_TIME);
		Object value = deviceComponent.getValue();
		return getElapsedTime(value);
	}
	
	public double getPresetTime() {
		DeviceComponent deviceComponent = getComponent(COMPONENT_KEY_ELAPSED_TIME);
		Object value = deviceComponent.getValue();
		return getPresetTime(value);
	}
	
	public double getDeadTime() {
		DeviceComponent deviceComponent = getComponent(COMPONENT_KEY_DEAD_TIME);
		Object value = deviceComponent.getValue();
		return getDeadTime(value);
	}
	
	public double getPeakingTime() {
		DeviceComponent deviceComponent = getComponent(COMPONENT_KEY_PEAKING_TIME);
		Object value = deviceComponent.getValue();
		return getPeakingTime(value);
	}
	
	public double getPeakingTimeSP() {
		DeviceComponent deviceComponent = getComponent(COMPONENT_KEY_PEAKING_TIME_SP);
		Object value = deviceComponent.getValue();
		return getPeakingTimeSP(value);
	}
	
	public double getTriggerLevel() {
		DeviceComponent deviceComponent = getComponent(COMPONENT_KEY_TRIGGER_LEVEL);
		Object value = deviceComponent.getValue();
		return getTriggerLevel(value);
	}
	
	public double getTriggerLevelSP() {
		DeviceComponent deviceComponent = getComponent(COMPONENT_KEY_TRIGGER_LEVEL_SP);
		Object value = deviceComponent.getValue();
		return getTriggerLevelSP(value);
	}
	
	public double getMaxEnergy() {
		DeviceComponent deviceComponent = getComponent(COMPONENT_KEY_MAX_ENERGY);
		Object value = deviceComponent.getValue();
		return getMaxEnergy(value);
	}
	
	public double getMaxEnergySP() {
		DeviceComponent deviceComponent = getComponent(COMPONENT_KEY_MAX_ENERGY_SP);
		Object value = deviceComponent.getValue();
		return getMaxEnergySP(value);
	}
	
	public int getCounts() {
		DeviceComponent deviceComponent = getComponent(COMPONENT_KEY_COUNTS);
		Object value = deviceComponent.getValue();
		return getCounts(value);
	}
	
	public double getInputCountRate() {
		DeviceComponent deviceComponent = getComponent(COMPONENT_KEY_INPUT_COUNT_RATE);
		Object value = deviceComponent.getValue();
		return getInputCountRate(value);
	}
	
	public double getOutputCountRate() {
		DeviceComponent deviceComponent = getComponent(COMPONENT_KEY_OUTPUT_COUNT_RATE);
		Object value = deviceComponent.getValue();
		return getOutputCountRate(value);
	}
	
	public void setPresetTime(double presetTime) {
		DeviceComponent deviceComponent = getComponent(COMPONENT_KEY_PRESET_TIME);
		deviceComponent.setValue(new double[] { presetTime });
	}
	
	public void setPeakingTimeSP(double peakingTime) {
		DeviceComponent deviceComponent = getComponent(COMPONENT_KEY_PEAKING_TIME_SP);
		deviceComponent.setValue(new double[] { peakingTime });
	}
	
	public void setTriggerLevelSP(double triggerLevel) {
		DeviceComponent deviceComponent = getComponent(COMPONENT_KEY_TRIGGER_LEVEL_SP);
		deviceComponent.setValue(new double[] { triggerLevel });
	}
	
	public void setMaxEnergySP(double maxEnergy) {
		DeviceComponent deviceComponent = getComponent(COMPONENT_KEY_MAX_ENERGY_SP);
		deviceComponent.setValue(new double[] { maxEnergy });
	}
	
	protected int[] getSpectrum(Object value) {
		try {
			int[] intArray = (int[]) value;
			if(intArray == null) {
				throw new NullPointerException("Spectrum value is null.  Throw NullPointer Exception.");
			}
			return intArray;
		}
		catch(ClassCastException e) {
			log.warn("Value for '" + VALUE_KEY_SPECTRUM + "', is not expected class (int[]).", e);
			return new int[0];
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + VALUE_KEY_SPECTRUM + "', is null (Disconnected EpicsDevice?).", e);
			return new int[0];
		}
	}
	
	protected String getAcquireState(Object value) {
		try {
			short[] shtArray = (short[]) value;
			return VORTEX_DETECTOR_ACQUIRE_STATES[shtArray[0]];
		}
		catch(ClassCastException e) {
			log.warn("Value for '" + VALUE_KEY_ACQUIRE_STATE + "', is not expected class (double[]).", e);
			return "N/A";
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + VALUE_KEY_ACQUIRE_STATE + "', is null (Disconnected EpicsDevice?).", e);
			return "N/A";
		}
	}
	
	protected double getElapsedTime(Object value) {
		try {
			double[] dblArray = (double[]) value;
			return dblArray[0];
		}
		catch(ClassCastException e) {
			log.warn("Value for '" + VALUE_KEY_ELAPSED_TIME + "', is not expected class (double[]).", e);
			return Double.NaN;
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + VALUE_KEY_ELAPSED_TIME + "', is null (Disconnected EpicsDevice?).", e);
			return Double.NaN;
		}
	}
	
	protected double getPresetTime(Object value) {
		try {
			double[] dblArray = (double[]) value;
			return dblArray[0];
		}
		catch(ClassCastException e) {
			log.warn("Value for '" + VALUE_KEY_PRESET_TIME + "', is not expected class (double[]).", e);
			return Double.NaN;
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + VALUE_KEY_PRESET_TIME + "', is null (Disconnected EpicsDevice?).", e);
			return Double.NaN;
		}
	}
	
	protected double getDeadTime(Object value) {
		try {
			double[] dblArray = (double[]) value;
			return dblArray[0];
		}
		catch(ClassCastException e) {
			log.warn("Value for '" + VALUE_KEY_DEAD_TIME + "', is not expected class (double[]).", e);
			return Double.NaN;
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + VALUE_KEY_DEAD_TIME + "', is null (Disconnected EpicsDevice?).", e);
			return Double.NaN;
		}
	}
	
	protected double getPeakingTime(Object value) {
		try {
			double[] dblArray = (double[]) value;
			return dblArray[0];
		}
		catch(ClassCastException e) {
			log.warn("Value for '" + VALUE_KEY_PEAKING_TIME + "', is not expected class (double[]).", e);
			return Double.NaN;
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + VALUE_KEY_PEAKING_TIME + "', is null (Disconnected EpicsDevice?).", e);
			return Double.NaN;
		}
	}
	
	protected double getPeakingTimeSP(Object value) {
		try {
			double[] dblArray = (double[]) value;
			return dblArray[0];
		}
		catch(ClassCastException e) {
			log.warn("Value for '" + VALUE_KEY_PEAKING_TIME_SP + "', is not expected class (double[]).", e);
			return Double.NaN;
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + VALUE_KEY_PEAKING_TIME_SP + "', is null (Disconnected EpicsDevice?).", e);
			return Double.NaN;
		}
	}
	
	protected double getTriggerLevel(Object value) {
		try {
			double[] dblArray = (double[]) value;
			return dblArray[0];
		}
		catch(ClassCastException e) {
			log.warn("Value for '" + VALUE_KEY_TRIGGER_LEVEL + "', is not expected class (double[]).", e);
			return Double.NaN;
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + VALUE_KEY_TRIGGER_LEVEL + "', is null (Disconnected EpicsDevice?).", e);
			return Double.NaN;
		}
	}
	
	protected double getTriggerLevelSP(Object value) {
		try {
			double[] dblArray = (double[]) value;
			return dblArray[0];
		}
		catch(ClassCastException e) {
			log.warn("Value for '" + VALUE_KEY_TRIGGER_LEVEL_SP + "', is not expected class (double[]).", e);
			return Double.NaN;
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + VALUE_KEY_TRIGGER_LEVEL_SP + "', is null (Disconnected EpicsDevice?).", e);
			return Double.NaN;
		}
	}
	
	protected double getMaxEnergy(Object value) {
		try {
			double[] dblArray = (double[]) value;
			return dblArray[0];
		}
		catch(ClassCastException e) {
			log.warn("Value for '" + VALUE_KEY_MAX_ENERGY + "', is not expected class (double[]).", e);
			return Double.NaN;
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + VALUE_KEY_MAX_ENERGY + "', is null (Disconnected EpicsDevice?).", e);
			return Double.NaN;
		}
	}
	
	protected double getMaxEnergySP(Object value) {
		try {
			double[] dblArray = (double[]) value;
			return dblArray[0];
		}
		catch(ClassCastException e) {
			log.warn("Value for '" + VALUE_KEY_MAX_ENERGY_SP + "', is not expected class (double[]).", e);
			return Double.NaN;
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + VALUE_KEY_MAX_ENERGY_SP + "', is null (Disconnected EpicsDevice?).", e);
			return Double.NaN;
		}
	}
	
	protected int getCounts(Object value) {
		try {
			int[] intArray = (int[]) value;
			return intArray[0];
		}
		catch(ClassCastException e) {
			log.warn("Value for '" + VALUE_KEY_COUNTS + "', is not expected class (int[]).", e);
			return 0;
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + VALUE_KEY_COUNTS + "', is null (Disconnected EpicsDevice?).", e);
			return 0;
		}
	}
	
	protected double getInputCountRate(Object value) {
		try {
			double[] dblArray = (double[]) value;
			return dblArray[0];
		}
		catch(ClassCastException e) {
			log.warn("Value for '" + VALUE_KEY_INPUT_COUNT_RATE + "', is not expected class (double[]).", e);
			return Double.NaN;
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + VALUE_KEY_INPUT_COUNT_RATE + "', is null (Disconnected EpicsDevice?).", e);
			return Double.NaN;
		}
	}
	
	protected double getOutputCountRate(Object value) {
		try {
			double[] dblArray = (double[]) value;
			return dblArray[0];
		}
		catch(ClassCastException e) {
			log.warn("Value for '" + VALUE_KEY_OUTPUT_COUNT_RATE + "', is not expected class (double[]).", e);
			return Double.NaN;
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + VALUE_KEY_OUTPUT_COUNT_RATE + "', is null (Disconnected EpicsDevice?).", e);
			return Double.NaN;
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
	
	protected class VortexDetectorDeviceEventListener implements DeviceEventListener {
		
		public VortexDetectorDeviceEventListener() {}

		public void handleEvent(DeviceEvent event) {
		
			String deviceId = event.getDeviceId();
			String componentKey = getComponentKeyByDeviceId(deviceId);
			
			DeviceEventType deviceEventType = event.getDeviceEventType();
			
			switch (deviceEventType) {
				case VALUE_CHANGE:
		
					Object value = event.getValue();
		
					HashMap<String,Object> newValueMap = new HashMap<String,Object>();
					
					if(COMPONENT_KEY_ACQUIRE_STATE.equals(componentKey)) {
						newValueMap.put(VALUE_KEY_ACQUIRE_STATE, getAcquireState(value));
					}
					else if(COMPONENT_KEY_DATA.equals(componentKey)) {
						newValueMap.put(VALUE_KEY_SPECTRUM, getSpectrum(value));
					}
					else if(COMPONENT_KEY_ELAPSED_TIME.equals(componentKey)) {
						newValueMap.put(VALUE_KEY_ELAPSED_TIME, getElapsedTime(value));
					} 
					else if(COMPONENT_KEY_PRESET_TIME.equals(componentKey)) {
						newValueMap.put(VALUE_KEY_PRESET_TIME, getPresetTime(value));
					}
					else if(COMPONENT_KEY_DEAD_TIME.equals(componentKey)) {
						newValueMap.put(VALUE_KEY_DEAD_TIME, getDeadTime(value));
					}
					else if(COMPONENT_KEY_PEAKING_TIME.equals(componentKey)) {
						newValueMap.put(VALUE_KEY_PEAKING_TIME, getPeakingTime(value));
					}
					else if(COMPONENT_KEY_PEAKING_TIME_SP.equals(componentKey)) {
						newValueMap.put(VALUE_KEY_PEAKING_TIME_SP, getPeakingTimeSP(value));
					}
					else if(COMPONENT_KEY_TRIGGER_LEVEL.equals(componentKey)) {
						newValueMap.put(VALUE_KEY_TRIGGER_LEVEL, getTriggerLevel(value));
					}
					else if(COMPONENT_KEY_TRIGGER_LEVEL_SP.equals(componentKey)) {
						newValueMap.put(VALUE_KEY_TRIGGER_LEVEL_SP, getTriggerLevelSP(value));
					}
					else if(COMPONENT_KEY_MAX_ENERGY.equals(componentKey)) {
						newValueMap.put(VALUE_KEY_MAX_ENERGY, getMaxEnergy(value));
					}
					else if(COMPONENT_KEY_MAX_ENERGY_SP.equals(componentKey)) {
						newValueMap.put(VALUE_KEY_MAX_ENERGY_SP, getMaxEnergySP(value));
					}
					else if(COMPONENT_KEY_COUNTS.equals(componentKey)) {
						newValueMap.put(VALUE_KEY_COUNTS, getCounts(value));
					}
					else if(COMPONENT_KEY_INPUT_COUNT_RATE.equals(componentKey)) {
						newValueMap.put(VALUE_KEY_INPUT_COUNT_RATE, getInputCountRate(value));
					}
					else if(COMPONENT_KEY_OUTPUT_COUNT_RATE.equals(componentKey)) {
						newValueMap.put(VALUE_KEY_OUTPUT_COUNT_RATE, getOutputCountRate(value));
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
