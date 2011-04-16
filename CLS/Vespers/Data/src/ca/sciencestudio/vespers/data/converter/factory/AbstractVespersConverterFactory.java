/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractVespersConverterFactory class.
 *     
 */
package ca.sciencestudio.vespers.data.converter.factory;

import ca.sciencestudio.data.converter.ConverterMap;
import ca.sciencestudio.data.support.ConverterFactoryException;
import ca.sciencestudio.data.converter.factory.AbstractModelConverterFactory;

/**
 * @author maxweld
 *
 */
public abstract class AbstractVespersConverterFactory extends AbstractModelConverterFactory {

	private static final String SUPPORTED_FACILITY_NAME = "CLS";
	private static final String SUPPORTED_LABORATORY_NAME = "VESPERS";
	private static final String SUPPORTED_INSTRUMENT_NAME = "Microprobe";
	
	@Override
	protected ConverterMap validateRequest(ConverterMap request) throws ConverterFactoryException {
		
		request = super.validateRequest(request);

		Object facilityName = request.get(REQUEST_KEY_FACILITY_NAME);
		if(!SUPPORTED_FACILITY_NAME.equals(facilityName)) {
			throw new ConverterFactoryException("Convert data from facility, " + facilityName + ", not supported.");
		}

		Object laboratoryName = request.get(REQUEST_KEY_LABORATORY_NAME);
		if(!SUPPORTED_LABORATORY_NAME.equals(laboratoryName)) {
			throw new ConverterFactoryException("Convert data from laboratory, " + laboratoryName + ", not supported.");
		}
		
		Object instrumentName = request.get(REQUEST_KEY_INSTRUMENT_NAME);
		if(!SUPPORTED_INSTRUMENT_NAME.equals(instrumentName)) {
			throw new ConverterFactoryException("Convert data from instrument, " + instrumentName + ", not supported.");
		}
		
		return request;
	}
}
