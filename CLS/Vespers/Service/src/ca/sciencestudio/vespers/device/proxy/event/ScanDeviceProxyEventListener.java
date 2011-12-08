/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		ScanDeviceProxyEventListener class.
 *     
 */
package ca.sciencestudio.vespers.device.proxy.event;

import java.util.Date;
import java.util.Map;

import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.dao.ScanAuthzDAO;
import ca.sciencestudio.device.proxy.event.DeviceProxyEvent;
import ca.sciencestudio.device.proxy.event.ReadWriteDeviceProxyEventListener;
import ca.sciencestudio.util.Parameters;
import ca.sciencestudio.util.state.StateMap;

/**
 * @author maxweld
 *
 */
public class ScanDeviceProxyEventListener extends ReadWriteDeviceProxyEventListener {

	private static final String VALUE_KEY_SCAN_GID = "scanGid";
	private static final String VALUE_KEY_SCAN_DETAILS = "scanDetails";
	private static final String VALUE_KEY_CONTROLLER_GID = "controllerGid";
	
	public static final String SCAN_DETAILS_KEY_PARAMETERS = "parameters";
	public static final String SCAN_DETAILS_KEY_START_DATE = "startDate";
	public static final String SCAN_DETAILS_KEY_END_DATE = "endDate";
	public static final String SCAN_DETAILS_KEY_DATA_URL = "dataUrl";
	
	private ScanAuthzDAO scanAuthzDAO;
	
	private StateMap beamlineSessionProxy;
	
	@Override
	@SuppressWarnings("unchecked")
	public void handleEvent(DeviceProxyEvent deviceProxyEvent) {
		if(getDeviceId().equals(deviceProxyEvent.getDeviceId())) {
			Object scanDetails = deviceProxyEvent.getValues().remove(VALUE_KEY_SCAN_DETAILS);
			super.handleEvent(deviceProxyEvent);
			
			if(!(scanDetails instanceof Map)) {
				return;
			}
			
			Object scanGid = beamlineSessionProxy.get(VALUE_KEY_SCAN_GID);
			if(!(scanGid instanceof String)) {
				return;
			}
			
			Object controllerGid = beamlineSessionProxy.get(VALUE_KEY_CONTROLLER_GID);
			if(!(controllerGid instanceof String)) {
				return;
			}
			
			Scan scan = scanAuthzDAO.get((String)controllerGid, (String)scanGid).get();
			if(scan == null) {
				return;
			}
			
			Object value = null;
			boolean modified = false;
			Map<String,Object> scanDetailsMap = (Map<String,Object>)scanDetails;
			
			value = scanDetailsMap.get(SCAN_DETAILS_KEY_START_DATE);
			if(value instanceof Date) {
				scan.setStartDate((Date)value);
				modified = true;
			}
			
			value = scanDetailsMap.get(SCAN_DETAILS_KEY_END_DATE);
			if(value instanceof Date) {
				scan.setEndDate((Date)value);
				modified = true;
			}
			
			value = scanDetailsMap.get(SCAN_DETAILS_KEY_DATA_URL);
			if(value instanceof String) {
				scan.setDataUrl((String)value);
				modified = true;
			}
			
			value = scanDetailsMap.get(SCAN_DETAILS_KEY_PARAMETERS);
			if(value instanceof Parameters) {
				Parameters parameters = (Parameters)value;
				if(!parameters.isEmpty()) {
					scan.getParameters().putAll(parameters);
					modified = true;
				}
			}
		
			if(modified) {
				scanAuthzDAO.edit((String)controllerGid, scan).get();
			}
		}
	}

	public ScanAuthzDAO getScanAuthzDAO() {
		return scanAuthzDAO;
	}
	public void setScanAuthzDAO(ScanAuthzDAO scanAuthzDAO) {
		this.scanAuthzDAO = scanAuthzDAO;
	}

	public StateMap getBeamlineSessionProxy() {
		return beamlineSessionProxy;
	}
	public void setBeamlineSessionProxy(StateMap beamlineSessionProxy) {
		this.beamlineSessionProxy = beamlineSessionProxy;
	}
}
