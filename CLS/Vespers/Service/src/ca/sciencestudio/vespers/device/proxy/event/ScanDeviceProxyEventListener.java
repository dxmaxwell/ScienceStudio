/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 * 		ScanDeviceProxyEventListener class.
 *     
 */
package ca.sciencestudio.vespers.device.proxy.event;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.dao.ScanDAO;
import ca.sciencestudio.device.proxy.event.DeviceProxyEvent;
import ca.sciencestudio.device.proxy.event.ReadWriteDeviceProxyEventListener;
import ca.sciencestudio.util.Parameters; 

/**
 * @author maxweld
 *
 */
public class ScanDeviceProxyEventListener extends ReadWriteDeviceProxyEventListener {

	private static final String VALUE_KEY_SCAN_ID = "scanId";
	private static final String VALUE_KEY_SCAN_DETAILS = "scanDetails";
	
	public static final String SCAN_DETAILS_KEY_PARAMETERS = "parameters";
	public static final String SCAN_DETAILS_KEY_START_DATE = "startDate";
	public static final String SCAN_DETAILS_KEY_END_DATE = "endDate";
	public static final String SCAN_DETAILS_KEY_DATA_URL = "dataUrl";
	
	private ScanDAO scanDAO;
	
	@Override
	@SuppressWarnings("unchecked")
	public void handleEvent(DeviceProxyEvent deviceProxyEvent) {
		if(getDeviceId().equals(deviceProxyEvent.getDeviceId())) {
			Map<String,Serializable> values = deviceProxyEvent.getValues();
			
			Object value;
			
			int scanId = 0;
			value = values.remove(VALUE_KEY_SCAN_ID);
			if(value instanceof Number) {
				scanId = ((Number)value).intValue();
			}
			
			Map<String,Object> scanDetails = null;
			value = values.remove(VALUE_KEY_SCAN_DETAILS);
			if(value instanceof Map) {
				scanDetails = (Map<String,Object>) value;
			}
			
			Scan scan = scanDAO.getScanById(scanId);
			if((scan != null) && (scanDetails != null)) {
				
				boolean modified = false;
				
				value = scanDetails.get(SCAN_DETAILS_KEY_START_DATE);
				if(value instanceof Date) {
					scan.setStartDate((Date)value);
					modified = true;
				}
				
				value = scanDetails.get(SCAN_DETAILS_KEY_END_DATE);
				if(value instanceof Date) {
					scan.setEndDate((Date)value);
					modified = true;
				}
				
				value = scanDetails.get(SCAN_DETAILS_KEY_DATA_URL);
				if(value instanceof String) {
					scan.setDataUrl((String)value);
					modified = true;
				}
				
				value = scanDetails.get(SCAN_DETAILS_KEY_PARAMETERS);
				if(value instanceof Parameters) {
					Parameters parameters = (Parameters)value;
					if(!parameters.isEmpty()) {
						Parameters scanParameters = scan.getParameters();
						scanParameters.putAll(parameters);
						modified = true;
					}
				}
			
				if(modified) {
					scanDAO.editScan(scan);
				}
			}
			super.handleEvent(deviceProxyEvent);
		}
	}

	public void setScanDAO(ScanDAO scanDAO) {
		this.scanDAO = scanDAO;
	}
}
