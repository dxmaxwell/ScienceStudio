/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractScanConverterFactory class.
 *     
 */
package ca.sciencestudio.data.converter.factory;

import java.util.Map;
import java.util.Date;
import java.util.Properties;

import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.data.converter.ConverterMap;
import ca.sciencestudio.data.support.ConverterFactoryException;
import ca.sciencestudio.util.Parameters;

/**
 * @author maxweld
 *
 */
public abstract class AbstractScanConverterFactory extends AbstractConverterFactory {

	private static final String DEFAULT_SCAN_NAME = "Not Available";
	private static final String DEFAULT_SCAN_DATA_URL = "file://";
	private static final Date DEFAULT_SCAN_START_DATE = new Date(0L);
	private static final Date DEFAULT_SCAN_END_DATE = new Date(10000L);
	private static final Parameters DEFAULT_SCAN_PARAMS = new Parameters();
	
	@SuppressWarnings("unchecked")
	protected ConverterMap validateRequest(ConverterMap request) throws ConverterFactoryException {

		request = super.validateRequest(request);
		
		// Scan Name //
		Object scanName = request.get(REQUEST_KEY_SCAN_NAME);
		if(!isNotEmptyString(scanName)) {
			Object scan = request.get(REQUEST_KEY_SCAN);
			if(scan instanceof Scan) {
				scanName = ((Scan)scan).getName();
				request.put(REQUEST_KEY_SCAN_NAME, scanName);
			}
		}
		
		if(!isNotEmptyString(scanName)) {
			scanName = DEFAULT_SCAN_NAME;
			request.put(REQUEST_KEY_SCAN_NAME, scanName);
		}
		
		// Scan Start Date //
		Object scanStartDate = request.get(REQUEST_KEY_SCAN_START_DATE);
		if(!((scanStartDate instanceof Date) || (scanStartDate instanceof Long))) {
			Object scan = request.get(REQUEST_KEY_SCAN);
			if(scan instanceof Scan) {
				scanStartDate = ((Scan)scan).getStartDate();
				request.put(REQUEST_KEY_SCAN_START_DATE, scanStartDate);
			}
		}
		
		if(scanStartDate instanceof Date) {
			// nothing to do //
		}
		else if(scanStartDate instanceof Long) {
			scanStartDate = new Date((Long)scanStartDate);
			request.put(REQUEST_KEY_SCAN_START_DATE, scanStartDate);
		}
		else {
			scanStartDate = DEFAULT_SCAN_START_DATE;
			request.put(REQUEST_KEY_SCAN_START_DATE, scanStartDate);
		}
		
		// Scan End Date //
		Object scanEndDate = request.get(REQUEST_KEY_SCAN_END_DATE);
		if(!((scanEndDate instanceof Date) || (scanEndDate instanceof Long))) {
			Object scan = request.get(REQUEST_KEY_SCAN);
			if(scan instanceof Scan) {
				scanEndDate = ((Scan)scan).getEndDate();
				request.put(REQUEST_KEY_SCAN_END_DATE, scanEndDate);
			}
		}
		
		if(scanEndDate instanceof Date) {
			// nothing to do //
		}
		else if(scanEndDate instanceof Long) {
			scanEndDate = new Date((Long)scanEndDate);
			request.put(REQUEST_KEY_SCAN_END_DATE, scanEndDate);
		}
		else {
			scanEndDate = DEFAULT_SCAN_END_DATE;
			request.put(REQUEST_KEY_SCAN_END_DATE, scanEndDate);
		}
		
		// Scan Data URL //
		Object scanDataUrl = request.get(REQUEST_KEY_SCAN_DATA_URL);
		if(!isNotEmptyString(scanDataUrl)) {
			Object scan = request.get(REQUEST_KEY_SCAN);
			if(scan instanceof Scan) {
				scanDataUrl = ((Scan)scan).getDataUrl();
				request.put(REQUEST_KEY_SCAN_DATA_URL, scanDataUrl);
			}
		}
		
		if(!isNotEmptyString(scanDataUrl)) {
			scanDataUrl = DEFAULT_SCAN_DATA_URL;
			request.put(REQUEST_KEY_SCAN_DATA_URL, scanDataUrl);
		}
		
		// Scan Params //
		Object scanParams = request.get(REQUEST_KEY_SCAN_PARAMS);
		if(!(scanParams instanceof Map)) {
			Object scan = request.get(REQUEST_KEY_SCAN);
			if(scan instanceof Scan) {
				scanParams = ((Scan)scan).getParameters();
				request.put(REQUEST_KEY_SCAN_PARAMS, scanParams);
			}
		}
		
		if(scanParams instanceof Parameters) {
			// nothing to do //
		}
		else if(scanParams instanceof Properties) {
			scanParams = new Parameters((Properties)scanParams);
			request.put(REQUEST_KEY_SCAN_PARAMS, scanParams);
		}
		else if(scanParams instanceof Map<?,?>) {
			scanParams = new Parameters((Map<String,String>)scanParams);
			request.put(REQUEST_KEY_SCAN_PARAMS, scanParams);
		}
		else {
			scanParams = new Parameters(DEFAULT_SCAN_PARAMS);
			request.put(REQUEST_KEY_SCAN_PARAMS, scanParams);
		}
		
		return request;
	}
}
