/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     MapXYVespersAdapterFactory interface.
 *     
 */
package ca.sciencestudio.vespers.data.converter.adapter.factory;

import ca.sciencestudio.data.converter.ConverterMap;
import ca.sciencestudio.data.support.ConverterFactoryException;
import ca.sciencestudio.vespers.data.converter.adapter.MapXYVespersAdapter;

/**
 * 
 * @author maxweld
 *
 */
public interface MapXYVespersAdapterFactory {
	
	public boolean supports(ConverterMap request);
	
	public MapXYVespersAdapter getAdapter(ConverterMap request) throws ConverterFactoryException;
}
