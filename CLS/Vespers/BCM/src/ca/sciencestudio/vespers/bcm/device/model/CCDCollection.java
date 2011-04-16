/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *
 *
 */
package ca.sciencestudio.vespers.bcm.device.model;

import java.util.HashMap;
import java.util.Map;

import ca.sciencestudio.device.control.component.DeviceComponent;
import ca.sciencestudio.device.control.composite.DeviceComposite;
import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.event.DeviceEventListener;
import ca.sciencestudio.device.control.event.DeviceEventType;

/**
 * Description:
 * 		CCDCollection class.
 *
 * @author Dong Liu
 */
public class CCDCollection extends DeviceComposite<DeviceComponent> {

	/*collect*/

	protected static final String COMPONENT_KEY_EXPOSURETIME = "exposureTime";
	protected static final String COMPONENT_KEY_EXPOSURETIME_RBV = "exposureTimeRBV";
	protected static final String COMPONENT_KEY_ACQUIREPERIOD = "acquirePeriod";
	protected static final String COMPONENT_KEY_ACQUIREPERIOD_RBV = "acquirePeriodRBV";
	protected static final String COMPONENT_KEY_NUMEXPOSURES = "numExposures";
	protected static final String COMPONENT_KEY_NUMEXPOSURES_RBV = "numExposuresRBV";
	protected static final String COMPONENT_KEY_EXPOSURECOUNTER_RBV = "exposureCounter";
	protected static final String COMPONENT_KEY_NUMIMAGES = "numImages";
	protected static final String COMPONENT_KEY_NUMIMAGES_RBV = "numImagesRBV";
	protected static final String COMPONENT_KEY_IMAGECOUNTER_RBV = "imageCounter";
	protected static final String COMPONENT_KEY_NUMACQUISIIONS = "numAcquisitions";
	protected static final String COMPONENT_KEY_NUMACQUISIIONS_RBV = "numAcquisitionsRBV";
	protected static final String COMPONENT_KEY_ACQUISITIONCOUNER_RBV = "acquisitionCounter";
	protected static final String COMPONENT_KEY_IMAGEMODE = "imageMode";
	protected static final String COMPONENT_KEY_IMAGEMODE_RBV = "imageModeRBV";
	protected static final String COMPONENT_KEY_TRIGGERMODE = "triggerMode";
	protected static final String COMPONENT_KEY_TRIGGERMODE_RBV = "triggerModeRBV";
	protected static final String COMPONENT_KEY_ACQUIRE = "acquire";
	protected static final String COMPONENT_KEY_DETECTORSTATE_RBV = "detectorStateRBV";
	protected static final String COMPONENT_KEY_TIMEREMAINING_RBV = "timeRemainingRBV";
	protected static final String COMPONENT_KEY_IMAGERATE_RBV = "imageRateRBV";


	protected static final String VALUE_KEY_EXPOSURETIME = "exposureTime";
	protected static final String VALUE_KEY_EXPOSURETIME_RBV = "exposureTimeRBV";
	protected static final String VALUE_KEY_ACQUIREPERIOD = "acquirePeriod";
	protected static final String VALUE_KEY_ACQUIREPERIOD_RBV = "acquirePeriodRBV";
	protected static final String VALUE_KEY_NUMEXPOSURES = "numExposures";
	protected static final String VALUE_KEY_NUMEXPOSURES_RBV = "numExposuresRBV";
	protected static final String VALUE_KEY_EXPOSURECOUNTER_RBV = "exposureCounter";
	protected static final String VALUE_KEY_NUMIMAGES = "numImages";
	protected static final String VALUE_KEY_NUMIMAGES_RBV = "numImagesRBV";
	protected static final String VALUE_KEY_IMAGECOUNTER_RBV = "imageCounter";
	protected static final String VALUE_KEY_NUMACQUISITIONS = "numAcquisitions";
	protected static final String VALUE_KEY_NUMACQUISITIONS_RBV = "numAcquisitionsRBV";
	protected static final String VALUE_KEY_ACQUISITIONCOUNER_RBV = "acquisitionCounter";
	protected static final String VALUE_KEY_IMAGEMODE = "imageMode";
	protected static final String VALUE_KEY_IMAGEMODE_RBV = "imageModeRBV";
	protected static final String VALUE_KEY_TRIGGERMODE = "triggerMode";
	protected static final String VALUE_KEY_TRIGGERMODE_RBV = "triggerModeRBV";
	protected static final String VALUE_KEY_ACQUIRE = "acquire";
	protected static final String VALUE_KEY_DETECTORSTATE_RBV = "detectorStateRBV";
	protected static final String VALUE_KEY_TIMEREMAINING_RBV = "timeRemainingRBV";
	protected static final String VALUE_KEY_IMAGERATE_RBV = "imageRateRBV";




	// Unknown for exceptions
	protected static final String[] CCD_DETECTOR_STATES = {"Unavailable", "Idle", "Acquire", "Readout", "Correct", "Saving", "Aborting", "Error", "Waiting" };
	protected static final String[] NO_YES = {"Unavailable", "No", "Yes"};
	protected static final String[] ACQUIRE = {"Unavailable", "Done", "Acquire"};
	protected static final String[] TRIGGER_MODE = {"Unavailable", "Free run", "Ext. sync", "Bulb trig.", "Single trig."};
	protected static final String[] IMAGE_MODE = {"Unavailable", "Normal", "Continuous", "Focus"};


	private CcdDetectorDeviceEventListener listener;

	public CCDCollection(String id, Map<String,DeviceComponent> components) {
		super(id, components);
		initCCDDetectorDeviceEventListener();

	}

	protected void initCCDDetectorDeviceEventListener() {
		listener = new CcdDetectorDeviceEventListener();
		for(Map.Entry<String,DeviceComponent> entry : getComponents().entrySet()) {
			entry.getValue().addEventListener(listener);
		}
	}

	public Object getValue() {
		Map<String,Object> value = new HashMap<String,Object>();
		value.put(VALUE_KEY_EXPOSURETIME, getDoubleFromComponent(COMPONENT_KEY_EXPOSURETIME, VALUE_KEY_EXPOSURETIME));
		value.put(VALUE_KEY_EXPOSURETIME_RBV, getDoubleFromComponent(COMPONENT_KEY_EXPOSURETIME_RBV, VALUE_KEY_EXPOSURETIME_RBV));
		value.put(VALUE_KEY_ACQUIREPERIOD, getDoubleFromComponent(COMPONENT_KEY_ACQUIREPERIOD, VALUE_KEY_ACQUIREPERIOD));
		value.put(VALUE_KEY_ACQUIREPERIOD_RBV, getDoubleFromComponent(COMPONENT_KEY_ACQUIREPERIOD_RBV, VALUE_KEY_ACQUIREPERIOD_RBV));
		value.put(VALUE_KEY_NUMEXPOSURES, getIntFromComponent(COMPONENT_KEY_NUMEXPOSURES, VALUE_KEY_NUMEXPOSURES));
		value.put(VALUE_KEY_NUMEXPOSURES_RBV, getIntFromComponent(COMPONENT_KEY_NUMEXPOSURES_RBV, VALUE_KEY_NUMEXPOSURES_RBV));
		value.put(VALUE_KEY_EXPOSURECOUNTER_RBV, getIntFromComponent(COMPONENT_KEY_EXPOSURECOUNTER_RBV, VALUE_KEY_EXPOSURECOUNTER_RBV));
		value.put(VALUE_KEY_NUMIMAGES, getIntFromComponent(COMPONENT_KEY_NUMIMAGES, VALUE_KEY_NUMIMAGES));
		value.put(VALUE_KEY_NUMIMAGES_RBV, getIntFromComponent(COMPONENT_KEY_NUMIMAGES_RBV, VALUE_KEY_NUMIMAGES_RBV));
		value.put(VALUE_KEY_IMAGECOUNTER_RBV, getIntFromComponent(COMPONENT_KEY_IMAGECOUNTER_RBV, VALUE_KEY_IMAGECOUNTER_RBV));
		value.put(VALUE_KEY_NUMACQUISITIONS, getIntFromComponent(COMPONENT_KEY_NUMACQUISIIONS, VALUE_KEY_NUMACQUISITIONS));
		value.put(VALUE_KEY_NUMACQUISITIONS_RBV, getIntFromComponent(COMPONENT_KEY_NUMACQUISIIONS_RBV, VALUE_KEY_NUMACQUISITIONS_RBV));
		value.put(VALUE_KEY_ACQUISITIONCOUNER_RBV, getIntFromComponent(COMPONENT_KEY_ACQUISITIONCOUNER_RBV, VALUE_KEY_ACQUISITIONCOUNER_RBV));
		value.put(VALUE_KEY_IMAGEMODE, getEnumString(getShortFromComponent(COMPONENT_KEY_IMAGEMODE, VALUE_KEY_IMAGEMODE), IMAGE_MODE));
		value.put(VALUE_KEY_IMAGEMODE_RBV, getEnumString(getShortFromComponent(COMPONENT_KEY_IMAGEMODE_RBV, VALUE_KEY_IMAGEMODE_RBV), IMAGE_MODE));
		value.put(VALUE_KEY_TRIGGERMODE, getEnumString(getShortFromComponent(COMPONENT_KEY_TRIGGERMODE, VALUE_KEY_TRIGGERMODE), TRIGGER_MODE));
		value.put(VALUE_KEY_TRIGGERMODE_RBV, getEnumString(getShortFromComponent(COMPONENT_KEY_TRIGGERMODE_RBV, VALUE_KEY_TRIGGERMODE_RBV), TRIGGER_MODE));
		value.put(VALUE_KEY_ACQUIRE, getEnumString(getShortFromComponent(COMPONENT_KEY_ACQUIRE, VALUE_KEY_ACQUIRE), ACQUIRE));
		value.put(VALUE_KEY_DETECTORSTATE_RBV, getEnumString(getShortFromComponent(COMPONENT_KEY_DETECTORSTATE_RBV, VALUE_KEY_DETECTORSTATE_RBV), CCD_DETECTOR_STATES));
		value.put(VALUE_KEY_TIMEREMAINING_RBV, getDoubleFromComponent(COMPONENT_KEY_TIMEREMAINING_RBV, VALUE_KEY_TIMEREMAINING_RBV));
		value.put(VALUE_KEY_IMAGERATE_RBV, getDoubleFromComponent(COMPONENT_KEY_IMAGERATE_RBV, VALUE_KEY_IMAGERATE_RBV));
		return value;
	}

	public String getEnumString(short index, String[] elements) {
		int i = index + 1;
		return (i >= 0 && i < elements.length) ? elements[i]:(new String("Unknow"));
	}


	public String getStringFromComponent(String compomentKey, String valueKey) {
		DeviceComponent deviceComponent = getComponent(compomentKey);
		Object value = deviceComponent.getValue();
		try {
			String[] strAry = (String[]) value;
			return strAry[0];
		}
		catch(ClassCastException e) {
			log.warn("Value for '" + valueKey + "', is not expected class (String[]).");
			return null;
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + valueKey + "', is null (Disconnected EpicsDevice?).");
			return null;
		}
	}


	public String getStringFromComponentValue(Object value, String valueKey) {
		try {
			String[] strAry = (String[]) value;
			return strAry[0];
		}
		catch(ClassCastException e) {
			log.warn("Value for '" + valueKey + "', is not expected class (String[]).");
			return null;
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + valueKey + "', is null (Disconnected EpicsDevice?).");
			return null;
		}
	}

	/**
	 * @param compomentKey
	 * @param valueKey
	 * @return a short number for a binary or enumerate PV value. -1 represents an invalid value.
	 */
	public short getShortFromComponent(String compomentKey, String valueKey) {
		DeviceComponent deviceComponent = getComponent(compomentKey);
		Object value = deviceComponent.getValue();
		try {
			short[] intAry = (short[]) value;
			return intAry[0];
		}
		catch(ClassCastException e) {
			log.warn("Value for '" + valueKey + "', is not expected class (short[]).");
			return -1;
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + valueKey + "', is null (Disconnected EpicsDevice?).");
			return -1;

		}
	}

	public short getShortFromComponentValue(Object value, String valueKey) {
		try {
			short[] intAry = (short[]) value;
			System.err.println(intAry[0]);
			return intAry[0];
		}
		catch(ClassCastException e) {
			log.warn("Value for '" + valueKey + "', is not expected class (short[]).");
			return -1;
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + valueKey + "', is null (Disconnected EpicsDevice?).");
			return -1;
		}
	}

	/**
	 * @param compomentKey
	 * @param valueKey
	 * @return an int converted from a DBR_LONG array
	 */
	public long getIntFromComponent(String compomentKey, String valueKey) {
		DeviceComponent deviceComponent = getComponent(compomentKey);
		Object value = deviceComponent.getValue();
		try {
			int[] intAry = (int[]) value;
			return intAry[0];
		}
		catch(ClassCastException e) {
			log.warn("Value for '" + valueKey + "', is not expected class (int[]).");
			return -1;
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + valueKey + "', is null (Disconnected EpicsDevice?).");
			return -1;
		}
	}

	public long getIntFromComponentValue(Object value, String valueKey) {
		try {
			int[] intAry = (int[]) value;
			return intAry[0];
		}
		catch(ClassCastException e) {
			log.warn("Value for '" + valueKey + "', is not expected class (int[]).");
			return -1;
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + valueKey + "', is null (Disconnected EpicsDevice?).");
			return -1;
		}
	}


	public double getDoubleFromComponent(String compomentKey, String valueKey) {
		DeviceComponent deviceComponent = getComponent(compomentKey);
		Object value = deviceComponent.getValue();
		try {
			double[] dbAry = (double[]) value;
			return dbAry[0];
		}
		catch(ClassCastException e) {
			log.warn("Value for '" + valueKey + "', is not expected class (double[]).");
			return Double.NaN;
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + valueKey + "', is null (Disconnected EpicsDevice?).");
			return Double.NaN;
		}
	}

	public double getDoubleFromComponentValue(Object value, String valueKey) {
		try {
			double[] dbAry = (double[]) value;
			return dbAry[0];
		}
		catch(ClassCastException e) {
			log.warn("Value for '" + valueKey + "', is not expected class (double[]).");
			return Double.NaN;
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + valueKey + "', is null (Disconnected EpicsDevice?).");
			return Double.NaN;
		}
	}

	@SuppressWarnings("unchecked")
	public void setValue(Object value) {
		try {
			Map<String,Object> valueMap = (Map<String,Object>) value;

			if(valueMap.containsKey(VALUE_KEY_EXPOSURETIME)) {
				setDoubleToComponent(valueMap.get(VALUE_KEY_EXPOSURETIME), COMPONENT_KEY_EXPOSURETIME);

			}

			if(valueMap.containsKey(VALUE_KEY_TRIGGERMODE)) {
				setShortToComponent(valueMap.get(VALUE_KEY_TRIGGERMODE), COMPONENT_KEY_TRIGGERMODE);

			}

			if(valueMap.containsKey(VALUE_KEY_ACQUIRE)) {
				if (((Integer) valueMap.get(VALUE_KEY_ACQUIRE)).shortValue() == 1) {
					acquireStart();
				} else {
					acquireStop();
				}
			}


		}
		catch(ClassCastException e) {
			log.warn("Set value from a map. ", e);
		}
	}


	public void acquireStart() {
		DeviceComponent deviceComponent = getComponent(COMPONENT_KEY_ACQUIRE);
		deviceComponent.setValue(new short[] { 1 });
	}

	public void acquireStop() {
		DeviceComponent deviceComponent = getComponent(COMPONENT_KEY_ACQUIRE);
		deviceComponent.setValue(new short[] { 0 });
	}


	private void setDoubleToComponent(Object object,
			String componentKey) {
		DeviceComponent deviceComponent = getComponent(componentKey);
		try{
			double[] value = new double[] {(Double)object};
			deviceComponent.setValue(value);
		} catch (ClassCastException e) {
			log.warn("Set value of " + componentKey + " wrong class, expecting Double.");
		}

	}

	private void setShortToComponent(Object object,
			String componentKey) {
		DeviceComponent deviceComponent = getComponent(componentKey);
		try{
			short[] value = new short[] {((Integer)object).shortValue()};
			deviceComponent.setValue(value);
		} catch (ClassCastException e) {
			log.warn("Set value of " + componentKey + " wrong class, expecting Integer.");
		}

	}

	protected void initDeviceEventListeners() {
		listener = new CcdDetectorDeviceEventListener();
		for(Map.Entry<String,DeviceComponent> entry : getComponents().entrySet()) {
			entry.getValue().addEventListener(listener);
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

	protected class CcdDetectorDeviceEventListener implements DeviceEventListener {

		public CcdDetectorDeviceEventListener() {}

		public void handleEvent(DeviceEvent event) {
			String deviceId = event.getDeviceId();
			String componentKey = getComponentKeyByDeviceId(deviceId);

			DeviceEventType deviceEventType = event.getDeviceEventType();

			switch (deviceEventType) {
				case VALUE_CHANGE:

					Object value = event.getValue();

					HashMap<String,Object> newValueMap = new HashMap<String,Object>();
					if (COMPONENT_KEY_EXPOSURETIME.equals(componentKey)){
						newValueMap.put(VALUE_KEY_EXPOSURETIME, getDoubleFromComponentValue(value, VALUE_KEY_EXPOSURETIME));
					}
					else if (COMPONENT_KEY_EXPOSURETIME_RBV.equals(componentKey)){
						newValueMap.put(VALUE_KEY_EXPOSURETIME_RBV, getDoubleFromComponentValue(value, VALUE_KEY_EXPOSURETIME_RBV));
					}
					else if (COMPONENT_KEY_ACQUIREPERIOD.equals(componentKey)){
						newValueMap.put(VALUE_KEY_ACQUIREPERIOD, getDoubleFromComponentValue(value, VALUE_KEY_ACQUIREPERIOD));
					}
					else if (COMPONENT_KEY_ACQUIREPERIOD_RBV.equals(componentKey)){
						newValueMap.put(VALUE_KEY_ACQUIREPERIOD_RBV, getDoubleFromComponentValue(value, VALUE_KEY_ACQUIREPERIOD_RBV));
					}
					else if (COMPONENT_KEY_NUMEXPOSURES.equals(componentKey)) {
						newValueMap.put(VALUE_KEY_NUMEXPOSURES, getIntFromComponentValue(value, VALUE_KEY_NUMEXPOSURES));
					}
					else if (COMPONENT_KEY_NUMEXPOSURES_RBV.equals(componentKey)){
						newValueMap.put(VALUE_KEY_NUMEXPOSURES_RBV, getIntFromComponentValue(value, VALUE_KEY_NUMEXPOSURES_RBV));
					}
					else if (COMPONENT_KEY_EXPOSURECOUNTER_RBV.equals(componentKey)){
						newValueMap.put(VALUE_KEY_EXPOSURECOUNTER_RBV, getIntFromComponentValue(value, VALUE_KEY_EXPOSURECOUNTER_RBV));
					}
					else if (COMPONENT_KEY_NUMIMAGES.equals(componentKey)){
						newValueMap.put(VALUE_KEY_NUMIMAGES, getIntFromComponentValue(value, VALUE_KEY_NUMIMAGES));
					}
					else if (COMPONENT_KEY_NUMIMAGES_RBV.equals(componentKey)){
						newValueMap.put(VALUE_KEY_NUMIMAGES_RBV, getIntFromComponentValue(value, VALUE_KEY_NUMIMAGES_RBV));
					}
					else if (COMPONENT_KEY_IMAGECOUNTER_RBV.equals(componentKey)){
						newValueMap.put(VALUE_KEY_IMAGECOUNTER_RBV, getIntFromComponentValue(value, VALUE_KEY_IMAGECOUNTER_RBV));
					}
					else if (COMPONENT_KEY_NUMACQUISIIONS.equals(componentKey)){
						newValueMap.put(VALUE_KEY_NUMACQUISITIONS, getIntFromComponentValue(value, VALUE_KEY_NUMACQUISITIONS));
					}
					else if (COMPONENT_KEY_NUMACQUISIIONS_RBV.equals(componentKey)){
						newValueMap.put(VALUE_KEY_NUMACQUISITIONS_RBV, getIntFromComponentValue(value, VALUE_KEY_NUMACQUISITIONS_RBV));
					}
					else if (COMPONENT_KEY_ACQUISITIONCOUNER_RBV.equals(componentKey)){
						newValueMap.put(VALUE_KEY_ACQUISITIONCOUNER_RBV, getIntFromComponentValue(value, VALUE_KEY_ACQUISITIONCOUNER_RBV));
					}
					else if (COMPONENT_KEY_IMAGEMODE.equals(componentKey)){
						newValueMap.put(VALUE_KEY_IMAGEMODE, getEnumString(getShortFromComponent(COMPONENT_KEY_IMAGEMODE, VALUE_KEY_IMAGEMODE), IMAGE_MODE));
					}
					else if (COMPONENT_KEY_IMAGEMODE_RBV.equals(componentKey)){
						newValueMap.put(VALUE_KEY_IMAGEMODE_RBV, getEnumString(getShortFromComponent(COMPONENT_KEY_IMAGEMODE_RBV, VALUE_KEY_IMAGEMODE_RBV), IMAGE_MODE));
					}
					else if (COMPONENT_KEY_TRIGGERMODE.equals(componentKey)){
						newValueMap.put(VALUE_KEY_TRIGGERMODE, getEnumString(getShortFromComponent(COMPONENT_KEY_TRIGGERMODE, VALUE_KEY_TRIGGERMODE), TRIGGER_MODE));
					}
					else if (COMPONENT_KEY_TRIGGERMODE_RBV.equals(componentKey)){
						newValueMap.put(VALUE_KEY_TRIGGERMODE_RBV, getEnumString(getShortFromComponent(COMPONENT_KEY_TRIGGERMODE_RBV, VALUE_KEY_TRIGGERMODE_RBV), TRIGGER_MODE));
					}
					else if (COMPONENT_KEY_ACQUIRE.equals(componentKey)){
						newValueMap.put(VALUE_KEY_ACQUIRE, getEnumString(getShortFromComponent(COMPONENT_KEY_ACQUIRE, VALUE_KEY_ACQUIRE), ACQUIRE));
					}
					else if (COMPONENT_KEY_DETECTORSTATE_RBV.equals(componentKey)){
						newValueMap.put(VALUE_KEY_DETECTORSTATE_RBV, getEnumString(getShortFromComponent(COMPONENT_KEY_DETECTORSTATE_RBV, VALUE_KEY_DETECTORSTATE_RBV), CCD_DETECTOR_STATES));
					}
					else if (COMPONENT_KEY_TIMEREMAINING_RBV.equals(componentKey)){
						newValueMap.put(VALUE_KEY_TIMEREMAINING_RBV, getDoubleFromComponentValue(value, VALUE_KEY_TIMEREMAINING_RBV));
					}
					else if (COMPONENT_KEY_IMAGERATE_RBV.equals(componentKey)){
						newValueMap.put(VALUE_KEY_IMAGERATE_RBV, getDoubleFromComponentValue(value, VALUE_KEY_IMAGERATE_RBV));
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
