/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ConverterFactory interface.
 *     
 */
package ca.sciencestudio.data.converter.factory;

import ca.sciencestudio.data.converter.Converter;
import ca.sciencestudio.data.converter.ConverterMap;
import ca.sciencestudio.data.support.ConverterFactoryException;

/**
 * @author maxweld
 *
 */
public interface ConverterFactory {
	
	public Converter getConverter(ConverterMap requestConverterMap) throws ConverterFactoryException;
}
