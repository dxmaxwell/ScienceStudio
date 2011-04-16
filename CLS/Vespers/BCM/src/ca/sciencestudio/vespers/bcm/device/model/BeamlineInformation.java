/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		BeamlineInformation class.
 *     
 */
package ca.sciencestudio.vespers.bcm.device.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

import ca.sciencestudio.device.control.component.DeviceComponent;
import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.event.DeviceEventListener;
import ca.sciencestudio.device.control.event.DeviceEventPublisher;
import ca.sciencestudio.device.control.event.DeviceEventType;

/**
 * @author maxweld
 *
 */
public class BeamlineInformation extends DeviceComponent implements DeviceEventListener {

	protected static final String DEFAULT_BEAMLINE_INFOMATION_PROPERTIES = "beamlineInformation.properties";
	
	protected static final String BEAMLINE_INFORMATION_PROPERTIES_COMMENT = "Beamline Inforamtion Propertoes";
	
	protected static final String VALUE_KEY_GENERAL_COMMENT = "generalComment";
	protected static final String VALUE_KEY_SPOT_SIZE_COMMENT = "spotSizeComment";
	
	private String beamlineInformationProperties = DEFAULT_BEAMLINE_INFOMATION_PROPERTIES;
	
	private Properties beamlineInformation = new Properties();
	
	public BeamlineInformation(String id, List<DeviceEventPublisher> eventPublishers) {
		super(id);
		for(DeviceEventPublisher eventPublisher : eventPublishers) {
			eventPublisher.addEventListener(this);
		}
		startPublishingEvents();
		beamlineInformation.setProperty(VALUE_KEY_GENERAL_COMMENT, "");
		beamlineInformation.setProperty(VALUE_KEY_SPOT_SIZE_COMMENT, "");
	}

	@Override
	public Object getValue() {
		Map<String,Object> value = new HashMap<String,Object>();
		value.put(VALUE_KEY_GENERAL_COMMENT, getProperty(VALUE_KEY_GENERAL_COMMENT));
		value.put(VALUE_KEY_SPOT_SIZE_COMMENT, getProperty(VALUE_KEY_SPOT_SIZE_COMMENT));
		return value;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void setValue(Object value) {
		try {
			Map<String,Object> valueMap =  (Map<String,Object>) value;
			Map<String,Object> newValueMap = new HashMap<String,Object>();
			
			if(valueMap.containsKey(VALUE_KEY_GENERAL_COMMENT)) {
				try {
					setProperty(VALUE_KEY_GENERAL_COMMENT, (String) valueMap.get(VALUE_KEY_GENERAL_COMMENT));
					newValueMap.put(VALUE_KEY_GENERAL_COMMENT, getProperty(VALUE_KEY_GENERAL_COMMENT));
				}
				catch(ClassCastException e) {
					log.warn("Set value of " + VALUE_KEY_GENERAL_COMMENT + " wrong class, expecting String.", e);
				}
			}
			
			if(valueMap.containsKey(VALUE_KEY_SPOT_SIZE_COMMENT)) {
				try {
					setProperty(VALUE_KEY_SPOT_SIZE_COMMENT, (String) valueMap.get(VALUE_KEY_SPOT_SIZE_COMMENT));
					newValueMap.put(VALUE_KEY_SPOT_SIZE_COMMENT, getProperty(VALUE_KEY_SPOT_SIZE_COMMENT));
				}
				catch(ClassCastException e) {
					log.warn("Set value of " + VALUE_KEY_SPOT_SIZE_COMMENT + " wrong class, expecting String.", e);
				}
			}
			
			if(!newValueMap.isEmpty()) {
				publishEvent(new DeviceEvent(DeviceEventType.VALUE_CHANGE, id, (Serializable) newValueMap, status, alarm));
			}	
		}
		catch (ClassCastException e) {
			log.warn("Set value wrong class, expecting Map<String,Object>.");
		}
	}
	
	protected String getProperty(String key) {
		return beamlineInformation.getProperty(key);
	}
	
	protected String getProperty(String key, String defaultValue) {
		return beamlineInformation.getProperty(key, defaultValue);
	}
	
	protected void setProperty(String key, String value) {
		beamlineInformation.setProperty(key, value);
		File beamlineInformationFile = new File(beamlineInformationProperties);
		try {
			OutputStream outputStream = new FileOutputStream(beamlineInformationFile);
			beamlineInformation.store(outputStream, BEAMLINE_INFORMATION_PROPERTIES_COMMENT);
		}
		catch(IOException e) {
			log.warn("Beamline information properties could not be stored in file: " + beamlineInformationFile.getAbsolutePath(), e); 
		}
	}

	public void setBeamlineInformationProperties(String beamlineInformationProperties) {
		if(!this.beamlineInformationProperties.equals(beamlineInformationProperties)) {
			this.beamlineInformationProperties = beamlineInformationProperties;
			File beamlineInformationFile = new File(beamlineInformationProperties);
			try {
				beamlineInformation.load(new FileInputStream(beamlineInformationFile));
			}
			catch(IOException e) {
				log.warn("Beamline information properties could not be read from file: " + beamlineInformationFile.getAbsolutePath()); 
			}
		}
	}

	public void handleEvent(DeviceEvent event) {
		
		switch(event.getDeviceEventType()) {
			case GET_VALUE:
				if(event.getDeviceId().equals(id)) {
					publishValue();
				}
				break;
				
			case SET_VALUE:
				if(event.getDeviceId().equals(id)) {
					setValue(event.getValue());
				}
				break;
			
			case VALUE_CHANGE:
				break;
			
			case ALARM_CHANGE:
			case CONNECTIVITY_CHANGE:
				break;
		}
	}
}
