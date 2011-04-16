/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		MonoStatus class.
 *     
 */
package ca.sciencestudio.vespers.bcm.device.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.event.DeviceEventListener;
import ca.sciencestudio.device.control.event.DeviceEventType;
import ca.sciencestudio.device.control.component.DeviceComponent;
import ca.sciencestudio.device.control.composite.DeviceComposite;

/**
 * @author maxweld
 *
 */
public class MonoStatus extends DeviceComposite<DeviceComponent>  implements DeviceEventListener {

	protected static final String COMPONENT_KEY_MONO_ENERGY_SP = "monoEnergySP";
	protected static final String COMPONENT_KEY_MONO_ENERGY_FBK = "monoEnergyFbk";
	
	protected static final String COMPONENT_KEY_MONO_MIDDLE_SP = "monoMiddleSP";
	protected static final String COMPONENT_KEY_MONO_OUTBOARD_16_SP = "monoOutboard16SP";
	protected static final String COMPONENT_KEY_MONO_OUTBOARD_SI_SP = "monoOutboardSiSP";
	protected static final String COMPONENT_KEY_MONO_OUTBOARD_10_SP = "monoOutboard10SP";
	protected static final String COMPONENT_KEY_MONO_INBOARD_16_SP = "monoInboard16SP";
	protected static final String COMPONENT_KEY_MONO_INBOARD_SI_SP = "monoInboardSiSP";
	protected static final String COMPONENT_KEY_MONO_INBOARD_10_SP = "monoInboard10SP";
	
	protected static final String COMPONENT_KEY_MONO_MIDDLE_FBK = "monoMiddleFbk";
	protected static final String COMPONENT_KEY_MONO_OUTBOARD_16_FBK = "monoOutboard16Fbk";
	protected static final String COMPONENT_KEY_MONO_OUTBOARD_SI_FBK = "monoOutboardSiFbk";
	protected static final String COMPONENT_KEY_MONO_OUTBOARD_10_FBK = "monoOutboard10Fbk";
	protected static final String COMPONENT_KEY_MONO_INBOARD_16_FBK = "monoInboard16Fbk";
	protected static final String COMPONENT_KEY_MONO_INBOARD_SI_FBK = "monoInboardSiFbk";
	protected static final String COMPONENT_KEY_MONO_INBOARD_10_FBK = "monoInboard10Fbk";
	
	protected static final String STRIPE_DESCRIPTION_MIDDLE = "None (Middle)";
	protected static final String STRIPE_DESCRIPTION_OUTBOARD_16 = "1.6% (Outboard)";
	protected static final String STRIPE_DESCRIPTION_OUTBOARD_SI = "Si (Outboard)";
	protected static final String STRIPE_DESCRIPTION_OUTBOARD_10 = "10% (Outboard)";
	protected static final String STRIPE_DESCRIPTION_INBOARD_16 = "1.6% (Inboard)";
	protected static final String STRIPE_DESCRIPTION_INBOARD_SI = "Si (Inboard)";
	protected static final String STRIPE_DESCRIPTION_INBOARD_10 = "10% (Inboard)";	
	protected static final String STRIPE_DESCRIPTION_NA = "N/A";
	
	protected static final String VALUE_KEY_MONO_ENERGY_SP = "monoEnergySP";
	protected static final String VALUE_KEY_MONO_ENERGY_FBK = "monoEnergyFbk";
	protected static final String VALUE_KEY_MONO_STRIPE_SP = "monoStripeSP";
	protected static final String VALUE_KEY_MONO_STRIPE_FBK = "monoStripeFbk";
	
	
	public MonoStatus(String id, Map<String,DeviceComponent> components) {
		super(id, components);
		for(DeviceComponent deviceComponent : components.values()) {
			deviceComponent.addEventListener(this);
		}
	}

	@Override
	public Object getValue() {
		Map<String,Object> value = new HashMap<String,Object>();
		value.put(VALUE_KEY_MONO_ENERGY_SP, getMonoEnergySetpoint());
		value.put(VALUE_KEY_MONO_ENERGY_FBK, getMonoEnergyFeedback());
		value.put(VALUE_KEY_MONO_STRIPE_SP, getMonoStripeSetpoint());
		value.put(VALUE_KEY_MONO_STRIPE_FBK, getMonoStripeFeedback());
		return value;
	}

	public double getMonoEnergySetpoint() {
		try {
			DeviceComponent component = getComponent(COMPONENT_KEY_MONO_ENERGY_SP);
			return getMonoEnergyFeedback(component.getValue());
		}
		catch(NullPointerException e) {
			log.warn("Device component not found, " + COMPONENT_KEY_MONO_ENERGY_SP, e);
			return 0.0;
		}
	}
	
	public double getMonoEnergyFeedback() {
		try {
			DeviceComponent component = getComponent(COMPONENT_KEY_MONO_ENERGY_FBK);
			return getMonoEnergyFeedback(component.getValue());
		}
		catch(NullPointerException e) {
			log.warn("Device component not found, " + COMPONENT_KEY_MONO_ENERGY_FBK, e);
			return 0.0;
		}
	}
	
	public String getMonoStripeSetpoint() {
		if(isMonoOnStripe(COMPONENT_KEY_MONO_MIDDLE_SP)) {
			return STRIPE_DESCRIPTION_MIDDLE;
		}
		else if(isMonoOnStripe(COMPONENT_KEY_MONO_OUTBOARD_16_SP)) {
			return STRIPE_DESCRIPTION_OUTBOARD_16;
		}
		else if(isMonoOnStripe(COMPONENT_KEY_MONO_OUTBOARD_SI_SP)) {
			return STRIPE_DESCRIPTION_OUTBOARD_SI;
		}
		else if(isMonoOnStripe(COMPONENT_KEY_MONO_OUTBOARD_10_SP)) {
			return STRIPE_DESCRIPTION_OUTBOARD_10;
		}
		else if(isMonoOnStripe(COMPONENT_KEY_MONO_INBOARD_16_SP)) {
			return STRIPE_DESCRIPTION_INBOARD_16;
		}
		else if(isMonoOnStripe(COMPONENT_KEY_MONO_INBOARD_SI_SP)) {
			return STRIPE_DESCRIPTION_INBOARD_SI;
		}
		else if(isMonoOnStripe(COMPONENT_KEY_MONO_INBOARD_10_SP)) {
			return STRIPE_DESCRIPTION_INBOARD_10;
		}
		else {
			return STRIPE_DESCRIPTION_NA;
		}
	}
	
	public String getMonoStripeFeedback() {
		if(isMonoOnStripe(COMPONENT_KEY_MONO_MIDDLE_FBK)) {
			return STRIPE_DESCRIPTION_MIDDLE;
		}
		else if(isMonoOnStripe(COMPONENT_KEY_MONO_OUTBOARD_16_FBK)) {
			return STRIPE_DESCRIPTION_OUTBOARD_16;
		}
		else if(isMonoOnStripe(COMPONENT_KEY_MONO_OUTBOARD_SI_FBK)) {
			return STRIPE_DESCRIPTION_OUTBOARD_SI;
		}
		else if(isMonoOnStripe(COMPONENT_KEY_MONO_OUTBOARD_10_FBK)) {
			return STRIPE_DESCRIPTION_OUTBOARD_10;
		}
		else if(isMonoOnStripe(COMPONENT_KEY_MONO_INBOARD_16_FBK)) {
			return STRIPE_DESCRIPTION_INBOARD_16;
		}
		else if(isMonoOnStripe(COMPONENT_KEY_MONO_INBOARD_SI_FBK)) {
			return STRIPE_DESCRIPTION_INBOARD_SI;
		}
		else if(isMonoOnStripe(COMPONENT_KEY_MONO_INBOARD_10_FBK)) {
			return STRIPE_DESCRIPTION_INBOARD_10;
		}
		else {
			return STRIPE_DESCRIPTION_NA;
		}
	}
	
	protected double getMonoEnergyFeedback(Object value) {
		try {
			double[] dblArray = (double[]) value;
			return dblArray[0];
		}
		catch(ClassCastException e) {
			log.warn("Device component value is unexpected class", e);
			return 0.0;
		}
		catch(NullPointerException e) {
			log.warn("Device component value is null (Disconnected EpicsDevice?).", e);
			return 0.0;
		}
	}
	
	protected double getMonoEnergySetpoint(Object value) {
		try {
			double[] dblArray = (double[]) value;
			return dblArray[0];
		}
		catch(ClassCastException e) {
			log.warn("Device component value is unexpected class", e);
			return 0.0;
		}
		catch(NullPointerException e) {
			log.warn("Device component value is null (Disconnected EpicsDevice?).", e);
			return 0.0;
		}
	}
	
	protected boolean isMonoOnStripe(String componentKey) {
		try {
			DeviceComponent c = getComponent(componentKey);
			return isMonoOnStripe(c.getValue());
		}
		catch(NullPointerException e) {
			log.warn("Device component not found, " + componentKey, e);
			return false;
		}
	}
	
	protected boolean isMonoOnStripe(Object object) {
		try {
			double[] value = (double[]) object;
			return (value[0] == 1.0);
		}
		catch(ClassCastException e) {
			log.warn("Device component value is unexpected class.", e);
			return false;
		}
		catch(NullPointerException e) {
			log.warn("Device component value is null (Disconnected EpicsDevice?).", e);
			return false;
		}
	}
	
	public void handleEvent(DeviceEvent event) {
		
		switch (event.getDeviceEventType()) {
		
			case VALUE_CHANGE:
				Map<String,Object> newValueMap = new HashMap<String,Object>();
				try {
					String id = event.getDeviceId();
					Object value = event.getValue();
					if(getComponent(COMPONENT_KEY_MONO_ENERGY_SP).getId().equals(id)) {
						newValueMap.put(VALUE_KEY_MONO_ENERGY_SP, getMonoEnergySetpoint(value));
					}
					else if(getComponent(COMPONENT_KEY_MONO_ENERGY_FBK).getId().equals(id)) {
						newValueMap.put(VALUE_KEY_MONO_ENERGY_FBK, getMonoEnergyFeedback(value));
					}
					else if(getComponent(COMPONENT_KEY_MONO_MIDDLE_SP).getId().equals(id) && isMonoOnStripe(value)) {
						newValueMap.put(VALUE_KEY_MONO_STRIPE_SP, STRIPE_DESCRIPTION_MIDDLE);
					}
					else if(getComponent(COMPONENT_KEY_MONO_OUTBOARD_16_SP).getId().equals(id) && isMonoOnStripe(value)) {
						newValueMap.put(VALUE_KEY_MONO_STRIPE_SP, STRIPE_DESCRIPTION_OUTBOARD_16);
					}
					else if(getComponent(COMPONENT_KEY_MONO_OUTBOARD_SI_SP).getId().equals(id) && isMonoOnStripe(value)) {
						newValueMap.put(VALUE_KEY_MONO_STRIPE_SP, STRIPE_DESCRIPTION_OUTBOARD_SI);
					}
					else if(getComponent(COMPONENT_KEY_MONO_OUTBOARD_10_SP).getId().equals(id) && isMonoOnStripe(value)) {
						newValueMap.put(VALUE_KEY_MONO_STRIPE_SP, STRIPE_DESCRIPTION_OUTBOARD_10);
					}
					else if(getComponent(COMPONENT_KEY_MONO_INBOARD_16_SP).getId().equals(id) && isMonoOnStripe(value)) {
						newValueMap.put(VALUE_KEY_MONO_STRIPE_SP, STRIPE_DESCRIPTION_INBOARD_16);
					}
					else if(getComponent(COMPONENT_KEY_MONO_INBOARD_SI_SP).getId().equals(id) && isMonoOnStripe(value)) {
						newValueMap.put(VALUE_KEY_MONO_STRIPE_SP, STRIPE_DESCRIPTION_INBOARD_SI);
					}
					else if(getComponent(COMPONENT_KEY_MONO_INBOARD_10_SP).getId().equals(id) && isMonoOnStripe(value)) {
						newValueMap.put(VALUE_KEY_MONO_STRIPE_SP, STRIPE_DESCRIPTION_INBOARD_10);
					}
					else if(getComponent(COMPONENT_KEY_MONO_MIDDLE_FBK).getId().equals(id) && isMonoOnStripe(value)) {
						newValueMap.put(VALUE_KEY_MONO_STRIPE_FBK, STRIPE_DESCRIPTION_MIDDLE);
					}
					else if(getComponent(COMPONENT_KEY_MONO_OUTBOARD_16_FBK).getId().equals(id) && isMonoOnStripe(value)) {
						newValueMap.put(VALUE_KEY_MONO_STRIPE_FBK, STRIPE_DESCRIPTION_OUTBOARD_16);
					}
					else if(getComponent(COMPONENT_KEY_MONO_OUTBOARD_SI_FBK).getId().equals(id) && isMonoOnStripe(value)) {
						newValueMap.put(VALUE_KEY_MONO_STRIPE_FBK, STRIPE_DESCRIPTION_OUTBOARD_SI);
					}
					else if(getComponent(COMPONENT_KEY_MONO_OUTBOARD_10_FBK).getId().equals(id) && isMonoOnStripe(value)) {
						newValueMap.put(VALUE_KEY_MONO_STRIPE_FBK, STRIPE_DESCRIPTION_OUTBOARD_10);
					}
					else if(getComponent(COMPONENT_KEY_MONO_INBOARD_16_FBK).getId().equals(id) && isMonoOnStripe(value)) {
						newValueMap.put(VALUE_KEY_MONO_STRIPE_FBK, STRIPE_DESCRIPTION_INBOARD_16);
					}
					else if(getComponent(COMPONENT_KEY_MONO_INBOARD_SI_FBK).getId().equals(id) && isMonoOnStripe(value)) {
						newValueMap.put(VALUE_KEY_MONO_STRIPE_FBK, STRIPE_DESCRIPTION_INBOARD_SI);
					}
					else if(getComponent(COMPONENT_KEY_MONO_INBOARD_10_FBK).getId().equals(id) && isMonoOnStripe(value)) {
						newValueMap.put(VALUE_KEY_MONO_STRIPE_FBK, STRIPE_DESCRIPTION_INBOARD_10);
					}
				}
				catch(NullPointerException e) {
					log.warn("Device component not found while handling event.", e);
				}
				
				if(!newValueMap.isEmpty()) {
					publishEvent(new DeviceEvent(DeviceEventType.VALUE_CHANGE, id, (Serializable) newValueMap, status, alarm));
				}
				break;
				
			case ALARM_CHANGE:
			case CONNECTIVITY_CHANGE:
				break;
				
			default:
				log.warn("DeviceEventType, " + event.getDeviceEventType() + ", not supported.");
				break; 
		}
		
	}
}
