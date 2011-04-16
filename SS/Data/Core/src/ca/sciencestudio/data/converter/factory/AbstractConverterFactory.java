/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractConverterFactory class.
 *     
 */
package ca.sciencestudio.data.converter.factory;

import ca.sciencestudio.util.CompareUtils;
import ca.sciencestudio.data.standard.StdConverter;
import ca.sciencestudio.data.converter.ConverterMap;
import ca.sciencestudio.data.converter.LinkedHashConverterMap;
import ca.sciencestudio.data.support.ConverterFactoryException;

/**
 * @author maxweld
 *
 */
public abstract class AbstractConverterFactory extends CompareUtils implements ConverterFactory, StdConverter {

	protected ConverterMap validateRequest(ConverterMap request) throws ConverterFactoryException {		
		return new LinkedHashConverterMap(request);
	}
}
