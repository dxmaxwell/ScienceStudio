/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractMapXYVespersConverterFactory class.
 *     
 */
package ca.sciencestudio.vespers.data.converter.factory;

import java.util.Collection;
import java.util.Collections;

import ca.sciencestudio.data.converter.ConverterMap;
import ca.sciencestudio.data.support.ConverterFactoryException;
import ca.sciencestudio.vespers.data.converter.AbstractMapXYVespersConverter;
import ca.sciencestudio.vespers.data.converter.adapter.factory.MapXYVespersAdapterFactory;

/**
 * @author maxweld
 *
 */
public abstract class AbstractMapXYVespersConverterFactory extends AbstractMapVespersConverterFactory {

	private Collection<MapXYVespersAdapterFactory> adapterFactories = Collections.emptyList();

	protected boolean adapterSupports(ConverterMap request) {
		for(MapXYVespersAdapterFactory af : adapterFactories) {
			if(af.supports(request)) {
				return true;
			}
		}
		return false;
	}
	
	protected void prepareAdapter(AbstractMapXYVespersConverter converter, ConverterMap request) throws ConverterFactoryException {
		for(MapXYVespersAdapterFactory af : adapterFactories) {
			if(af.supports(request)) {
				converter.setAdapter(af.getAdapter(request));
				return;
			}
		}
		throw new ConverterFactoryException("No AdapterFactory found for TO format: " + request.getToFormat());
	}

	public void setAdapterFactories(Collection<MapXYVespersAdapterFactory> adapterFactories) {
		this.adapterFactories = adapterFactories;
	}
}
