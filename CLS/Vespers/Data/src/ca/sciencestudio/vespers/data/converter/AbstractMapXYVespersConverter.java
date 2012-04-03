/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractMapXYVespersConverter class.
 *     
 */
package ca.sciencestudio.vespers.data.converter;

import ca.sciencestudio.data.converter.AbstractModelConverter;
import ca.sciencestudio.data.support.ConverterException;
import ca.sciencestudio.vespers.data.converter.adapter.MapXYVespersAdapter;

/**
 * 
 * @author maxweld
 *
 */
public abstract class AbstractMapXYVespersConverter extends AbstractModelConverter {

	private MapXYVespersAdapter adapter = null;
	
	public AbstractMapXYVespersConverter(String fromFormat, String toFormat, boolean forceUpdate) {
		super(fromFormat, toFormat, forceUpdate);
	}
	
	public void setAdapter(MapXYVespersAdapter adapter) {
		this.adapter = adapter;
	}
	public MapXYVespersAdapter getAdapter() throws ConverterException {
		return adapter;
	}
}
