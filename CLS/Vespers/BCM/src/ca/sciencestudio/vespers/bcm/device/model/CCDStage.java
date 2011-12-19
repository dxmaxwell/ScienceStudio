/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *
 */
package ca.sciencestudio.vespers.bcm.device.model;

import java.util.HashMap;
import java.util.Map;

import ca.sciencestudio.device.control.composite.DeviceComposite;
import ca.sciencestudio.device.control.event.DeviceEvent;
import ca.sciencestudio.device.control.event.DeviceEventListener;
import ca.sciencestudio.device.control.event.DeviceEventType;

/**
 * @author medrand
 * 
 *         Modified this class since Aug. 27, 2009
 * @author Dong Liu
 */
public class CCDStage extends DeviceComposite<SimpleMotor> {

	protected static final String COMPONENT_KEY_MOTOR_Z = "motorZ";

	public static final String VALUE_KEY_POSITION_Z = "positionZ";

	private CcdStageDeviceEventListener listener;

	public CCDStage(String id, Map<String, SimpleMotor> components) {
		super(id, components);
		initCCDStageDeviceEventListener();
	}

	protected void initCCDStageDeviceEventListener() {
		listener = new CcdStageDeviceEventListener();
		for (Map.Entry<String, SimpleMotor> entry : getComponents().entrySet()) {
			entry.getValue().addEventListener(listener);
		}
	}

	public Object getValue() {
		Map<String, Object> value = new HashMap<String, Object>();
		value.put(VALUE_KEY_POSITION_Z, getPositionZ());
		return value;
	}

	public double getPositionZ() {
		SimpleMotor motor = getComponent(COMPONENT_KEY_MOTOR_Z);
		return motor.getPosition();
	}

	protected void initDeviceEventListeners() {
		listener = new CcdStageDeviceEventListener();
		for (Map.Entry<String, SimpleMotor> entry : getComponents().entrySet()) {
			entry.getValue().addEventListener(listener);
		}
	}

	protected String getComponentKeyByDeviceId(String deviceId) {
		for (Map.Entry<String, SimpleMotor> entry : components.entrySet()) {
			if (entry.getValue().getId().equals(deviceId)) {
				return entry.getKey();
			}
		}
		return null;
	}

	protected class CcdStageDeviceEventListener implements DeviceEventListener {

		public CcdStageDeviceEventListener() {
		}

		@SuppressWarnings("unchecked")
		public void handleEvent(DeviceEvent event) {

			String deviceId = event.getDeviceId();
			String componentKey = getComponentKeyByDeviceId(deviceId);

			DeviceEventType deviceEventType = event.getDeviceEventType();

			switch (deviceEventType) {
			case VALUE_CHANGE:
				try {
					Map<String, Object> valueMap = (Map<String, Object>) event.getValue();
					HashMap<String, Object> newValueMap = new HashMap<String, Object>();

					if (COMPONENT_KEY_MOTOR_Z.equals(componentKey)) {
						if (valueMap.containsKey(SimpleMotor.VALUE_KEY_POSITION)) {
							Object value = valueMap.get(SimpleMotor.VALUE_KEY_POSITION);
							newValueMap.put(VALUE_KEY_POSITION_Z, value);
						}
					}
					if (!newValueMap.isEmpty()) {
						publishEvent(new DeviceEvent(DeviceEventType.VALUE_CHANGE, id, newValueMap, status, alarm));
					}
				} catch (ClassCastException e) {
					log.warn("Device event value is not expected class (Map<String,Object>).");
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
