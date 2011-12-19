/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
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
 *
 * @author Dong Liu
 */
public class CCDSetup extends DeviceComposite<DeviceComponent> {

	/*setup*/


//	protected static final String COMPONENT_KEY_MANUFACTURER = "manufacturer";
//	protected static final String COMPONENT_KEY_MODEL = "model";
	protected static final String COMPONENT_KEY_PORTNAME_RBV = "portNameRBV";
	protected static final String COMPONENT_KEY_AsynIO_CNNT = "asynIOCNNT";
	protected static final String COMPONENT_KEY_TEMPERATURE = "temperature";
	protected static final String COMPONENT_KEY_TEMPERATURE_RBV = "temperatureRBV";
	protected static final String COMPONENT_KEY_BINX = "binX";
	protected static final String COMPONENT_KEY_BINY = "binY";
	protected static final String COMPONENT_KEY_BINX_RBV = "binXRBV";
	protected static final String COMPONENT_KEY_BINY_RBV = "binYRBV";
	protected static final String COMPONENT_KEY_IMAGEREVERSEX_RBV = "imageReverseXRBV";
	protected static final String COMPONENT_KEY_IMAGEREVERSEY_RBV = "imageReverseYRBV";
	protected static final String COMPONENT_KEY_AUTODATATYPE = "autoDataType";
	protected static final String COMPONENT_KEY_AUTODATATYPE_RBV = "autoDataTypeRBV";
	protected static final String COMPONENT_KEY_DATATYPE_RBV = "dataTypeRBV";
	protected static final String COMPONENT_KEY_DATATYPE = "dataType";
	protected static final String COMPONENT_KEY_GAIN = "gain";
	protected static final String COMPONENT_KEY_GAIN_RBV = "gainRBV";
	protected static final String COMPONENT_KEY_IMAGESIZE_RBV = "imageSizeRBV";
	protected static final String COMPONENT_KEY_REGIONSTARTX = "regionStartX";
	protected static final String COMPONENT_KEY_REGIONSTARTY = "regionStartY";
	protected static final String COMPONENT_KEY_REGIONSIZEX = "regionSizeX";
	protected static final String COMPONENT_KEY_REGIONSIZEY = "regionSizeY";
	protected static final String COMPONENT_KEY_REGIONSIZEX_RBV = "regionSizeXRBV";
	protected static final String COMPONENT_KEY_REGIONSIZEY_RBV = "regionSizeYRBV";


//	public static final String VALUE_KEY_MANUFACTURER = "manufacturer";
//	public static final String VALUE_KEY_MODEL = "model";
	public static final String VALUE_KEY_PORTNAME_RBV = "portNameRBV";
	public static final String VALUE_KEY_AsynIO_CNNT = "asynIOCNNT";
	public static final String VALUE_KEY_TEMPERATURE = "temperature";
	public static final String VALUE_KEY_TEMPERATURE_RBV = "temperatureRBV";
	public static final String VALUE_KEY_BINX = "binX";
	public static final String VALUE_KEY_BINY = "binY";
	public static final String VALUE_KEY_BINX_RBV = "binXRBV";
	public static final String VALUE_KEY_BINY_RBV = "binYRBV";
	public static final String VALUE_KEY_IMAGEREVERSEX_RBV = "imageReverseXRBV";
	public static final String VALUE_KEY_IMAGEREVERSEY_RBV = "imageReverseYRBV";
	public static final String VALUE_KEY_AUTODATATYPE = "autoDataType";
	public static final String VALUE_KEY_AUTODATATYPE_RBV = "autoDataTypeRBV";
	public static final String VALUE_KEY_DATATYPE_RBV = "dataTypeRBV";
	public static final String VALUE_KEY_DATATYPE = "dataType";
	public static final String VALUE_KEY_GAIN = "gain";
	public static final String VALUE_KEY_GAIN_RBV = "gainRBV";
	public static final String VALUE_KEY_IMAGESIZE_RBV = "imageSizeRBV";
	public static final String VALUE_KEY_REGIONSTARTX = "regionStartX";
	public static final String VALUE_KEY_REGIONSTARTY = "regionStartY";
	public static final String VALUE_KEY_REGIONSIZEX = "regionSizeX";
	public static final String VALUE_KEY_REGIONSIZEY = "regionSizeY";
	public static final String VALUE_KEY_REGIONSIZEX_RBV = "regionSizeXRBV";
	public static final String VALUE_KEY_REGIONSIZEY_RBV = "regionSizeYRBV";


	// Unknown for exceptions
	protected static final String[] NO_YES = {"Unavailable", "No", "Yes"};
	protected static final String[] DATA_TYPE = {"Unavailable", "Int8", "UInt8", "Int16", "UInt16", "Int32", "UInt32", "Float32", "Float64"};


	private CcdDetectorDeviceEventListener listener;

	public CCDSetup(String id, Map<String,DeviceComponent> components) {
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
//		value.put(VALUE_KEY_MANUFACTURER, getManufacturer());
//		value.put(VALUE_KEY_MODEL, getModel());
		value.put(VALUE_KEY_PORTNAME_RBV, getStringFromComponent(COMPONENT_KEY_PORTNAME_RBV, VALUE_KEY_PORTNAME_RBV));
		value.put(VALUE_KEY_AsynIO_CNNT, getEnumString(getShortFromComponent(COMPONENT_KEY_AsynIO_CNNT, VALUE_KEY_AsynIO_CNNT), NO_YES));
//		value.put(VALUE_KEY_TEMPERATURE, getDoubleFromComponent(COMPONENT_KEY_TEMPERATURE, VALUE_KEY_TEMPERATURE));
		value.put(VALUE_KEY_TEMPERATURE_RBV, getDoubleFromComponent(COMPONENT_KEY_TEMPERATURE_RBV, VALUE_KEY_TEMPERATURE_RBV));
		value.put(VALUE_KEY_BINX_RBV, getIntFromComponent(COMPONENT_KEY_BINX_RBV, VALUE_KEY_BINX_RBV));
		value.put(VALUE_KEY_BINY_RBV, getIntFromComponent(COMPONENT_KEY_BINY_RBV, VALUE_KEY_BINY_RBV));
		value.put(VALUE_KEY_IMAGEREVERSEX_RBV, getEnumString(getShortFromComponent(COMPONENT_KEY_IMAGEREVERSEX_RBV, VALUE_KEY_IMAGEREVERSEX_RBV), NO_YES));
		value.put(VALUE_KEY_IMAGEREVERSEY_RBV, getEnumString(getShortFromComponent(COMPONENT_KEY_IMAGEREVERSEY_RBV, VALUE_KEY_IMAGEREVERSEY_RBV), NO_YES));
//		value.put(VALUE_KEY_AUTODATATYPE, getEnumString(getShortFromComponent(COMPONENT_KEY_AUTODATATYPE, VALUE_KEY_AUTODATATYPE), NO_YES));
		value.put(VALUE_KEY_AUTODATATYPE_RBV, getEnumString(getShortFromComponent(COMPONENT_KEY_AUTODATATYPE_RBV, VALUE_KEY_AUTODATATYPE_RBV), NO_YES));
		value.put(VALUE_KEY_DATATYPE_RBV, getEnumString(getShortFromComponent(COMPONENT_KEY_DATATYPE_RBV, VALUE_KEY_DATATYPE_RBV), DATA_TYPE));
//		value.put(VALUE_KEY_DATATYPE, getEnumString(getShortFromComponent(COMPONENT_KEY_DATATYPE, VALUE_KEY_DATATYPE), DATA_TYPE));
//		value.put(VALUE_KEY_GAIN, getDoubleFromComponent(COMPONENT_KEY_GAIN, VALUE_KEY_GAIN));
		value.put(VALUE_KEY_GAIN_RBV, getDoubleFromComponent(COMPONENT_KEY_GAIN_RBV, VALUE_KEY_GAIN_RBV));
		value.put(VALUE_KEY_IMAGESIZE_RBV, getIntFromComponent(COMPONENT_KEY_IMAGESIZE_RBV, VALUE_KEY_IMAGESIZE_RBV));
		value.put(VALUE_KEY_REGIONSTARTX, getIntFromComponent(COMPONENT_KEY_REGIONSTARTX, VALUE_KEY_REGIONSTARTX));
		value.put(VALUE_KEY_REGIONSTARTY, getIntFromComponent(COMPONENT_KEY_REGIONSTARTY, VALUE_KEY_REGIONSTARTY));
//		value.put(VALUE_KEY_REGIONSIZEX, getIntFromComponent(COMPONENT_KEY_REGIONSIZEX, VALUE_KEY_REGIONSIZEX));
//		value.put(VALUE_KEY_REGIONSIZEY, getIntFromComponent(COMPONENT_KEY_REGIONSIZEY, VALUE_KEY_REGIONSIZEY));
		value.put(VALUE_KEY_REGIONSIZEX_RBV, getIntFromComponent(COMPONENT_KEY_REGIONSIZEX_RBV, VALUE_KEY_REGIONSIZEX_RBV));
		value.put(VALUE_KEY_REGIONSIZEY_RBV, getIntFromComponent(COMPONENT_KEY_REGIONSIZEY_RBV, VALUE_KEY_REGIONSIZEY_RBV));
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
	public int getIntFromComponent(String compomentKey, String valueKey) {
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

	public int getIntFromComponentValue(Object value, String valueKey) {
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
			Map<String,Object> valueMap = (Map<String,Object>) value;
			if(valueMap.containsKey(VALUE_KEY_TEMPERATURE)){
				setDoubleToComponent(valueMap.get(VALUE_KEY_TEMPERATURE), COMPONENT_KEY_TEMPERATURE);
			}
			if(valueMap.containsKey(VALUE_KEY_BINX)){
				setIntToComponent(((Integer)valueMap.get(VALUE_KEY_BINX)).intValue(), COMPONENT_KEY_BINX);
			}
			if(valueMap.containsKey(VALUE_KEY_BINY)){
				setIntToComponent(((Integer)valueMap.get(VALUE_KEY_BINY)).intValue(), COMPONENT_KEY_BINY);
			}
			if(valueMap.containsKey(VALUE_KEY_REGIONSTARTX)){
				setIntToComponent(((Integer)valueMap.get(VALUE_KEY_REGIONSTARTX)).intValue(), COMPONENT_KEY_REGIONSTARTX);
			}
			if(valueMap.containsKey(VALUE_KEY_REGIONSTARTY)){
				setIntToComponent(((Integer)valueMap.get(VALUE_KEY_REGIONSTARTY)).intValue(), COMPONENT_KEY_REGIONSTARTY);
			}
			if(valueMap.containsKey(VALUE_KEY_REGIONSIZEX)){
				setIntToComponent(((Integer)valueMap.get(VALUE_KEY_REGIONSIZEX)).intValue(), COMPONENT_KEY_REGIONSIZEX);
			}
			if(valueMap.containsKey(VALUE_KEY_REGIONSIZEY)){
				setIntToComponent(((Integer)valueMap.get(VALUE_KEY_REGIONSIZEY)).intValue(), COMPONENT_KEY_REGIONSIZEY);
			}

		}

	private void setIntToComponent(Object object, String componentKey) {
		DeviceComponent deviceComponent = getComponent(componentKey);
		try {
			deviceComponent.setValue(new int[] { ((Integer) object).intValue() });
		} catch (ClassCastException e) {
			log.warn("Set value of " + componentKey + ": wrong class expecting Integer.");
		}
	}

	private void setDoubleToComponent(Object object, String componentKey) {
		DeviceComponent deviceComponent = getComponent(componentKey);
		try{
			deviceComponent.setValue(new double[] { ((Double) object).doubleValue() });
		} catch (ClassCastException e) {
			log.warn("Set value of " + componentKey + ": wrong class expecting Double.");
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

					if(COMPONENT_KEY_AsynIO_CNNT.equals(componentKey)) {
						newValueMap.put(VALUE_KEY_AsynIO_CNNT, getEnumString(getShortFromComponent(COMPONENT_KEY_AsynIO_CNNT, VALUE_KEY_AsynIO_CNNT), NO_YES));
					}
					else if(COMPONENT_KEY_TEMPERATURE_RBV.equals(componentKey)) {
						newValueMap.put(VALUE_KEY_TEMPERATURE_RBV, getDoubleFromComponentValue(value, VALUE_KEY_TEMPERATURE_RBV));
					}
					else if(COMPONENT_KEY_TEMPERATURE.equals(componentKey)) {
						newValueMap.put(VALUE_KEY_TEMPERATURE, getDoubleFromComponentValue(value, VALUE_KEY_TEMPERATURE));
					}
					else if(COMPONENT_KEY_BINX_RBV.equals(componentKey)){
						newValueMap.put(VALUE_KEY_BINX_RBV, getIntFromComponentValue(value, VALUE_KEY_BINX_RBV));
					}
					else if(COMPONENT_KEY_BINY_RBV.equals(componentKey)){
						newValueMap.put(VALUE_KEY_BINY_RBV, getIntFromComponentValue(value, VALUE_KEY_BINY_RBV));
					}
					else if(COMPONENT_KEY_IMAGEREVERSEX_RBV.equals(componentKey)){
						newValueMap.put(VALUE_KEY_IMAGEREVERSEX_RBV, getEnumString(getShortFromComponent(COMPONENT_KEY_IMAGEREVERSEX_RBV, VALUE_KEY_IMAGEREVERSEX_RBV), NO_YES));
					}
					else if(COMPONENT_KEY_IMAGEREVERSEY_RBV.equals(componentKey)){
						newValueMap.put(VALUE_KEY_IMAGEREVERSEY_RBV, getEnumString(getShortFromComponent(COMPONENT_KEY_IMAGEREVERSEY_RBV, VALUE_KEY_IMAGEREVERSEY_RBV), NO_YES));
					}
					else if(COMPONENT_KEY_AUTODATATYPE.equals(componentKey)){
						newValueMap.put(VALUE_KEY_AUTODATATYPE, getEnumString(getShortFromComponent(COMPONENT_KEY_AUTODATATYPE, VALUE_KEY_AUTODATATYPE), NO_YES));
					}
					else if(COMPONENT_KEY_AUTODATATYPE_RBV.equals(componentKey)){
						newValueMap.put(VALUE_KEY_AUTODATATYPE_RBV, getEnumString(getShortFromComponent(COMPONENT_KEY_AUTODATATYPE_RBV, VALUE_KEY_AUTODATATYPE_RBV), NO_YES));
					}
					else if(COMPONENT_KEY_DATATYPE_RBV.equals(componentKey)){
						newValueMap.put(VALUE_KEY_DATATYPE_RBV, getEnumString(getShortFromComponent(COMPONENT_KEY_DATATYPE_RBV, VALUE_KEY_DATATYPE_RBV), DATA_TYPE));
					}
					else if(COMPONENT_KEY_DATATYPE.equals(componentKey)){
						newValueMap.put(VALUE_KEY_DATATYPE, getEnumString(getShortFromComponent(COMPONENT_KEY_DATATYPE, VALUE_KEY_DATATYPE), DATA_TYPE));
					}
					else if(COMPONENT_KEY_GAIN.equals(componentKey)){
						newValueMap.put(VALUE_KEY_GAIN, getDoubleFromComponentValue(value, VALUE_KEY_GAIN));
					}
					else if(COMPONENT_KEY_GAIN_RBV.equals(componentKey)){
						newValueMap.put(VALUE_KEY_GAIN_RBV, getDoubleFromComponentValue(value, VALUE_KEY_GAIN_RBV));
					}
					else if(COMPONENT_KEY_IMAGESIZE_RBV.equals(componentKey)){
						newValueMap.put(VALUE_KEY_IMAGESIZE_RBV, getIntFromComponentValue(value, VALUE_KEY_IMAGESIZE_RBV));
					}
					else if(COMPONENT_KEY_REGIONSTARTX.equals(componentKey)){
						newValueMap.put(VALUE_KEY_REGIONSTARTX, getIntFromComponentValue(value, VALUE_KEY_REGIONSTARTX));
					}
					else if(COMPONENT_KEY_REGIONSTARTY.equals(componentKey)){
						newValueMap.put(VALUE_KEY_REGIONSTARTY, getIntFromComponentValue(value, VALUE_KEY_REGIONSTARTY));
					}
					else if(COMPONENT_KEY_REGIONSIZEX.equals(componentKey)){
						newValueMap.put(VALUE_KEY_REGIONSIZEX, getIntFromComponentValue(value, VALUE_KEY_REGIONSIZEX));
					}
					else if(COMPONENT_KEY_REGIONSIZEX_RBV.equals(componentKey)){
						newValueMap.put(VALUE_KEY_REGIONSIZEX_RBV, getIntFromComponentValue(value, VALUE_KEY_REGIONSIZEX_RBV));
					}
					else if(COMPONENT_KEY_REGIONSIZEY.equals(componentKey)){
						newValueMap.put(VALUE_KEY_REGIONSIZEY, getIntFromComponentValue(value, VALUE_KEY_REGIONSIZEY));
					}
					else if(COMPONENT_KEY_REGIONSIZEY_RBV.equals(componentKey)){
						newValueMap.put(VALUE_KEY_REGIONSIZEY_RBV, getIntFromComponentValue(value, VALUE_KEY_REGIONSIZEY_RBV));
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
