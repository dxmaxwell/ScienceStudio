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
 * A simple stage just showing the values.
 *
 * @author Dong Liu
 */
public class SimpleCCDStage extends DeviceComposite<DeviceComponent> {

	protected static final String COMPONENT_KEY_MOTOR_Z = "motorZ";

	public static final String VALUE_KEY_POSITION_Z = "positionZ";

	private CcdStageDeviceEventListener listener;

	public SimpleCCDStage(String id, Map<String, DeviceComponent> components) {
		super(id, components);
		initCCDDetectorDeviceEventListener();
	}

	private void initCCDDetectorDeviceEventListener() {
		listener = new CcdStageDeviceEventListener();
		for (Map.Entry<String, DeviceComponent> entry : getComponents()
				.entrySet()) {
			entry.getValue().addEventListener(listener);
		}
	}

	public Object getValue() {
		Map<String, Object> value = new HashMap<String, Object>();
		value.put(VALUE_KEY_POSITION_Z, getDoubleFromComponent(
				COMPONENT_KEY_MOTOR_Z, VALUE_KEY_POSITION_Z));
		return value;
	}

	public double getDoubleFromComponent(String compomentKey, String valueKey) {
		DeviceComponent deviceComponent = getComponent(compomentKey);
		Object value = deviceComponent.getValue();
		try {
			double[] dbAry = (double[]) value;
			return dbAry[0];
		} catch (ClassCastException e) {
			log.warn("Value for '" + valueKey
					+ "', is not expected class (double[]).");
			return Double.NaN;
		} catch (NullPointerException e) {
			log.warn("Value for '" + valueKey
					+ "', is null (Disconnected EpicsDevice?).");
			return Double.NaN;
		}
	}

	/*
	 * public double getSetPointZ() { SimpleMotor motor =
	 * getComponent(COMPONENT_KEY_MOTOR_Z); return motor.getSetPoint(); }
	 */

	protected String getComponentKeyByDeviceId(String deviceId) {
		for (Map.Entry<String, DeviceComponent> entry : components.entrySet()) {
			if (entry.getValue().getId().equals(deviceId)) {
				return entry.getKey();
			}
		}
		return new String();
	}

	protected class CcdStageDeviceEventListener implements DeviceEventListener {

		public CcdStageDeviceEventListener() {
		}
		
		public void handleEvent(DeviceEvent event) {

			String deviceId = event.getDeviceId();
			String componentKey = getComponentKeyByDeviceId(deviceId);

			DeviceEventType deviceEventType = event.getDeviceEventType();

			switch (deviceEventType) {
			case VALUE_CHANGE:
				Object value = event.getValue();
				HashMap<String, Object> newValueMap = new HashMap<String, Object>();

				if (COMPONENT_KEY_MOTOR_Z.equals(componentKey)) {

					newValueMap.put(VALUE_KEY_POSITION_Z, value);
				}
				if (!newValueMap.isEmpty()) {
					publishEvent(new DeviceEvent(DeviceEventType.VALUE_CHANGE,
							id, newValueMap, status, alarm));
				}

				break;

			case ALARM_CHANGE:
			case CONNECTIVITY_CHANGE:
				break;

			default:
				log.warn("DeviceEventType, " + deviceEventType.toString()
						+ ", not supported.");
				break;
			}
		}
	}
}
