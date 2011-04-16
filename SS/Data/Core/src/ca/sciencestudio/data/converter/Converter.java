/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Converter interface.
 *     
 */
package ca.sciencestudio.data.converter;

import ca.sciencestudio.data.converter.ConverterMap;
import ca.sciencestudio.data.support.ConverterException;

/**
 * @author maxweld
 *
 */
public interface Converter {

	public ConverterMap convert() throws ConverterException;
}
