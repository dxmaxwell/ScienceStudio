/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		MirrorStatus class.
 *     
 */
package ca.sciencestudio.vespers.bcm.device.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.event.DeviceEventType;
import ca.sciencestudio.device.control.event.DeviceEventListener;
import ca.sciencestudio.device.control.component.DeviceComponent;
import ca.sciencestudio.device.control.composite.DeviceComposite;

/**
 * @author maxweld
 *
 */
public class MirrorStatus extends DeviceComposite<DeviceComponent> implements DeviceEventListener {
	
	protected static final String STRIPE_DESCRIPTION_PT = "Pt";
	protected static final String STRIPE_DESCRIPTION_SI = "Si";
	protected static final String STRIPE_DESCRIPTION_PD = "Pd";
	protected static final String STRIPE_DESCRIPTION_NA = "N/A";
	
	protected static final String COMPONENT_KEY_MIRROR1A_PT = "mirror1AOnPt";
	protected static final String COMPONENT_KEY_MIRROR1A_SI = "mirror1AOnSi";
	protected static final String COMPONENT_KEY_MIRROR1A_PD = "mirror1AOnPd";
	
	protected static final String COMPONENT_KEY_MIRROR1B_PT = "mirror1BOnPt";
	protected static final String COMPONENT_KEY_MIRROR1B_SI = "mirror1BOnSi";
	protected static final String COMPONENT_KEY_MIRROR1B_PD = "mirror1BOnPd";
	
	protected static final String COMPONENT_KEY_MIRROR2A_PT = "mirror2AOnPt";
	protected static final String COMPONENT_KEY_MIRROR2A_SI = "mirror2AOnSi";
	protected static final String COMPONENT_KEY_MIRROR2A_PD = "mirror2AOnPd";
	
	protected static final String COMPONENT_KEY_MIRROR2B_PT = "mirror2BOnPt";
	protected static final String COMPONENT_KEY_MIRROR2B_SI = "mirror2BOnSi";
	protected static final String COMPONENT_KEY_MIRROR2B_PD = "mirror2BOnPd";
	
	protected static final String VALUE_KEY_MIRROR1A_STRIPE = "m1AStripe";
	protected static final String VALUE_KEY_MIRROR1B_STRIPE = "m1BStripe";
	protected static final String VALUE_KEY_MIRROR2A_STRIPE = "m2AStripe";
	protected static final String VALUE_KEY_MIRROR2B_STRIPE = "m2BStripe";
	
	
	public MirrorStatus(String id, Map<String, DeviceComponent> components) {
		super(id, components);
		for(DeviceComponent deviceComponent : components.values()) {
			deviceComponent.addEventListener(this);
		}
	}
	
	@Override
	public Object getValue() {
		Map<String,Object> value = new HashMap<String,Object>();
		value.put(VALUE_KEY_MIRROR1A_STRIPE, getMirror1AStripe());
		value.put(VALUE_KEY_MIRROR1B_STRIPE, getMirror1BStripe());
		value.put(VALUE_KEY_MIRROR2A_STRIPE, getMirror2AStripe());
		value.put(VALUE_KEY_MIRROR2B_STRIPE, getMirror2BStripe());
		return value;
	}

	public String getMirror1AStripe() {
		if(isMirrorOnStripe(COMPONENT_KEY_MIRROR1A_PT)) {
			return STRIPE_DESCRIPTION_PT;
		}
		else if(isMirrorOnStripe(COMPONENT_KEY_MIRROR1A_SI)) {
			return STRIPE_DESCRIPTION_SI;
		}
		else if(isMirrorOnStripe(COMPONENT_KEY_MIRROR1A_PD)) {
			return STRIPE_DESCRIPTION_PD;
		}
		else {
			return STRIPE_DESCRIPTION_NA;
		}
	}

	public String getMirror1BStripe() {
		if(isMirrorOnStripe(COMPONENT_KEY_MIRROR1B_PT)) {
			return STRIPE_DESCRIPTION_PT;
		}
		else if(isMirrorOnStripe(COMPONENT_KEY_MIRROR1B_SI)) {
			return STRIPE_DESCRIPTION_SI;
		}
		else if(isMirrorOnStripe(COMPONENT_KEY_MIRROR1B_PD)) {
			return STRIPE_DESCRIPTION_PD;
		}
		else {
			return STRIPE_DESCRIPTION_NA;
		}
	}
	
	public String getMirror2AStripe() {
		if(isMirrorOnStripe(COMPONENT_KEY_MIRROR2A_PT)) {
			return STRIPE_DESCRIPTION_PT;
		}
		else if(isMirrorOnStripe(COMPONENT_KEY_MIRROR2A_SI)) {
			return STRIPE_DESCRIPTION_SI;
		}
		else if(isMirrorOnStripe(COMPONENT_KEY_MIRROR2A_PD)) {
			return STRIPE_DESCRIPTION_PD;
		}
		else {
			return STRIPE_DESCRIPTION_NA;
		}
	}
	
	public String getMirror2BStripe() {
		if(isMirrorOnStripe(COMPONENT_KEY_MIRROR2B_PT)) {
			return STRIPE_DESCRIPTION_PT;
		}
		else if(isMirrorOnStripe(COMPONENT_KEY_MIRROR2B_SI)) {
			return STRIPE_DESCRIPTION_SI;
		}
		else if(isMirrorOnStripe(COMPONENT_KEY_MIRROR2B_PD)) {
			return STRIPE_DESCRIPTION_PD;
		}
		else {
			return STRIPE_DESCRIPTION_NA;
		}
	}
	
	protected boolean isMirrorOnStripe(String componentKey) {
		try {
			DeviceComponent c = getComponent(componentKey);
			return isMirrorOnStripe(c.getValue());
		}
		catch(NullPointerException e) {
			log.warn("Device component not found, " + componentKey, e);
			return false;
		}
	}
	
	protected boolean isMirrorOnStripe(Object object) {
		try {
			double[] value = (double[]) object;
			return (value[0] == 1.0);
		}
		catch(ClassCastException e) {
			log.warn("Device component value is unexpected class.", e);
			return false;
		}
		catch(NullPointerException e) {
			log.warn("Device component value is null. (Disconnected EpicsDevice?).", e);
			return false;
		}
	}
	
	public void handleEvent(DeviceEvent event) {
		
		switch (event.getDeviceEventType()) {
		
			case VALUE_CHANGE:
				Map<String,Serializable> newValueMap = new HashMap<String,Serializable>();
				try {
					String id = event.getDeviceId();
					Object value = event.getValue();
					if(getComponent(COMPONENT_KEY_MIRROR1A_PT).getId().equals(id) && isMirrorOnStripe(value)) {
						newValueMap.put(VALUE_KEY_MIRROR1A_STRIPE, STRIPE_DESCRIPTION_PT);
					}
					else if(getComponent(COMPONENT_KEY_MIRROR1A_SI).getId().equals(id) && isMirrorOnStripe(value)) {
						newValueMap.put(VALUE_KEY_MIRROR1A_STRIPE, STRIPE_DESCRIPTION_SI);
					}
					else if(getComponent(COMPONENT_KEY_MIRROR1A_PD).getId().equals(id) && isMirrorOnStripe(value)) {
						newValueMap.put(VALUE_KEY_MIRROR1A_STRIPE, STRIPE_DESCRIPTION_PD);
					}
					else if(getComponent(COMPONENT_KEY_MIRROR1B_PT).getId().equals(id) && isMirrorOnStripe(value)) {
						newValueMap.put(VALUE_KEY_MIRROR1B_STRIPE, STRIPE_DESCRIPTION_PT);
					}
					else if(getComponent(COMPONENT_KEY_MIRROR1B_SI).getId().equals(id) && isMirrorOnStripe(value)) {
						newValueMap.put(VALUE_KEY_MIRROR1B_STRIPE, STRIPE_DESCRIPTION_SI);
					}
					else if( getComponent(COMPONENT_KEY_MIRROR1B_PD).getId().equals(id) && isMirrorOnStripe(value)) {
						newValueMap.put(VALUE_KEY_MIRROR1B_STRIPE, STRIPE_DESCRIPTION_PD);
					}
					else if(getComponent(COMPONENT_KEY_MIRROR2A_PT).getId().equals(id) && isMirrorOnStripe(value)) {
						newValueMap.put(VALUE_KEY_MIRROR2A_STRIPE, STRIPE_DESCRIPTION_PT);
					}
					else if(getComponent(COMPONENT_KEY_MIRROR2A_SI).getId().equals(id) && isMirrorOnStripe(value)) {
						newValueMap.put(VALUE_KEY_MIRROR2A_STRIPE, STRIPE_DESCRIPTION_SI);
					}
					else if(getComponent(COMPONENT_KEY_MIRROR2A_PD).getId().equals(id) && isMirrorOnStripe(value)) {
						newValueMap.put(VALUE_KEY_MIRROR2A_STRIPE, STRIPE_DESCRIPTION_PD);
					}
					else if(getComponent(COMPONENT_KEY_MIRROR2B_PT).getId().equals(id) && isMirrorOnStripe(value)) {
						newValueMap.put(VALUE_KEY_MIRROR2B_STRIPE, STRIPE_DESCRIPTION_PT);
					}
					else if(getComponent(COMPONENT_KEY_MIRROR2B_SI).getId().equals(id) && isMirrorOnStripe(value)) {
						newValueMap.put(VALUE_KEY_MIRROR2B_STRIPE, STRIPE_DESCRIPTION_SI);
					}
				    else if(getComponent(COMPONENT_KEY_MIRROR2B_PD).getId().equals(id) && isMirrorOnStripe(value)) {
				    	newValueMap.put(VALUE_KEY_MIRROR2B_STRIPE, STRIPE_DESCRIPTION_PD);
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
