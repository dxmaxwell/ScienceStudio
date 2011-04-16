/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		SimulationVortexDetector class.
 *     
 */
package ca.sciencestudio.vespers.bcm.simulation.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;

import ca.sciencestudio.device.control.simulation.SimulationDevice;
import ca.sciencestudio.device.control.simulation.SimulationEvent;
import ca.sciencestudio.device.control.simulation.SimulationEventType;
import ca.sciencestudio.device.control.simulation.SimulationEventListener;
import ca.sciencestudio.device.control.simulation.AbstractSimulationDevice;

/**
 * @author maxweld
 *
 */
public class SimulationVortexDetector extends AbstractSimulationDevice implements SimulationEventListener {

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
	
	public static final double MAXIMUM_PRESET_TIME = 1717;
	public static final int AVAILABLE_SPECTRUM_SIZES[] = new int[] { 1024, 2048, 4096, 8192 };
	public static final double AVAILABLE_CHANNEL_SIZES[] = new double[] { 10.0, 20.0, 40.0 };
	public static final int DEFAULT_CHANNEL_SIZE_INDEX = 1;
	
	public static final String DEVICE_VALUE_KEY_POSITION = "position";
	public static final String DEVICE_VALUE_KEY_STATUS = "status";
	
	public static enum SimulationVortexDetectorState {
		DONE, ACQUIRING
	};
	
	public static enum SimulationVortexDetectorNoYes {
		NO, YES
	}
	
	private int simulationUpdateInterval = 2000; // milliseconds //
	private Thread simulationSimpleMotorThread;
	private SpectrumGenerator spectrumGenerator;
	
	private SimulationVortexDetectorNoYes acquireStart = SimulationVortexDetectorNoYes.NO;
	private SimulationVortexDetectorNoYes acquireStop = SimulationVortexDetectorNoYes.NO;
	private SimulationVortexDetectorNoYes acquireErase = SimulationVortexDetectorNoYes.NO;
	private SimulationVortexDetectorNoYes acquireEraseStart = SimulationVortexDetectorNoYes.NO;
	private SimulationVortexDetectorState acquireState = SimulationVortexDetectorState.DONE;
	
	private double elapsedTime = 0.0; // seconds
	private double deadTime = 0.0; // percent
	
	private double presetTime = 0.0; // seconds
	//private double presetTimeSetpoint = 0.0; // seconds
	
	private double peakingTime = 0.25; // microseconds
	private double peakingTimeSetpoint = 0.25; // microseconds
	
	private double counts = 3;
	private double inputCountRate = 0.0; // counts per second
	private double outputCountRate = 0.0; // counts per second
	
	private double positionA = 0.0;
	private double positionB = 0.0;
	
	private int statusA = 0;
	private int statusB = 0;
	
	private SimulationDevice deviceA;
	private SimulationDevice deviceB;
	
	private int rawDataFileCount;
	private String rawDataFilePathTemplate;
	
	SimulationVortexDetector(String id, int rawDataFileCount, String rawDataFilePathTemplate, SimulationDevice deviceA, SimulationDevice deviceB) {
		super(id);
		this.rawDataFileCount = rawDataFileCount;
		this.rawDataFilePathTemplate = rawDataFilePathTemplate;
		spectrumGenerator = new SpectrumGenerator();
		this.deviceA = deviceA;
		this.deviceB = deviceB;
		initSimulationDevices();
		simulationSimpleMotorThread = new SimulationVortexDetectorThread();
		simulationSimpleMotorThread.start();
	}
	
	protected void initSimulationDevices() {
		
		deviceA.addEventListener(this);
		deviceB.addEventListener(this);
		
		Map<String,Object> valueMap;
		
		valueMap = getMapFromObject(deviceA.getValue());
		if(valueMap.containsKey(DEVICE_VALUE_KEY_POSITION)) {
			try {
				positionA = (Double)valueMap.get(DEVICE_VALUE_KEY_POSITION);
			}
			catch(ClassCastException e) {
				log.warn("Value for deviceA with key, " + DEVICE_VALUE_KEY_POSITION + ", is unexpected class.");
			}
		}
		if(valueMap.containsKey(DEVICE_VALUE_KEY_STATUS)) {
			try {
				statusA = (Integer)valueMap.get(DEVICE_VALUE_KEY_STATUS);
			}
			catch(ClassCastException e) {
				log.warn("Value for deviceA with key, " + DEVICE_VALUE_KEY_STATUS + ", is unexpected class.");
			}
		}
		
		valueMap = getMapFromObject(deviceB.getValue());
		if(valueMap.containsKey(DEVICE_VALUE_KEY_POSITION)) {
			try {
				positionB = (Double)valueMap.get(DEVICE_VALUE_KEY_POSITION);
			}
			catch(ClassCastException e) {
				log.warn("Value for deviceB with key, " + DEVICE_VALUE_KEY_POSITION + ", is unexpected class.");
			}
		}
		if(valueMap.containsKey(DEVICE_VALUE_KEY_STATUS)) {
			try {
			statusB = (Integer)valueMap.get(DEVICE_VALUE_KEY_STATUS);
			}
			catch(ClassCastException e) {
				log.warn("Value for deviceB with key, " + DEVICE_VALUE_KEY_STATUS + ", is unexpected class.");
			}
		}
		
		if((statusA == 0) && (statusB == 0)) {
			spectrumGenerator.setSeed(computeSeed());
		}
	}
	
	
	public Object getValue() {
		Map<String,Object> valueMap = new HashMap<String,Object>();
		valueMap.put(VALUE_KEY_SPECTRUM, getSpectrum());
		valueMap.put(VALUE_KEY_ACQUIRE_START, getAcquireStart());
		valueMap.put(VALUE_KEY_ACQUIRE_STOP, getAcquireStop());
		valueMap.put(VALUE_KEY_ACQUIRE_ERASE, getAcquireErase());
		valueMap.put(VALUE_KEY_ACQUIRE_ERASE_START, getAcquireEraseStart());
		valueMap.put(VALUE_KEY_ACQUIRE_STATE, getAcquireState());
		valueMap.put(VALUE_KEY_ELAPSED_TIME, getElapsedTime());
		valueMap.put(VALUE_KEY_PRESET_TIME, getPresetTime());
		valueMap.put(VALUE_KEY_DEAD_TIME, getDeadTime());
		valueMap.put(VALUE_KEY_PEAKING_TIME, getPeakingTime());
		valueMap.put(VALUE_KEY_PEAKING_TIME_SP, getPeakingTimeSP());
		valueMap.put(VALUE_KEY_TRIGGER_LEVEL, getTriggerLevel());
		valueMap.put(VALUE_KEY_TRIGGER_LEVEL_SP, getTriggerLevelSP());
		valueMap.put(VALUE_KEY_MAX_ENERGY, getMaxEnergy());
		valueMap.put(VALUE_KEY_MAX_ENERGY_SP, getMaxEnergySP());
		valueMap.put(VALUE_KEY_COUNTS, getCounts());
		valueMap.put(VALUE_KEY_INPUT_COUNT_RATE, getInputCountRate());
		valueMap.put(VALUE_KEY_OUTPUT_COUNT_RATE, getOutputCountRate());
		return valueMap;
	}

	public void setValue(Object value) {
		
		Map<String,Object> valueMap = getMapFromObject(value);
		
		if(valueMap.containsKey(VALUE_KEY_ACQUIRE_START)) {
			try {
				setAcquireStart((Integer)valueMap.get(VALUE_KEY_ACQUIRE_START));
			}
			catch(ClassCastException e) {
				log.warn("Value for key, " + VALUE_KEY_ACQUIRE_STATE + ", is unexpected class.");
			}
		}
		
		if(valueMap.containsKey(VALUE_KEY_ACQUIRE_STOP)) {
			try {
				setAcquireStop((Integer)valueMap.get(VALUE_KEY_ACQUIRE_STOP));
			}
			catch(ClassCastException e) {
				log.warn("Value for key, " + VALUE_KEY_ACQUIRE_STOP + ", is unexpected class.");
			}
		}
		
		if(valueMap.containsKey(VALUE_KEY_ACQUIRE_ERASE)) {
			try {
				setAcquireErase((Integer)valueMap.get(VALUE_KEY_ACQUIRE_ERASE));
			}
			catch(ClassCastException e) {
				log.warn("Value for key, " + VALUE_KEY_ACQUIRE_ERASE + ", is unexpected class.");
			}
		}
		
		if(valueMap.containsKey(VALUE_KEY_ACQUIRE_ERASE_START)) {
			try {
				setAcquireEraseStart((Integer)valueMap.get(VALUE_KEY_ACQUIRE_ERASE_START));
			}
			catch(ClassCastException e) {
				log.warn("Value for key, " + VALUE_KEY_ACQUIRE_ERASE_START + ", is unexpected class.");
			}
		}
		
		if(valueMap.containsKey(VALUE_KEY_PRESET_TIME)) {
			try {
				setPresetTime((Double)valueMap.get(VALUE_KEY_PRESET_TIME));
			}
			catch(ClassCastException e) {
				log.warn("Value for key, " + VALUE_KEY_PRESET_TIME + ", is unexpected class.");
			}
		}
		
		if(valueMap.containsKey(VALUE_KEY_TRIGGER_LEVEL_SP)) {
			try {
				setTriggerLevelSP((Double)valueMap.get(VALUE_KEY_TRIGGER_LEVEL_SP));
			}
			catch(ClassCastException e) {
				log.warn("Value for key, " + VALUE_KEY_TRIGGER_LEVEL_SP + ", is unexpected class.");
			}
		}
		
		if(valueMap.containsKey(VALUE_KEY_MAX_ENERGY_SP)) {
			try {
				setMaxEnergySP((Double)valueMap.get(VALUE_KEY_MAX_ENERGY_SP));
			}
			catch(ClassCastException e) {
				log.warn("Value for key, " + VALUE_KEY_MAX_ENERGY_SP + ", is unexpected class.");
			}
		}
	}

	public int[] getSpectrum() {
		return spectrumGenerator.getSpectrum();
	}
	
	
	public int getAcquireStart() {
		return acquireStart.ordinal();
	}

	public void setAcquireStart(int acquireStart) {
		
		SimulationVortexDetectorNoYes noYesValues[] = 
				SimulationVortexDetectorNoYes.values();
		
		if((acquireStart < 0) || (acquireStart >= noYesValues.length)) {
			log.warn("AcquireStart out of bounds");
			return;
		}
		
		this.acquireStart = noYesValues[acquireStart];
		
		Map<String,Object> valueMap = new HashMap<String,Object>();
		
		if(this.acquireStart == SimulationVortexDetectorNoYes.YES) {
			valueMap.put(VALUE_KEY_ACQUIRE_START, getAcquireStart());
			publishEvent(new SimulationEvent(SimulationEventType.VALUE_CHANGE, id, valueMap));
			this.acquireState = SimulationVortexDetectorState.ACQUIRING;
			this.acquireStart = SimulationVortexDetectorNoYes.NO;
			valueMap.put(VALUE_KEY_ACQUIRE_STATE, getAcquireState());
			valueMap.put(VALUE_KEY_ACQUIRE_START, getAcquireStart());
			publishEvent(new SimulationEvent(SimulationEventType.VALUE_CHANGE, id, valueMap));
		}
		else if(this.acquireStart == SimulationVortexDetectorNoYes.NO) {
			valueMap.put(VALUE_KEY_ACQUIRE_START, getAcquireStart());
			publishEvent(new SimulationEvent(SimulationEventType.VALUE_CHANGE, id, valueMap));
		}
	}

	public int getAcquireStop() {
		return acquireStop.ordinal();
	}

	public void setAcquireStop(int acquireStop) {
		
		SimulationVortexDetectorNoYes noYesValues[] = 
				SimulationVortexDetectorNoYes.values();
		
		if((acquireStop < 0) || (acquireStop >= noYesValues.length)) {
			log.warn("AcquireStop out of bounds");
			return;
		}
		
		this.acquireStop = noYesValues[acquireStop];
		
		Map<String,Object> valueMap = new HashMap<String,Object>();
		
		if(this.acquireStop == SimulationVortexDetectorNoYes.YES) {
			valueMap.put(VALUE_KEY_ACQUIRE_STOP, getAcquireStop());
			publishEvent(new SimulationEvent(SimulationEventType.VALUE_CHANGE, id, valueMap));
			this.acquireState = SimulationVortexDetectorState.DONE;
			this.acquireStop = SimulationVortexDetectorNoYes.NO;
			valueMap.put(VALUE_KEY_ACQUIRE_STATE, getAcquireState());
			valueMap.put(VALUE_KEY_ACQUIRE_STOP, getAcquireStop());
			publishEvent(new SimulationEvent(SimulationEventType.VALUE_CHANGE, id, valueMap));
		}
		else if(this.acquireStop == SimulationVortexDetectorNoYes.NO) {
			valueMap.put(VALUE_KEY_ACQUIRE_STOP, getAcquireStop());
			publishEvent(new SimulationEvent(SimulationEventType.VALUE_CHANGE, id, valueMap));
		}
	}

	public int getAcquireErase() {
		return acquireErase.ordinal();
	}

	public void setAcquireErase(int acquireErase) {
		
		SimulationVortexDetectorNoYes noYesValues[] = 
				SimulationVortexDetectorNoYes.values();
		
		if((acquireErase < 0) || (acquireErase >= noYesValues.length)) {
			log.warn("AcquireErase out of bounds");	
			return;
		}
		
		this.acquireErase = noYesValues[acquireErase];
		
		Map<String,Object> valueMap = new HashMap<String,Object>();
		
		if(this.acquireErase == SimulationVortexDetectorNoYes.YES) {
			valueMap.put(VALUE_KEY_ACQUIRE_ERASE, getAcquireErase());
			publishEvent(new SimulationEvent(SimulationEventType.VALUE_CHANGE, id, valueMap));
			elapsedTime = 0.0;
			spectrumGenerator.eraseSpectrum();
			this.acquireErase = SimulationVortexDetectorNoYes.NO;
			valueMap.put(VALUE_KEY_SPECTRUM, getSpectrum());
			valueMap.put(VALUE_KEY_ELAPSED_TIME, getElapsedTime());
			valueMap.put(VALUE_KEY_ACQUIRE_ERASE, getAcquireErase());
			publishEvent(new SimulationEvent(SimulationEventType.VALUE_CHANGE, id, valueMap));
		}
		else if(this.acquireErase == SimulationVortexDetectorNoYes.NO) {
			valueMap.put(VALUE_KEY_ACQUIRE_ERASE, getAcquireErase());
			publishEvent(new SimulationEvent(SimulationEventType.VALUE_CHANGE, id, valueMap));
		}
	}

	public int getAcquireEraseStart() {
		return acquireEraseStart.ordinal();
	}
	
	public void setAcquireEraseStart(int acquireEraseStart) {
		
		SimulationVortexDetectorNoYes noYesValues[] = 
						SimulationVortexDetectorNoYes.values();
	
		if((acquireEraseStart < 0) || (acquireEraseStart >= noYesValues.length)) {
			log.warn("AcquireEraseStart out of bounds");	
			return;
		}
		
		this.acquireEraseStart = noYesValues[acquireEraseStart];
		
		Map<String,Object> valueMap = new HashMap<String,Object>();
		
		if(this.acquireEraseStart == SimulationVortexDetectorNoYes.YES) {
			valueMap.put(VALUE_KEY_ACQUIRE_ERASE_START, getAcquireEraseStart());
			publishEvent(new SimulationEvent(SimulationEventType.VALUE_CHANGE, id, valueMap));
			setAcquireErase(SimulationVortexDetectorNoYes.YES.ordinal());
			setAcquireStart(SimulationVortexDetectorNoYes.YES.ordinal());
			this.acquireEraseStart = SimulationVortexDetectorNoYes.NO;
			valueMap.put(VALUE_KEY_ACQUIRE_ERASE_START, getAcquireEraseStart());
			publishEvent(new SimulationEvent(SimulationEventType.VALUE_CHANGE, id, valueMap));
		} 
		else if(this.acquireEraseStart == SimulationVortexDetectorNoYes.NO) {
			valueMap.put(VALUE_KEY_ACQUIRE_ERASE_START, getAcquireEraseStart());
			publishEvent(new SimulationEvent(SimulationEventType.VALUE_CHANGE, id, valueMap));
		}
	}
	
	public int getAcquireState() {
		return acquireState.ordinal();
	}

	public double getElapsedTime() {
		return elapsedTime;
	}
	
	public double getPresetTime() {
		return presetTime;
	}
	
	public void setPresetTime(double presetTime) {
		if(presetTime < 0.0) {
			presetTime = 0.0;
		}
		else if(presetTime > MAXIMUM_PRESET_TIME) {
			presetTime = MAXIMUM_PRESET_TIME;
		}
		this.presetTime = presetTime;
		Map<String,Object> valueMap = new HashMap<String,Object>();
		valueMap.put(VALUE_KEY_PRESET_TIME, getPresetTime());
		publishEvent(new SimulationEvent(SimulationEventType.VALUE_CHANGE, id, valueMap));
	}
	
	public double getDeadTime() {
		return deadTime;
	}
	
	public double getPeakingTime() {
		return peakingTime;
	}
	
	public double getPeakingTimeSP() {
		return peakingTimeSetpoint;
	}
	
	public double getTriggerLevel() {
		return spectrumGenerator.getTriggerLevel();
	}
	
	public double getTriggerLevelSP() {
		return spectrumGenerator.getTriggerLevelSetpoint();
	}
	
	public void setTriggerLevelSP(double triggerLevelSP) {
		spectrumGenerator.setTriggerLevelSetpoint(triggerLevelSP);
		Map<String,Object> valueMap = new HashMap<String,Object>();
		valueMap.put(VALUE_KEY_TRIGGER_LEVEL, getTriggerLevel());
		valueMap.put(VALUE_KEY_TRIGGER_LEVEL_SP, getTriggerLevelSP());
		publishEvent(new SimulationEvent(SimulationEventType.VALUE_CHANGE, id, valueMap));
	}
	
	public double getMaxEnergy() {
		return spectrumGenerator.getMaximumEnergy();
	}
	
	public double getMaxEnergySP() {
		return spectrumGenerator.getMaximumEnergySetpoint();
	}
	
	public void setMaxEnergySP(double maxEnergySP) {
		spectrumGenerator.setMaximumEnergySetpoint(maxEnergySP);
		Map<String,Object> valueMap = new HashMap<String,Object>();
		valueMap.put(VALUE_KEY_SPECTRUM, getSpectrum());
		valueMap.put(VALUE_KEY_MAX_ENERGY, getMaxEnergy());
		valueMap.put(VALUE_KEY_MAX_ENERGY_SP, getMaxEnergySP());
		valueMap.put(VALUE_KEY_TRIGGER_LEVEL, getTriggerLevel());
		valueMap.put(VALUE_KEY_TRIGGER_LEVEL_SP, getTriggerLevelSP());
		publishEvent(new SimulationEvent(SimulationEventType.VALUE_CHANGE, id, valueMap));
	}
	
	public double getCounts() {
		return counts;
	}
	
	public double getInputCountRate() {
		return inputCountRate;
	}
	
	public void setInputCountRate(double inputCountRate) {
		this.inputCountRate = inputCountRate;
	}
	
	public double getOutputCountRate() {
		return outputCountRate;
	}
	
	public int getRawDataFileCount() {
		return rawDataFileCount;
	}

	public String getRawDataFilePathTemplate() {
		return rawDataFilePathTemplate;
	}

	public int getSimulationUpdateInterval() {
		return simulationUpdateInterval;
	}

	public void setSimulationUpdateInterval(int simulationUpdateInterval) {
		this.simulationUpdateInterval = simulationUpdateInterval;
	}

	public void handleEvent(SimulationEvent event) {
		
		String deviceId = event.getDeviceId();
		
		Map<String,Object> valueMap = getMapFromObject(event.getValue());
		
		if(deviceA.getId().equals(deviceId)) {
			if(valueMap.containsKey(DEVICE_VALUE_KEY_POSITION)) {
				try {
					positionA = (Double)valueMap.get(DEVICE_VALUE_KEY_POSITION);
				}
				catch(ClassCastException e) {
					log.warn("Value for deviceA with key, " + DEVICE_VALUE_KEY_POSITION + ", is unexpected class.");
				}
			}
			if(valueMap.containsKey(DEVICE_VALUE_KEY_STATUS)) {
				try {
					statusA = (Integer)valueMap.get(DEVICE_VALUE_KEY_STATUS);
				}
				catch(ClassCastException e) {
					log.warn("Value deviceA with key, " + DEVICE_VALUE_KEY_STATUS + ", is unexpected class.");
				}
			}
		}
		else if(deviceB.getId().equals(deviceId)) {
			if(valueMap.containsKey(DEVICE_VALUE_KEY_POSITION)) {
				try {
					positionB = (Double)valueMap.get(DEVICE_VALUE_KEY_POSITION);
				}
				catch(ClassCastException e) {
					log.warn("Value for deviceB with key, " + DEVICE_VALUE_KEY_POSITION + ", is unexpected class.");
				}
			}
			if(valueMap.containsKey(DEVICE_VALUE_KEY_STATUS)) {
				try {
					statusB = (Integer)valueMap.get(DEVICE_VALUE_KEY_STATUS);
				}
				catch(ClassCastException e) {
					log.warn("Value deviceB with key, " + DEVICE_VALUE_KEY_STATUS + ", is unexpected class.");
				}
			}
		}
		
		if((statusA == 0) && (statusB == 0)) {
			spectrumGenerator.setSeed(computeSeed());
		}
	}

	protected long computeSeed() {
		return (long)(1000000.0 * (positionA + positionB));
	}
	
	private class SimulationVortexDetectorThread extends Thread {
		
		public SimulationVortexDetectorThread() {
			setDaemon(true);
		}
		
		public void run() {
			
			int sleepInterval = 0; // milliseconds
			double sleepTime = 0.0; // seconds
			
			while(true) {
				
				if(acquireState == SimulationVortexDetectorState.ACQUIRING) {
				
					Map<String,Object> valueMap = new HashMap<String,Object>();
					
					elapsedTime += sleepTime;
					
					if(sleepTime != 0.0) {
						int inputCounts = (int)(inputCountRate * sleepTime);
						int outputCounts = spectrumGenerator.generate(inputCounts);
						outputCountRate = outputCounts / sleepTime;
						
						valueMap.put(VALUE_KEY_SPECTRUM, getSpectrum());
						valueMap.put(VALUE_KEY_ELAPSED_TIME, getElapsedTime());
						valueMap.put(VALUE_KEY_ACQUIRE_STATE, getAcquireState());
						valueMap.put(VALUE_KEY_INPUT_COUNT_RATE, getInputCountRate());
						valueMap.put(VALUE_KEY_OUTPUT_COUNT_RATE, getOutputCountRate());
					}
					
					double maximumSleepTime = simulationUpdateInterval / 1000.0;
					
					if(presetTime <= 0.0) {
						sleepTime = maximumSleepTime; 	
					}
					else {
						sleepTime = presetTime - elapsedTime;
						
						if((sleepTime <= 0.0) || (elapsedTime >= MAXIMUM_PRESET_TIME)) {
							acquireState = SimulationVortexDetectorState.DONE;
							valueMap.put(VALUE_KEY_ACQUIRE_STATE, getAcquireState());
							sleepTime = 0.0;
						}
						else if(sleepTime > maximumSleepTime) {
							sleepTime = maximumSleepTime;
						}
					}	
					sleepInterval = (int)(sleepTime * 1000.0); 
					
					if(!valueMap.isEmpty()) {
						SimulationEvent event = new SimulationEvent(SimulationEventType.VALUE_CHANGE, id, valueMap);
						publishEvent(event);
					}
				}
				else if(acquireState == SimulationVortexDetectorState.DONE) {
					
					sleepTime = 0.0;
					sleepInterval = simulationUpdateInterval;
				}
				
				try {
					Thread.sleep(sleepInterval);
				}
				catch(InterruptedException e) {
					log.warn("Simulation update thread interrupted.");
					return;
				}
			}
		}	
	}
	
	private class SpectrumGenerator {
		
		private double channelSize = AVAILABLE_CHANNEL_SIZES[1];
		private int[] spectrum = new int[AVAILABLE_SPECTRUM_SIZES[1]];
		private double[] distribution = new double[getMaximumSpectrumSize()];
		
		private double triggerLevel = 0.0;
		private double triggerLevelSetpoint = 0.0;
		
		private double maximumEnergySetpoint = 0.0;
		
		private long seed = 0L;
		
		public SpectrumGenerator() {
			eraseSpectrum();
			setMaximumEnergySetpoint(0.0);
		}
		
		public int[] getSpectrum() {
			return spectrum;
		}
		
		public double getMaximumEnergy() {
			return (spectrum.length * channelSize) / 1000.0;
		}
		
		public double getMaximumEnergySetpoint() {
			return maximumEnergySetpoint;
		}
		
		public void setMaximumEnergySetpoint(double maximumEnergySetpoint) {
			
			int spectrumSize = 0;
			double channelSize = 0;
			double maximumEnergy = 0.0;
			
			for(int i=0; i<AVAILABLE_CHANNEL_SIZES.length; i++) {
				channelSize = AVAILABLE_CHANNEL_SIZES[i];
				for(int j=0; j<AVAILABLE_SPECTRUM_SIZES.length; j++) {
					spectrumSize = AVAILABLE_SPECTRUM_SIZES[j];
					maximumEnergy = (channelSize * spectrumSize) / 1000.0;
					if(maximumEnergy >= maximumEnergySetpoint) { 
						break;
					}	
				}
				if(maximumEnergy >= maximumEnergySetpoint) {
					break;
				}		
			}
			
			this.spectrum = resize(spectrum, spectrumSize, 0);
			this.channelSize = channelSize;
			
			setTriggerLevelSetpoint(getTriggerLevelSetpoint());
			updateDistribution();
		}
		
		public double getTriggerLevel() {
			return triggerLevel;
		}
		
		public double getTriggerLevelSetpoint() {
			return triggerLevelSetpoint;
		}
		
		public void setTriggerLevelSetpoint(double triggerLevelSetpoint) {
			
			this.triggerLevelSetpoint = triggerLevelSetpoint;
			
			double triggerLevel = 0.0;
			
			for(int i=0; i<=spectrum.length; i++) {
				triggerLevel = (channelSize * i) / 1000.0;
				if(triggerLevel >= triggerLevelSetpoint) {
					break;
				}
			}
			
			this.triggerLevel = triggerLevel;
		}
		
		public void setSeed(long seed) {
			this.seed = seed;
			updateDistribution();
		}
		
		public int generate(int inputCounts) {
			
			int outputCounts = 0;
			
			int triggerIndex = (int)((triggerLevel / channelSize) * 1000.0);
			
			Random random = new Random();
			
			for(int i=0; i<inputCounts; i++) {
				
				double event = random.nextDouble();
				int eventIndex = 0;
				
				for(int j=0; j<distribution.length; j++) {
					if(event <= distribution[j]) {
						eventIndex = j;
						break;
					}
				}
				if((eventIndex >= triggerIndex) && (eventIndex < spectrum.length)) {
					spectrum[eventIndex]++;
					outputCounts++;
				}
			}
			return outputCounts;
		}
		
		public void eraseSpectrum() {
			for(int i=0; i<spectrum.length; i++) {
				spectrum[i] = 0;
			}
		}
		
		protected int getMaximumSpectrumSize() {
			return AVAILABLE_SPECTRUM_SIZES[AVAILABLE_SPECTRUM_SIZES.length - 1];
		}
		
		@SuppressWarnings("unused")
		protected double getMaximumChannelSize() {
			return AVAILABLE_CHANNEL_SIZES[AVAILABLE_CHANNEL_SIZES.length - 1];
		}
		
		protected void updateDistribution() {
			
			Random random = new Random(seed);
			int rawDataFileIndex = random.nextInt(rawDataFileCount);
			
			String rawDataFilePath = rawDataFilePathTemplate.replaceAll("%i", String.valueOf(rawDataFileIndex));
			File rawDataFile = new File(rawDataFilePath);
			
			
			byte[] rawDataBuffer;
			
			try {
				log.info("Loading Vortex detector simulation data: " + rawDataFile.getAbsolutePath());
				FileInputStream fiStream = new FileInputStream(rawDataFile);
				rawDataBuffer = new byte[fiStream.available()];
				fiStream.read(rawDataBuffer);
			}
			catch(FileNotFoundException e) {
				log.warn("Simulation raw data file NOT found!");
				return;
			}
			catch(IOException e) {
				log.warn("Simulation raw data file NOT readable!");
				return;
			}
			
			String textRawData = new String(rawDataBuffer);
			String[] splitTextRawData = textRawData.split("\\s");
			int rawDataSize = Integer.parseInt(splitTextRawData[1]);
			double[] rawData = new double[rawDataSize];
			for(int i=0; i<rawData.length; i++) {
				rawData[i] = Double.parseDouble(splitTextRawData[i+2]);
			}
			
			double channelSizeRatio = channelSize / AVAILABLE_CHANNEL_SIZES[DEFAULT_CHANNEL_SIZE_INDEX];
			
			if(channelSizeRatio < 0) {
				int repeat = (int)((1.0 / channelSizeRatio) / 2);
				for(int i=0; i<repeat; i++) {
					rawData = expand(rawData);
				}
			} 
			else {
				int repeat = (int)(channelSizeRatio / 2);
				for(int i=0; i<repeat; i++) {
					rawData = collapse(rawData);
				}
			}
			
			double[] meanAndRMS = getMeanAndRMS(rawData);
			
			distribution = resize(rawData, distribution.length, Double.MAX_VALUE);
			
			for(int i=0; i<distribution.length; i++) {
				if(distribution[i] == Double.MAX_VALUE) {
					distribution[i] = random.nextDouble() * meanAndRMS[0];
				}
			}
			
			distribution = smooth(distribution, 8);
			
			for(int i=1; i<distribution.length; i++) {
				distribution[i] = distribution[i-1] + distribution[i];
			}
				
			double integral = distribution[distribution.length - 1];
			
			for(int i=0; i<distribution.length; i++) {
				distribution[i] = distribution[i] / integral;
			}
		}
		
		protected double[] resize(double[] array, int length, double pad) {
			
			double[] resizeArray = new double[length];
			
			for(int i=0; i<resizeArray.length; i++) {
				if(i < array.length) {
					resizeArray[i] = array[i];	
				}
				else {
					resizeArray[i] = pad;
				}
			}
			return resizeArray;
		}
		
		protected int[] resize(int[] array, int length, int pad) {
			
			int[] resizeArray = new int[length];
			
			for(int i=0; i<resizeArray.length; i++) {
				if(i < array.length) {
					resizeArray[i] = array[i];	
				}
				else {
					resizeArray[i] = pad;
				}
			}
			return resizeArray;
		}
		
		protected double[] smooth(double[] array, int repeat) {
			
			if(array.length < 2) { 
				return array; 
			}
			
			int lastIndex = array.length - 1;
			double[] smoothArray = array;
			
			for(int i=0; i<repeat; i++) {
			
				double[] tempArray = smoothArray;
				smoothArray = new double[tempArray.length];
				
				for(int j=0; j<=lastIndex; j++) {
					if(j == 0) {
						smoothArray[0] = (0.5 * tempArray[0]) + (0.5 * tempArray[1]);
					} 
					else if(j == lastIndex) {
						smoothArray[lastIndex] = (0.5 * tempArray[lastIndex-1]) + (0.5 * tempArray[lastIndex]);
					}
					else {
						smoothArray[j] = (0.3333 * tempArray[j-1]) + (0.3333 * tempArray[j]) + (0.3333 * tempArray[j+1]);
					}
				}
			}
			return smoothArray;
		}
		
		protected double[] collapse(double[] array) {
			
			double[] collapseArray = new double[array.length / 2];
			
			for(int i=0; i<collapseArray.length; i++) {
				collapseArray[i] = array[(2*i)] + array[(2*i)+1];
			}
			return collapseArray;
		}
		
		protected double[] expand(double[] array) {
			
			double[] expandArray = new double[array.length * 2];
			
			for(int i=0; i<expandArray.length; i++) {
				expandArray[i] = array[i/2] / 2.0;
			}
			return expandArray;
		}
		
		@SuppressWarnings("unused")
		protected double getMinimumGreaterThan(double[] array, double greaterThan) {
			
			int index = getMinimumGreaterThanIndex(array, greaterThan);
			
			if(index < 0) {
				return greaterThan;
			}
			return array[index];
		}
		
		protected int getMinimumGreaterThanIndex(double[] array, double greaterThan) {
			
			int minimumIndex = -1;
			
			for(int i=0; i<array.length; i++) {
				if(array[i] > greaterThan) {
					if(minimumIndex >= 0) {
						if(array[i] < array[minimumIndex]) {
							minimumIndex = i;
						}
					}
					else {
						minimumIndex = i;
					}
				}
			}	
			return minimumIndex;
		}
		
		protected double[] getMeanAndRMS(double[] array) {
			
			if(array.length <= 0) {
				return new double[] { 0.0, 0.0 };
			}
			
			int n = 0;
			double sumx = 0.0;
			double sumx2 = 0.0;
			
			for(double x : array) {
				n += 1;
				sumx += x;
				sumx2 += (x * x);
			}
			
			double mean = sumx / n;
			double variance = sumx2 - ((sumx * sumx) / n);  
			return new double[] { mean, Math.sqrt(variance) };
		}
	}
}
