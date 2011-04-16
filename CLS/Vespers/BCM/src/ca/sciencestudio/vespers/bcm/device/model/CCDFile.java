/**
 * Copyright (c) Canadian Light Source, Inc. All rights reserved.
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
 * This class provides access to the PV's related to Vespers CCD files.
 * Since primitive types like short and double cannot be used as generic types, two methods
 * are used to get the value from EpicDevice: getShortFromComponent() and getStringFromComponent().
 *
 * @author Dong Liu
 *
 */
public class CCDFile extends DeviceComposite<DeviceComponent> {

	protected static final String COMPONENT_KEY_FILEPATH_RBV = "filePathRBV";
	protected static final String COMPONENT_KEY_FILEPATH = "filePath";
	protected static final String COMPONENT_KEY_FILENAME_RBV = "fileNameRBV";
	protected static final String COMPONENT_KEY_FILENAME = "fileName";
	protected static final String COMPONENT_KEY_FILENUMBER = "fileNumber";
	protected static final String COMPONENT_KEY_FILENUMBER_RBV = "fileNumberRBV";
	protected static final String COMPONENT_KEY_AUTOINCREMENT = "autoIncrement";
	protected static final String COMPONENT_KEY_AUTOINCREMENT_RBV = "autoIncrementRBV";
	protected static final String COMPONENT_KEY_FILETEMPLATE = "fileTemplate";
	protected static final String COMPONENT_KEY_FILETEMPLATE_RBV = "fileTemplateRBV";
	protected static final String COMPONENT_KEY_FULLFILENAME_RBV = "fullfilenameRBV";
	protected static final String COMPONENT_KEY_FILEFORMAT_RBV = "fileFormatRBV";

	protected static final String[] CCD_FILE_FORMARTS = {"Unavailable", "SPE", "TIFF"};
	protected static final String[] CCD_FILE_INCREMENT = {"Unavailable", "No", "Yes"};

	public static final String VALUE_KEY_FILEPATH_RBV = "filePathRBV";
	public static final String VALUE_KEY_FILEPATH = "filePath";
	public static final String VALUE_KEY_FILENAME_RBV = "fileNameRBV";
	public static final String VALUE_KEY_FILENAME = "fileName";
	public static final String VALUE_KEY_FILENUMBER = "fileNumber";
	public static final String VALUE_KEY_FILENUMBER_RBV = "fileNumberRBV";
	public static final String VALUE_KEY_AUTOINCREMENT = "autoIncrement";
	public static final String VALUE_KEY_AUTOINCREMENT_RBV = "autoIncrementRBV";
	public static final String VALUE_KEY_FILETEMPLATE = "fileTemplate";
	public static final String VALUE_KEY_FILETEMPLATE_RBV = "fileTemplateRBV";
	public static final String VALUE_KEY_FULLFILENAME_RBV = "fullfilenameRBV";
	public static final String VALUE_KEY_FILEFORMAT_RBV = "fileFormatRBV";

	private CcdFileDeviceEventListener listener;

	public CCDFile(String id, Map<String, DeviceComponent> map) {
		super(id, map);
		initCCDFileDeviceEventListener();
	}

	protected void initCCDFileDeviceEventListener() {
		listener = new CcdFileDeviceEventListener();
		for(Map.Entry<String,DeviceComponent> entry : getComponents().entrySet()){
			entry.getValue().addEventListener(listener);
		}
	}

	public Object getValue() {
		Map<String, Object> value = new HashMap<String, Object>();
		value.put(VALUE_KEY_FILEPATH_RBV, getStringFromComponent(COMPONENT_KEY_FILEPATH_RBV, VALUE_KEY_FILEPATH_RBV));
		value.put(VALUE_KEY_FILEPATH, getStringFromComponent(COMPONENT_KEY_FILEPATH, VALUE_KEY_FILEPATH));
		value.put(VALUE_KEY_FILENAME_RBV, getStringFromComponent(COMPONENT_KEY_FILENAME_RBV, VALUE_KEY_FILENAME_RBV));
		value.put(VALUE_KEY_FILENAME, getStringFromComponent(COMPONENT_KEY_FILENAME, VALUE_KEY_FILENAME));
		value.put(VALUE_KEY_FILENUMBER, getIntFromComponent(COMPONENT_KEY_FILENUMBER, VALUE_KEY_FILENUMBER));
		value.put(VALUE_KEY_FILENUMBER_RBV, getIntFromComponent(COMPONENT_KEY_FILENUMBER_RBV, VALUE_KEY_FILENUMBER_RBV));
		value.put(VALUE_KEY_AUTOINCREMENT, getEnumString(getShortFromComponent(COMPONENT_KEY_AUTOINCREMENT, VALUE_KEY_AUTOINCREMENT), CCD_FILE_INCREMENT));
		value.put(VALUE_KEY_AUTOINCREMENT_RBV, getEnumString(getShortFromComponent(COMPONENT_KEY_AUTOINCREMENT_RBV, VALUE_KEY_AUTOINCREMENT_RBV), CCD_FILE_INCREMENT));
		value.put(VALUE_KEY_FILETEMPLATE, getStringFromComponent(COMPONENT_KEY_FILETEMPLATE, VALUE_KEY_FILETEMPLATE));
		value.put(VALUE_KEY_FILETEMPLATE_RBV, getStringFromComponent(COMPONENT_KEY_FILETEMPLATE_RBV, VALUE_KEY_FILETEMPLATE_RBV));
		value.put(VALUE_KEY_FULLFILENAME_RBV, getStringFromComponent(COMPONENT_KEY_FULLFILENAME_RBV, VALUE_KEY_FULLFILENAME_RBV));
		value.put(VALUE_KEY_FILEFORMAT_RBV, getEnumString(getShortFromComponent(COMPONENT_KEY_FILEFORMAT_RBV, VALUE_KEY_FILEFORMAT_RBV), CCD_FILE_FORMARTS));
		return value;
	}

	public String getEnumString(short index, String[] elements) {
		int i = index + 1;
		return (i >= 0 && i < elements.length) ? elements[i]:(new String("Unknow"));
	}

	/**
	 * @param compomentKey
	 * @param valueKey
	 * @return a String converted from a DBR_CHAR array with the /0 removed.
	 */
	public String getStringFromComponent(String compomentKey, String valueKey) {
		DeviceComponent deviceComponent = getComponent(compomentKey);
		Object value = deviceComponent.getValue();
		try {
			byte[] byteAry = (byte[]) value;
			int index = 0;
			int length = byteAry.length;
			for (;index < length; index ++) {
				if (byteAry[index] == (byte) 0)
					break;
			}
			return new String(byteAry, 0, index);
		}
		catch(ClassCastException e) {
			log.warn("Value for '" + valueKey + "', is not expected class (byte[]).");
			return null;
		}
		catch(NullPointerException e) {
			log.warn("Value for '" + valueKey + "', is null (Disconnected EpicsDevice?).");
			return null;
		}
	}

	public String getStringFromComponentValue(Object value, String valueKey) {
		try {
			byte[] byteAry = (byte[]) value;
			int index = 0;
			int length = byteAry.length;
			for (;index < length; index ++) {
				if (byteAry[index] == (byte) 0)
					break;
			}
			return new String(byteAry, 0, index);
		}
		catch(ClassCastException e) {
			log.warn("Value for '" + valueKey + "', is not expected class (byte[]).");
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
	 * @return a short number converted from a DBR_ENUM array. -1 represents an invalid value.
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


	@SuppressWarnings("unchecked")
	public void setValue(Object value) {
		try{
			Map<String,Object> valueMap = (Map<String,Object>) value;

			if(valueMap.containsKey(VALUE_KEY_FILEPATH)) {
				String filePath = (String) valueMap.get(VALUE_KEY_FILEPATH);
				setStringToComponent(COMPONENT_KEY_FILEPATH,filePath);
			}

			if(valueMap.containsKey(VALUE_KEY_FILENAME)) {
				String fileName = (String) valueMap.get(VALUE_KEY_FILENAME);
				setStringToComponent(COMPONENT_KEY_FILENAME,fileName);
			}

			if(valueMap.containsKey(VALUE_KEY_FILENUMBER)) {
				int fileNumber = ((Integer) valueMap.get(VALUE_KEY_FILENUMBER)).intValue();
				setIntToComponent(fileNumber,COMPONENT_KEY_FILENUMBER);
			}

			if(valueMap.containsKey(VALUE_KEY_AUTOINCREMENT)) {
				short autoInrement = ((Short) valueMap.get(VALUE_KEY_AUTOINCREMENT)).shortValue();
				setShortToComponent(autoInrement,COMPONENT_KEY_AUTOINCREMENT);
			}

			if(valueMap.containsKey(VALUE_KEY_FILETEMPLATE)) {
				String fileTemplate = (String) valueMap.get(VALUE_KEY_FILETEMPLATE);
				setStringToComponent(COMPONENT_KEY_FILETEMPLATE,fileTemplate);
			}

		}
		catch(ClassCastException e) {
			log.warn("Set value from a map. ", e);
		}
	}


	private void setShortToComponent(short number,
			String componentKey) {
		DeviceComponent deviceComponent = getComponent(componentKey);
		deviceComponent.setValue(new short[] { number });

	}


	/**
	 * @param number
	 * @param componentKey
	 * Setting an integer to DBR_LONG[] type PV
	 */
	private void setIntToComponent(int number,
			String componentKey) {
		DeviceComponent deviceComponent = getComponent(componentKey);
		deviceComponent.setValue(new int[] { number });

	}


	/**
	 * @param componentKey
	 * @param name
	 *
	 * Setting a String to DBR_CHAR[] type PV
	 */
	private void setStringToComponent(String componentKey,
			String name) {
		DeviceComponent deviceComponent = getComponent(componentKey);
		byte[] charArray = new byte[256];
		byte[] stringByte = name.getBytes();
		try {
			System.arraycopy(stringByte, 0, charArray, 0, stringByte.length);
		}
		catch (IndexOutOfBoundsException e) {
			log.error("The string is longer than 256.");
			byte[] wrong = new byte[]{'f','a','i','l', ' ', 't','o', ' ', 's','e','t'};
			System.arraycopy(wrong, 0, charArray, 0, wrong.length);
		}
		deviceComponent.setValue(charArray);

	}

	protected String getComponentKeyByDeviceId(String deviceID) {
		for(Map.Entry<String,DeviceComponent> entry : components.entrySet()) {
			if(entry.getValue().getId().equals(deviceID)) {
				return entry.getKey();
			}
		}
		return new String();
	}


	protected class CcdFileDeviceEventListener implements DeviceEventListener {

		public CcdFileDeviceEventListener() {}


		public void handleEvent(DeviceEvent aEvent) {
			String deviceID = aEvent.getDeviceId();
			String componentKey = getComponentKeyByDeviceId(deviceID);
			DeviceEventType deviceEventType = aEvent.getDeviceEventType();

			switch (deviceEventType) {
			case VALUE_CHANGE:

				Object value = aEvent.getValue();

				HashMap<String,Object> newValueMap = new HashMap<String,Object>();


				if(COMPONENT_KEY_FILEPATH_RBV.equals(componentKey)) {
					newValueMap.put(VALUE_KEY_FILEPATH_RBV, getStringFromComponentValue(value, VALUE_KEY_FILEPATH_RBV));
				}
				else if(COMPONENT_KEY_FILEPATH.equals(componentKey)) {
					newValueMap.put(VALUE_KEY_FILEPATH, getStringFromComponentValue(value, VALUE_KEY_FILEPATH));
				}
				else if(COMPONENT_KEY_FILENAME_RBV.equals(componentKey)) {
					newValueMap.put(VALUE_KEY_FILENAME_RBV, getStringFromComponentValue(value, VALUE_KEY_FILENAME_RBV));
				}
				else if(COMPONENT_KEY_FILENAME.equals(componentKey)) {
					newValueMap.put(VALUE_KEY_FILENAME, getStringFromComponentValue(value, VALUE_KEY_FILENAME));
				}
				else if(COMPONENT_KEY_FILENUMBER.equals(componentKey)) {
					newValueMap.put(VALUE_KEY_FILENUMBER, getIntFromComponentValue(value, VALUE_KEY_FILENUMBER));
				}
				else if(COMPONENT_KEY_FILENUMBER_RBV.equals(componentKey)) {
					newValueMap.put(VALUE_KEY_FILENUMBER_RBV, getIntFromComponentValue(value, VALUE_KEY_FILENUMBER_RBV));
				}
				else if(COMPONENT_KEY_AUTOINCREMENT.equals(componentKey)) {
					newValueMap.put(VALUE_KEY_AUTOINCREMENT, getEnumString(getShortFromComponent(COMPONENT_KEY_AUTOINCREMENT, VALUE_KEY_AUTOINCREMENT), CCD_FILE_INCREMENT));
				}
				else if(COMPONENT_KEY_AUTOINCREMENT_RBV.equals(componentKey)) {
					newValueMap.put(VALUE_KEY_AUTOINCREMENT_RBV, getEnumString(getShortFromComponent(COMPONENT_KEY_AUTOINCREMENT_RBV, VALUE_KEY_AUTOINCREMENT_RBV), CCD_FILE_INCREMENT));
				}
				else if(COMPONENT_KEY_FILEFORMAT_RBV.equals(componentKey)) {
					newValueMap.put(VALUE_KEY_FILEFORMAT_RBV, getEnumString(getShortFromComponent(COMPONENT_KEY_FILEFORMAT_RBV, VALUE_KEY_FILEFORMAT_RBV), CCD_FILE_FORMARTS));
				}
				else if(COMPONENT_KEY_FILETEMPLATE.equals(componentKey)) {
					newValueMap.put(VALUE_KEY_FILETEMPLATE, getStringFromComponentValue(value, VALUE_KEY_FILETEMPLATE));
				}
				else if(COMPONENT_KEY_FILETEMPLATE_RBV.equals(componentKey)) {
					newValueMap.put(VALUE_KEY_FILETEMPLATE_RBV, getStringFromComponentValue(value, VALUE_KEY_FILETEMPLATE_RBV));
				}
				else if(COMPONENT_KEY_FULLFILENAME_RBV.equals(componentKey)) {
					newValueMap.put(VALUE_KEY_FULLFILENAME_RBV, getStringFromComponentValue(value, VALUE_KEY_FULLFILENAME_RBV));
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
